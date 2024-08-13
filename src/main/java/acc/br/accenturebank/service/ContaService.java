package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.conta.ValorDTO;
import acc.br.accenturebank.dto.conta.*;
import acc.br.accenturebank.dto.transacao.CreateTransacaoDTO;
import acc.br.accenturebank.dto.transacao.TransacaoSimpleDTO;
import acc.br.accenturebank.exception.PeriodoInvalidoException;
import acc.br.accenturebank.exception.SaldoInsuficienteException;
import acc.br.accenturebank.model.*;
import acc.br.accenturebank.model.enums.Operacao;
import acc.br.accenturebank.model.enums.TipoConta;
import acc.br.accenturebank.repository.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private PixService pixService;




    public Conta createConta(CreateContaDTO createContaDTO) {


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

        List<Pix> chaves = new ArrayList<>();
        List<Transacao> transacoes = new ArrayList<>();

        Conta conta = Conta.builder()
                .tipoConta(createContaDTO.getTipoConta())
                .agencia(agencia)
                .cliente(cliente)
                .numero(numero)
                .saldo(BigDecimal.ZERO)
                .saldoSeparado(BigDecimal.ZERO)
                .chavesPix(chaves)
                .transacoes(transacoes)
                .build();

        return contaRepository.save(conta);

    }

    public Conta getContaById(long id) {
        return contaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Conta com id %d não foi encontrada.".formatted(id)));
    }

    public List<ContaResponseDTO> getAllContas() {
        return contaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public Conta updateConta(long id, UpdateContaDTO updateContaDTO) {

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
           conta.setSaldoSeparado(novoSaldoSeparado);
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
    }

    public void deleteConta(long id) {

        this.getContaById(id);

        contaRepository.deleteById(id);

    }

    private Conta getContaByNumero(String numero) {
        return contaRepository.findByNumero(numero)
                .orElseThrow(() -> new EntityNotFoundException("Conta de numero %s não foi encontrada.".formatted(numero)));
    }

    @Transactional
    public Conta separarValor(long id, ValorDTO valorDTO) {

        BigDecimal valor = valorDTO.getValor();

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
                conta
        ));

        return conta;
    }

    @Transactional
    public Conta resgatarSaldoSeparado(long id, ValorDTO valorDTO) {

        BigDecimal valor = valorDTO.getValor();

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
                conta
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
                contaOrigem
        ));


        BigDecimal saldoDestino = contaDestino.getSaldo();

        contaDestino.setSaldo(saldoDestino.add(valor));

        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.RECEBIMENTO_TRANSFERENCIA,
                "Recebimento de transferência da conta " + contaOrigem.getNumero() + " no valor de R$ " + valor ,
                valor,
                contaDestino
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
                conta
        ));

        return contaRepository.save(conta);
    }

    @Transactional
    public Conta realizarDeposito(long id, ValorDTO valorDTO) {

        BigDecimal valor = valorDTO.getValor();

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
                conta
        ));

        return contaRepository.save(conta);
    }

    @Transactional
    public Conta realizarSaque(long id, ValorDTO valorDTO) {

        BigDecimal valor = valorDTO.getValor();

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
                conta
        ));

        return contaRepository.save(conta);
    }

    @Transactional
    public Conta realizarPagamento(long id, ValorDTO valorDTO) {

        BigDecimal valor = valorDTO.getValor();

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
                conta
        ));

        return contaRepository.save(conta);
    }

    public List<TransacaoSimpleDTO> getExtrato(long id){

        Conta conta = this.getContaById(id);

        return transacaoService.getLast10Transacoes(conta);

    }

    public List<TransacaoSimpleDTO> getExtratoPeriodo(long id, ExtratoPeriodoDTO extratoPeriodoDTO){
        LocalDate startDate = extratoPeriodoDTO.getStartDate();

        LocalDate endDate = extratoPeriodoDTO.getEndDate();

        LocalDate now = LocalDate.now();
        if (!startDate.isBefore(now) || !startDate.isEqual(now)){
            throw new PeriodoInvalidoException("startDate deve ser antes ou igual a data de hoje");
        }

        if (!endDate.isBefore(now) || !endDate.isEqual(now)){
            throw new PeriodoInvalidoException("endDate deve ser antes ou igual a data de hoje");
        }

        if(!startDate.isBefore(endDate)){
            throw new PeriodoInvalidoException("startDate deve ser antes de endDate");
        }

        Conta conta = this.getContaById(id);

        return transacaoService.getTransacoesByPeriodo(conta, startDate, endDate);


    }


    public Conta ativarPix(long id){

        Conta conta = this.getContaById(id);

        conta.setPixAtivo(true);

        return contaRepository.save(conta);
    }

    public Conta desativarPix(long id){

        Conta conta = this.getContaById(id);

        conta.setPixAtivo(false);

        return contaRepository.save(conta);
    }


    public Conta ativarConta(long id){

        Conta conta = this.getContaById(id);

        conta.setAtiva(true);

        return contaRepository.save(conta);
    }

    public Conta desativarConta(long id){

        Conta conta = this.getContaById(id);

        conta.setAtiva(false);

        return contaRepository.save(conta);
    }

    public Conta realizarPix(long id, PixDTO pixDTO){

        Conta contaOrigem = this.getContaById(id);
        String chave = pixDTO.getChave();
        BigDecimal valor = pixDTO.getValor();
        Pix pix = pixService.getPixByChave(chave);
        Conta contaDestino = pix.getConta();

        BigDecimal saldoOrigem = contaOrigem.getSaldo();

        if (saldoOrigem.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para transferência");
        }


        contaOrigem.setSaldo(saldoOrigem.subtract(valor));


        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.PIX,
                "PIX para a conta " + contaDestino.getNumero() + " no valor de R$ " + valor,
                valor,
                contaOrigem
        ));


        BigDecimal saldoDestino = contaDestino.getSaldo();

        contaDestino.setSaldo(saldoDestino.add(valor));

        transacaoService.createTransacao(new CreateTransacaoDTO(
                LocalDateTime.now(),
                Operacao.RECEBIMENTO_PIX,
                "PIX de transferência da conta " + contaOrigem.getNumero() + " no valor de R$ " + valor ,
                valor,
                contaDestino
        ));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        return contaOrigem;


    }




    private String gerarNumeroContaUnico() {
        String numeroConta;
        do {
            numeroConta = String.format("%08d", new Random().nextInt(100000000));
        } while (contaRepository.existsByNumero(numeroConta));

        return numeroConta;
    }

    private ContaResponseDTO converterParaDTO(Conta conta){
        return new ContaResponseDTO(conta);
    }

}