package acc.br.accenturebank.dto.agencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO de Criação de Transação
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAgenciaDTO {

    @NotBlank(message = "O Nome da Agência não pode ser vazia.")
    private String nomeAgencia;

    @NotBlank(message = "O endereço  não pode ser vazio.")
    private String endereco;

    @NotBlank(message = "O telefone não pode ser vazio .")
    @Pattern(regexp = "^(\\+\\d{2})?\\(\\d{2}\\)\\d{4,5}-\\d{4}$",
            message = "O telefone deve ser no formato +55(83)3341-2007 ou (83)91232-2007")
    private String telefone;
}



