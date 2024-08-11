package acc.br.accenturebank.service;

import acc.br.accenturebank.exception.SaldoInsuficienteException;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.model.enums.Operacao;
import acc.br.accenturebank.repository.ContaRepository;
import acc.br.accenturebank.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Conta separarValor(long idConta, BigDecimal valor) {
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        conta.setSaldoSeparado(conta.getSaldoSeparado().add(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setDataTransacao(LocalDateTime.now());
        transacao.setOperacao(Operacao.SEPARACAO);
        transacao.setDescricao("Separação de valor");
        transacao.setValor(valor);
        transacaoRepository.save(transacao);

        return conta;
    }

    public Conta resgatarValor(Long idConta, BigDecimal valor) {
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        if (conta.getSaldoSeparado().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo separado insuficiente");
        }

        conta.setSaldo(conta.getSaldo().add(valor));
        conta.setSaldoSeparado(conta.getSaldoSeparado().subtract(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setDataTransacao(LocalDateTime.now());
        transacao.setOperacao(Operacao.RESGATE);
        transacao.setDescricao("Resgate de valor separado");
        transacao.setValor(valor);
        transacaoRepository.save(transacao);

        return conta;
    }


    @Transactional
    public void transferir(Long idContaOrigem, String numeroContaDestino, BigDecimal valor) {
        Conta contaOrigem = contaRepository.findById(idContaOrigem)
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

    public List<Conta> getAllContas() {
        return contaRepository.findAll();
    }

    public Conta getContaById(Long id) {
        return contaRepository.findById(id).orElse(null);
    }

    public Conta createConta(Conta conta) {

        String numeroConta = gerarNumeroContaUnico();
        conta.setNumero(numeroConta);
        return contaRepository.save(conta);
    }

    public Conta updateConta(int id, Conta conta) {
        conta.setIdConta(id);
        return contaRepository.save(conta);
    }

    public void deleteConta(Long id) {
        contaRepository.deleteById(id);
    }

    private String gerarNumeroContaUnico() {
        String numeroConta;
        do {
            numeroConta = String.format("%08d", new Random().nextInt(100000000));
        } while (contaRepository.existsByNumero(numeroConta));

        return numeroConta;
    }

}