package acc.br.accenturebank.dto.agencia;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO de Criação de Transação
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAgenciaDTO {

    private String nomeAgencia;

    private String endereco;

    @Pattern(regexp = "^(\\+\\d{2})?\\(\\d{2}\\)\\d{4,5}-\\d{4}$",
            message = "O telefone deve ser no formato +55(83)3341-2007 ou (83)91232-2007")
    private String telefone;

}



