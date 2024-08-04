package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.CreateTransacaoDTO;
import acc.br.accenturebank.exception.ContaNaoExisteException;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaService contaService;

    public List<Transacao> getAllTransacoes() {
        return transacaoRepository.findAll();
    }

    public Transacao getTransacaoById(int id) {
        return transacaoRepository.findById(id).orElse(null);
    }

    public Transacao createTransacao(CreateTransacaoDTO createTransacaoDTO) throws Exception  {

        long idConta = createTransacaoDTO.getIdConta();
        Conta conta = contaService.getContaById(idConta);

        if (conta == null){
            throw new ContaNaoExisteException(
                    "Conta com ID %d não foi encontrada".formatted(idConta)
            );
        }

        Transacao transacao = new Transacao();
        transacao.setDataTransacao(createTransacaoDTO.getDataTransacao());
        transacao.setOperacao(createTransacaoDTO.getOperacao());
        transacao.setDescricao(createTransacaoDTO.getDescricao());
        transacao.setValor(createTransacaoDTO.getValor());
        transacao.setConta(conta);

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