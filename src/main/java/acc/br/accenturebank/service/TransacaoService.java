package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.CreateTransacaoDTO;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.model.enums.Operacao;
import acc.br.accenturebank.repository.TransacaoRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Transacao createTransacao(CreateTransacaoDTO createTransacaoDTO) {

        try{


            Conta conta = createTransacaoDTO.getConta();

            if(conta == null){
                throw new ResourceNotFoundException("Conta com id não foi encontrada.");
            }

            Transacao transacao = new Transacao(
                    conta,
                    createTransacaoDTO.getDataTransacao(),
                    createTransacaoDTO.getOperacao(),
                    createTransacaoDTO.getDescricao(),
                    createTransacaoDTO.getValor()
            );

            return transacaoRepository.save(transacao);
        }catch(Exception e){
            throw new RuntimeException("Falha ao criar a Transação: ".concat(e.getMessage()), e);
        }
    }

    public Transacao updateTransacao(int id, Transacao transacao) {
        transacao.setId(id);
        return transacaoRepository.save(transacao);
    }

    public void deleteTransacao(int id) {
        transacaoRepository.deleteById(id);
    }
}