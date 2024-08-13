package acc.br.accenturebank.dto.conta;

import acc.br.accenturebank.model.enums.TipoConta;
import acc.br.accenturebank.validator.ValidEnum;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateContaDTO {
    @ValidEnum(enumClass = TipoConta.class, message = "Tipo de Conta Inválido.")
    private TipoConta tipoConta;

    @Digits(integer = 10, fraction = 0, message = "O ID da conta deve ser um número inteiro com até 10 digitos")
    @Min(value = 0, message = "O ID da conta deve ser um número inteiro não negativo.")
    private int idAgencia;

    @Digits(integer = 10, fraction = 0, message = "O ID do Cliente deve ser um número inteiro com até 10 digitos")
    @Min(value = 0, message = "O ID do Cliente deve ser um número inteiro não negativo.")
    private int idCliente;

    private BigDecimal saldo;

    private BigDecimal saldoSeparado;

    private boolean ativa;

    private boolean pixAtivo;
}
