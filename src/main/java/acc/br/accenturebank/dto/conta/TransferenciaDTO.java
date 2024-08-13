package acc.br.accenturebank.dto.conta;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferenciaDTO {

    @NotNull(message = "O Id da Conta de Origem não pode ser vazia.")
    @Digits(integer = 10, fraction = 0, message = "O ID da conta deve ser um número inteiro com até 10 digitos")
    @Min(value = 0, message = "O ID da conta deve ser um número inteiro não negativo.")
    private long idContaOrigem;


    @NotBlank(message = "O Numero da Conta de Destino não pode ser vazio.")
    @Pattern(regexp = "^\\d{8}$")
    private String numeroContaDestino;

    @NotNull(message = "O Valor não pode ser nulo.")
    @Min(value = 0, message = "O Valor deve ser um número inteiro não negativo.")
    private BigDecimal valor;

}
