package acc.br.accenturebank.dto.transacao;

import acc.br.accenturebank.dto.conta.ContaSimpleDTO;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.model.enums.Operacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransacaoResponseDTO {
    private int id;
    private LocalDateTime dataTransacao;
    private Operacao operacao;
    private String descricao;
    private BigDecimal valor;
    private ContaSimpleDTO conta;

    public TransacaoResponseDTO(Transacao transacao) {
        this.id = transacao.getId();
        this.dataTransacao = transacao.getDataTransacao();
        this.operacao = transacao.getOperacao();
        this.descricao = transacao.getDescricao();
        this.valor = transacao.getValor();
        this.conta = new ContaSimpleDTO(transacao.getConta());
    }
}
