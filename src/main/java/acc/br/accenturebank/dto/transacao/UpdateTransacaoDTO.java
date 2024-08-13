package acc.br.accenturebank.dto.transacao;

import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.enums.Operacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpdateTransacaoDTO {

    private LocalDateTime dataTransacao;

    @Pattern(regexp = "TRANSFERENCIA|SAQUE|DEPOSITO|RECARGA|PAGAMENTO|RECEBIMENTO_TRANSFERENCIA|SEPARACAO|RESGATE",
            message = "A Operação deve ser um dos tipos: TRANSFERENCIA,SAQUE,DEPOSITO,RECARGA,PAGAMENTO,RECEBIMENTO_TRANSFERENCIA,SEPARACAO,RESGATE.")
    private Operacao operacao;

    private String descricao;

    @DecimalMin(value = "0.01", message = "Valor da transação deve ser maior que 0.")
    private BigDecimal valor;

    private Conta conta;

}
