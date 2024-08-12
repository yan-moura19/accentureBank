package acc.br.accenturebank.dto.pix;

import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.enums.TipoChavePix;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdatePixDTO {

    @Pattern(regexp = "EMAIL|TELEFONE|CPF",
            message = "O Tipo de Chave Pix deve ser um dos seguintes: EMAIL, TELEFONE, CPF.")
    private TipoChavePix tipo;

    private String chave;

    private Conta conta;
}
