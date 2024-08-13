package acc.br.accenturebank.dto.conta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PixDTO {

    @NotBlank(message = "A chave não pode ser nula.")
    private String chave;

    @NotNull(message = "O Valor não pode ser nulo.")
    @Min(value = 0, message = "O Valor deve ser um número inteiro não negativo.")
    private BigDecimal valor;
}
