package acc.br.accenturebank.service;

import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<Transacao> getAllTransacoes() {
        return transacaoRepository.findAll();
    }

    public Transacao getTransacaoById(int id) {
        return transacaoRepository.findById(id).orElse(null);
    }

    public Transacao createTransacao(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    public Transacao updateTransacao(int id, Transacao transacao) {
        transacao.setIdTransacao(id);
        return transacaoRepository.save(transacao);
    }

    public void deleteTransacao(int id) {
        transacaoRepository.deleteById(id);
    }
}