package acc.br.accenturebank.dto.conta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorDTO {
    @NotNull(message = "O Valor não pode ser nulo.")
    @Min(value = 0, message = "O Valor deve ser um número inteiro não negativo.")
    private BigDecimal valor;
}
