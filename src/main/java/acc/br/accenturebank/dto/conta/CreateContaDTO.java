package acc.br.accenturebank.dto.conta;

import acc.br.accenturebank.model.enums.TipoConta;
import acc.br.accenturebank.validator.ValidEnum;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateContaDTO {
    @NotNull(message = "O Tipo da Conta não pode ser nula.")
    @ValidEnum(enumClass = TipoConta.class, message = "Tipo de Conta Inválido.")
    private TipoConta tipoConta;

    @NotNull(message = "A Agência não pode ser nula.")
    @Digits(integer = 10, fraction = 0, message = "O ID da Agência deve ser um número inteiro com até 10 digitos")
    @Min(value = 0, message = "O ID da Agência deve ser um número inteiro não negativo.")
    private int idAgencia;

    @NotNull(message = "O Cliente não pode ser nulo.")
    @Digits(integer = 10, fraction = 0, message = "O ID do Cliente deve ser um número inteiro com até 10 digitos")
    @Min(value = 0, message = "O ID do Cliente deve ser um número inteiro não negativo.")
    private int idCliente;

    // Getters and setters
}
