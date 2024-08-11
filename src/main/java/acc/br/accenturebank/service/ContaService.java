package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.*;
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
import java.util.Random;
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

            String numero = gerarNumeroContaUnico();

            Conta conta = Conta.builder()
                    .tipoConta(createContaDTO.getTipoConta())
                    .agencia(agencia)
                    .cliente(cliente)
                    .numero(numero)
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

    private Conta getContaByNumero(String numero) {
        return contaRepository.findByNumero(numero)
                .orElseThrow(() -> new EntityNotFoundException("Conta de numero %s não foi encontrada.".formatted(numero)));
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
                "Separação de valor - R$ " + valor,
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
                "Resgate de SaldoSeparado - R$ " + valor,
                valor,
                conta.getIdConta()
        ));

        return conta;
    }

    @Transactional
    public Conta transferir(TransferenciaDTO transferenciaDTO) {

        long idContaOrigem = transferenciaDTO.getIdContaOrigem();
        String numeroContaDestino = transferenciaDTO.getNumeroContaDestino();
        BigDecimal valor = transferenciaDTO.getValor();


        Conta contaOrigem = this.getContaById(idContaOrigem);

        Conta contaDestino = this.getContaByNumero(numeroContaDestino);

        BigDecimal saldoOrigem = contaOrigem.getSaldo();

        if (saldoOrigem.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para transferência");
        }


        contaOrigem.setSaldo(saldoOrigem.subtract(valor));


        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.TRANSFERENCIA,
                "Transferência para a conta " + numeroContaDestino + " no valor de R$ " + valor,
                valor,
                contaOrigem.getIdConta()
        ));


        BigDecimal saldoDestino = contaDestino.getSaldo();

        contaDestino.setSaldo(saldoDestino.add(valor));

        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.TRANSFERENCIA,
                "Recebimento de transferência da conta " + contaOrigem.getNumero() + " no valor de R$ " + valor ,
                valor,
                contaDestino.getIdConta()
        ));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        return contaOrigem;
    }

    @Transactional
    public Conta realizarRecarga(long id, RecargaDTO recargaDTO) {


        String numeroCelular = recargaDTO.getNumeroCelular();
        BigDecimal valor = recargaDTO.getValor();

        Conta conta = this.getContaById(id);

        BigDecimal saldo = conta.getSaldo();

        if (saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para recarga");
        }

        conta.setSaldo(saldo.subtract(valor));

        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.RECARGA,
                "Recarga de celular para o número " + numeroCelular + " no valor R$ " + valor,
                valor,
                conta.getIdConta()
        ));

        return contaRepository.save(conta);
    }

    @Transactional
    public Conta realizarDeposito(Long id, BigDecimal valor) {

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }

        Conta conta = this.getContaById(id);

        conta.setSaldo(conta.getSaldo().add(valor));

        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.DEPOSITO,
                "Deposito no valor R$ " + valor,
                valor,
                conta.getIdConta()
        ));

        return contaRepository.save(conta);
    }

    @Transactional
    public Conta realizarSaque(Long id, BigDecimal valor) {

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }

        Conta conta = this.getContaById(id);
        BigDecimal saldo = conta.getSaldo();

        if (saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }

        conta.setSaldo(saldo.subtract(valor));

        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.SAQUE,
                "Saque no valor R$ " + valor,
                valor,
                conta.getIdConta()
        ));

        return contaRepository.save(conta);
    }

    @Transactional
    public Conta realizarPagamento(Long id, BigDecimal valor) {

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }

        Conta conta = this.getContaById(id);
        BigDecimal saldo = conta.getSaldo();

        if (saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }

        conta.setSaldo(saldo.subtract(valor));

        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.PAGAMENTO,
                "Pagamento no valor R$ " + valor,
                valor,
                conta.getIdConta()
        ));

        return contaRepository.save(conta);
    }



    private String gerarNumeroContaUnico() {
        String numeroConta;
        do {
            numeroConta = String.format("%08d", new Random().nextInt(100000000));
        } while (contaRepository.existsByNumero(numeroConta));

        return numeroConta;
    }

}