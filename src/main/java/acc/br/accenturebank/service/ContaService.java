package acc.br.accenturebank.service;

import acc.br.accenturebank.exception.SaldoInsuficienteException;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.model.enums.Operacao;
import acc.br.accenturebank.repository.ContaRepository;
import acc.br.accenturebank.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;
    public Conta realizarRecarga(Long idConta, String numeroCelular, float valor) {
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));


        conta.setSaldo(conta.getSaldo() - valor);


        Transacao transacao = new Transacao();
        transacao.setDataTransacao(LocalDate.now());
        transacao.setOperacao(Operacao.RECARGA);
        transacao.setDescricao("Recarga de celular para o número " + numeroCelular);
        transacao.setValor(valor);
        transacao.setConta(conta);


        transacaoRepository.save(transacao);


        return contaRepository.save(conta);
    }

    public Conta realizarSaque(Long idConta, float valor){
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        if (conta.getSaldo() < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }
        conta.setSaldo(conta.getSaldo() - valor);
        Transacao transacao = new Transacao();
        transacao.setDataTransacao(LocalDate.now());
        transacao.setOperacao(Operacao.SAQUE);
        transacao.setDescricao("S/ D");
        transacao.setValor(valor);
        transacao.setConta(conta);
        transacaoRepository.save(transacao);


        return contaRepository.save(conta);
    }
    public Conta realizarPagamento(Long idConta, float valor){
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        if (conta.getSaldo() < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }
        conta.setSaldo(conta.getSaldo() - valor);
        Transacao transacao = new Transacao();
        transacao.setDataTransacao(LocalDate.now());
        transacao.setOperacao(Operacao.PAGAMENTO);
        transacao.setDescricao("Pagamento feito via app");
        transacao.setValor(valor);
        transacao.setConta(conta);
        transacaoRepository.save(transacao);


        return contaRepository.save(conta);
    }

    public Conta realizarDeposito(Long idConta, float valor) {

        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));


        conta.setSaldo(conta.getSaldo() + valor);


        Transacao transacao = new Transacao();
        transacao.setDataTransacao(LocalDate.now());
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
        return contaRepository.save(conta);
    }

    public Conta updateConta(int id, Conta conta) {
        conta.setIdConta(id);
        return contaRepository.save(conta);
    }

    public void deleteConta(Long id) {
        contaRepository.deleteById(id);
    }
}