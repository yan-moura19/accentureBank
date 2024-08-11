package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.CreateContaDTO;
import acc.br.accenturebank.dto.CreateTransacaoDTO;
import acc.br.accenturebank.dto.UpdateContaDTO;
import acc.br.accenturebank.exception.SaldoInsuficienteException;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.model.enums.Operacao;
import acc.br.accenturebank.model.enums.TipoConta;
import acc.br.accenturebank.repository.ContaRepository;
import acc.br.accenturebank.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private AgenciaService agenciaService;

    @Autowired
    private TransacaoService transacaoService;




    public Conta createConta(CreateContaDTO createContaDTO) {

        try{
            final int idCliente = createContaDTO.getIdCliente();
            Cliente cliente = clienteService.getClienteById(idCliente);

            if (cliente == null) {
                throw new ResourceNotFoundException("Cliente com id %d não foi encontrado.".formatted(idCliente));
            }

            final int idAgencia = createContaDTO.getIdAgencia();
            Agencia agencia = agenciaService.getAgenciaById(idAgencia);

            if (agencia == null) {
                throw new ResourceNotFoundException("Agência com id %d não foi encontrada.".formatted(idAgencia));
            }

            Conta conta = Conta.builder()
                    .tipoConta(createContaDTO.getTipoConta())
                    .agencia(agencia)
                    .cliente(cliente)
                    .build();

            return contaRepository.save(conta);
        }catch(Exception e){
            throw new RuntimeException("Falha ao criar a Conta: ".concat(e.getMessage()), e);
        }
    }

    public Conta getContaById(Long id) {
        return contaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Conta com id %d não foi encontrada.".formatted(id)));
    }

    public List<Conta> getAllContas() {
        return contaRepository.findAll();
    }

    public Conta updateConta(Long id, UpdateContaDTO updateContaDTO) {

       try{
           Conta conta = this.getContaById(id);

           TipoConta novoTipoConta = updateContaDTO.getTipoConta();
           int novoIdAgencia = updateContaDTO.getIdAgencia();
           int novoIdCliente = updateContaDTO.getIdCliente();
           BigDecimal novoSaldo = updateContaDTO.getSaldo();
           BigDecimal novoSaldoSeparado = updateContaDTO.getSaldoSeparado();
           boolean novoAtiva = updateContaDTO.isAtiva();
           boolean novoPixAtivo = updateContaDTO.isPixAtivo();

           if(novoTipoConta != null){
               conta.setTipoConta(novoTipoConta);
           }

           if(novoSaldo != null){
               conta.setSaldo(novoSaldo);
           }

           if(novoSaldoSeparado != null){
               conta.setSaldo(novoSaldoSeparado);
           }

           conta.setAtiva(novoAtiva);

           conta.setPixAtivo(novoPixAtivo);

           Agencia novaAgencia = agenciaService.getAgenciaById(novoIdAgencia);

           if(novaAgencia != null){
               conta.setAgencia(novaAgencia);
           }else{
               throw new ResourceNotFoundException("Agência com id %d não foi encontrada.".formatted(novoIdAgencia));
           }

           Cliente novoCliente = clienteService.getClienteById(novoIdCliente);

           if(novoCliente != null){
               conta.setCliente(novoCliente);
           }else{
               throw new ResourceNotFoundException("Cliente com id %d não foi encontrado.".formatted(novoIdCliente));
           }

           return contaRepository.save(conta);

       }catch( Exception e){
           throw new RuntimeException("Falha ao atualizar a Conta: ".concat(e.getMessage()), e);
       }
    }

    public void deleteConta(Long id) {
        try{
            this.getContaById(id);

            contaRepository.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("Falha ao deletar a Conta: ".concat(e.getMessage()), e);
        }
    }

    @Transactional
    public Conta separarValor(long id, BigDecimal valor) {

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }

        Conta conta = this.getContaById(id);
        BigDecimal saldo = conta.getSaldo();
        BigDecimal saldoSeparado = conta.getSaldoSeparado();

        if (saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para separar o valor.");
        }

        conta.setSaldo(saldo.subtract(valor));
        conta.setSaldoSeparado(saldoSeparado.add(valor));

        contaRepository.save(conta);

        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.SEPARACAO,
                "Separação de valor",
                valor,
                conta.getIdConta()
        ));

        return conta;
    }

    @Transactional
    public Conta resgatarSaldoSeparado(long id, BigDecimal valor) {


        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }

        Conta conta = this.getContaById(id);

        BigDecimal saldo = conta.getSaldo();
        BigDecimal saldoSeparado = conta.getSaldoSeparado();

        if (saldoSeparado.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("SaldoSeparado insuficiente para separar o valor.");
        }

        conta.setSaldoSeparado(saldoSeparado.subtract(valor));
        conta.setSaldo(saldo.add(valor));

        contaRepository.save(conta);

        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.RESGATE,
                "Resgate de SaldoSeparado",
                valor,
                conta.getIdConta()
        ));

        return conta;
    }

    @Transactional
    public void transferir(Long idContaOrigem, long idContaDestino, BigDecimal valor) {
        Conta contaOrigem = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta de origem não encontrada"));

        Conta contaDestino = contaRepository.findByNumero(numeroContaDestino)
                .orElseThrow(() -> new EntityNotFoundException("Conta de destino não encontrada"));

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para transferência");
        }


        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));


        Transacao transacaoDebito = new Transacao();
        transacaoDebito.setDataTransacao(LocalDateTime.now());
        transacaoDebito.setOperacao(Operacao.TRANSFERENCIA);
        transacaoDebito.setDescricao("Transferência para a conta " + numeroContaDestino);
        transacaoDebito.setValor(valor);
        transacaoDebito.setConta(contaOrigem);
        transacaoRepository.save(transacaoDebito);


        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));


        Transacao transacaoCredito = new Transacao();
        transacaoCredito.setDataTransacao(LocalDateTime.now());
        transacaoCredito.setOperacao(Operacao.RECEBIMENTO_TRANSFERENCIA);
        transacaoCredito.setDescricao("Recebimento de transferência da conta " + contaOrigem.getNumero() + " - " + contaOrigem.getCliente().getNome());
        transacaoCredito.setValor(valor);
        transacaoCredito.setConta(contaDestino);
        transacaoRepository.save(transacaoCredito);


        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
    }

    @Autowired
    private TransacaoRepository transacaoRepository;

    public Conta realizarRecarga(Long idConta, String numeroCelular, BigDecimal valor) {
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));


        conta.setSaldo(conta.getSaldo().subtract(valor));


        Transacao transacao = new Transacao();
        transacao.setDataTransacao(LocalDateTime.now());
        transacao.setOperacao(Operacao.RECARGA);
        transacao.setDescricao("Recarga de celular para o número " + numeroCelular);
        transacao.setValor(valor);
        transacao.setConta(conta);


        transacaoRepository.save(transacao);


        return contaRepository.save(conta);
    }

    public Conta realizarSaque(Long idConta, BigDecimal valor) {
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }
        conta.setSaldo(conta.getSaldo().subtract(valor));
        Transacao transacao = new Transacao();
        transacao.setDataTransacao(LocalDateTime.now());
        transacao.setOperacao(Operacao.SAQUE);
        transacao.setDescricao("S/ D");
        transacao.setValor(valor);
        transacao.setConta(conta);
        transacaoRepository.save(transacao);


        return contaRepository.save(conta);
    }

    public Conta realizarPagamento(Long idConta, BigDecimal valor) {
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }
        conta.setSaldo(conta.getSaldo().subtract(valor));
        Transacao transacao = new Transacao();
        transacao.setDataTransacao(LocalDateTime.now());
        transacao.setOperacao(Operacao.PAGAMENTO);
        transacao.setDescricao("Pagamento feito via app");
        transacao.setValor(valor);
        transacao.setConta(conta);
        transacaoRepository.save(transacao);


        return contaRepository.save(conta);
    }

    public Conta realizarDeposito(Long idConta, BigDecimal valor) {

        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));


        conta.setSaldo(conta.getSaldo().add(valor));


        Transacao transacao = new Transacao();
        transacao.setDataTransacao(LocalDateTime.now());
        transacao.setOperacao(Operacao.DEPOSITO);
        transacao.setDescricao("S/ D");
        transacao.setValor(valor);
        transacao.setConta(conta);


        transacaoRepository.save(transacao);


        return contaRepository.save(conta);
    }

}