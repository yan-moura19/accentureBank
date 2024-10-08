package acc.br.accenturebank.dto.pix;

import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.enums.TipoChavePix;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreatePixDTO {

    @NotNull(message = "Tipo da Chave não pode ser nula.")
    @Pattern(regexp = "EMAIL|TELEFONE|CPF|ALEATORIO",
            message = "O Tipo de Chave Pix deve ser um dos seguintes: EMAIL, TELEFONE, CPF.")
    private TipoChavePix tipo;


    private String chave;

    @NotNull(message = "A Conta não pode ser nula.")
    private String IdConta;

    public CreatePixDTO(CreatePixDTO createPixDTO) {

    }
}
