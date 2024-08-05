package acc.br.accenturebank.dto;

import acc.br.accenturebank.model.enums.Operacao;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

//DTO de Criação de Transação
@Data
public class CreateTransacaoDTO {

    @NotNull(message = "A Data da Transação não pode ser nula.")
    private LocalDate dataTransacao;

    @NotNull()
    @Pattern(regexp = "TRANSFERENCIA|SAQUE|DEPOSITO|RECARGA|PAGAMENTO|RECEBIMENTO_TRANSFERENCIA|SEPARACAO|RESGATE",
            message = "A Operação deve ser um dos tipos: TRANSFERENCIA,SAQUE,DEPOSITO,RECARGA,PAGAMENTO,RECEBIMENTO_TRANSFERENCIA,SEPARACAO,RESGATE.")
    private Operacao operacao;

    @NotBlank(message = "Descrição não pode ser vazia.")
    private String descricao;

    @NotNull(message = "Valor não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "Valor da transação deve ser maior que 0.")
    private BigDecimal valor;

    @NotNull(message = "O id da Conta não pode ser nula.")
    private long idConta;
}



