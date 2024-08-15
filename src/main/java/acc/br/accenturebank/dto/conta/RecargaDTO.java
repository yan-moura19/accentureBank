package acc.br.accenturebank.dto.conta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class RecargaDTO {

    @NotBlank(message = "O Numero do Celular não pode ser nulo.")
    @Pattern(regexp = "^(\\+\\d{2})?\\(\\d{2}\\)\\d{4,5}-\\d{4}$", message = "O numero celular deve ser no formato +55(83)98763-1234 ou (83)91232-2007")
    private String numeroCelular;

    @NotNull(message = "O Valor não pode ser nulo.")
    @Min(value = 0, message = "O Valor deve ser um número inteiro não negativo.")
    private BigDecimal valor;
}
