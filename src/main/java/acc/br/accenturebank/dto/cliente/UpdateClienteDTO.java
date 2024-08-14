package acc.br.accenturebank.dto.cliente;

import acc.br.accenturebank.validator.ValidBirthDate;
import acc.br.accenturebank.validator.ValidCPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateClienteDTO {








    @Email(message = "O email deve ser um email válido.")
    private String email;

    private String cep;

    private String numeroEndereco;

    private String complemento;



    @Pattern(regexp = "^(\\+\\d{2})?\\(\\d{2}\\)\\d{4,5}-\\d{4}$",
            message = "O telefone deve ser no formato +55(83)3341-2007 ou (83)98834-2007")
    private String telefone;

    // Getters and setters
}