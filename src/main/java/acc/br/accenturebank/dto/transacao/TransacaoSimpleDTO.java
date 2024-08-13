package acc.br.accenturebank.dto.transacao;

import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.model.enums.Operacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TransacaoSimpleDTO {
    private int id;
    private LocalDateTime dataTransacao;
    private Operacao operacao;
    private String descricao;
    private BigDecimal valor;

    public TransacaoSimpleDTO(Transacao transacao) {
        this.id = transacao.getId();
        this.dataTransacao = transacao.getDataTransacao();
        this.operacao = transacao.getOperacao();
        this.descricao = transacao.getDescricao();
        this.valor = transacao.getValor();
    }
}
