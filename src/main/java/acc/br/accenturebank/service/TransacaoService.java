package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.transacao.CreateTransacaoDTO;
import acc.br.accenturebank.dto.transacao.TransacaoResponseDTO;
import acc.br.accenturebank.dto.transacao.TransacaoSimpleDTO;
import acc.br.accenturebank.dto.transacao.UpdateTransacaoDTO;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.model.enums.Operacao;
import acc.br.accenturebank.repository.TransacaoRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;


    public Transacao createTransacao(CreateTransacaoDTO createTransacaoDTO) {

        Conta conta = createTransacaoDTO.getConta();

        if(conta == null){
            throw new ResourceNotFoundException("Conta não foi encontrada.");
        }

        Transacao transacao = new Transacao(
                conta,
                createTransacaoDTO.getDataTransacao(),
                createTransacaoDTO.getOperacao(),
                createTransacaoDTO.getDescricao(),
                createTransacaoDTO.getValor()
        );

        return transacaoRepository.save(transacao);

    }

    public Transacao getTransacaoById(int id) {
        return transacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transacao com id %d não foi encontrada.". formatted(id)));
    }

    public List<TransacaoSimpleDTO> getLast10Transacoes(Conta conta){
        return  transacaoRepository.findTop10ByContaOrderByDataTransacaoDesc(conta).stream()
                .map(TransacaoSimpleDTO::new)
                .collect(Collectors.toList());
    }

    public List<TransacaoSimpleDTO> getTransacoesByPeriodo(Conta conta, LocalDate startDate, LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return transacaoRepository.findByContaAndDataTransacaoBetween(conta, startDateTime, endDateTime).stream()
                .map(TransacaoSimpleDTO::new)
                .collect(Collectors.toList());
    }

    public List<TransacaoResponseDTO> getAllTransacoes() {
        return transacaoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public Transacao updateTransacao(int id, UpdateTransacaoDTO updateTransacaoDTO) {

        Transacao transacao = this.getTransacaoById(id);

        LocalDateTime novaDataTransacao = updateTransacaoDTO.getDataTransacao();
        Operacao novaOperacao = updateTransacaoDTO.getOperacao();
        String novaDescricao = updateTransacaoDTO.getDescricao();
        BigDecimal novoValor = updateTransacaoDTO.getValor();
        Conta novaConta = updateTransacaoDTO.getConta();

        if(novaDataTransacao != null){
            transacao.setDataTransacao(novaDataTransacao);
        }

        if(novaOperacao != null){
            transacao.setOperacao(novaOperacao);
        }

        if(novaDescricao != null){
            transacao.setDescricao(novaDescricao);
        }

        if(novoValor != null){
            transacao.setValor(novoValor);
        }

        if(novaConta != null){
            transacao.setConta(novaConta);
        }


        return transacaoRepository.save(transacao);
    }

    public void deleteTransacao(int id) {

        this.getTransacaoById(id);

        transacaoRepository.deleteById(id);
    }
    private TransacaoResponseDTO converterParaDTO(Transacao transacao){
        return new TransacaoResponseDTO(transacao);
    }
}