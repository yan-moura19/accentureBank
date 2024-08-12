package acc.br.accenturebank.dto.cliente;

import acc.br.accenturebank.validator.ValidBirthDate;
import acc.br.accenturebank.validator.ValidCPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateClienteDTO {


    @ValidCPF
    private String cpf;


    private String nome;


    @Email(message = "O email deve ser um email válido.")
    private String email;

    private String cep;

    private String numeroEndereco;

    private String complemento;

    @Past(message = "A data de nascimento deve estar no passado.")
    @ValidBirthDate
    private LocalDate dataNascimento;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "A senha teve contém pelo menos 8 caracteres, sendo pelo menos:" +
                    "1 letra maiúscula" +
                    "1 número" +
                    "1 caractere especial: @, & , $ , % , * , ! , ?")
    private String senha;

    @Pattern(regexp = "^(\\+\\d{2})?\\(\\d{2}\\)\\d{4,5}-\\d{4}$",
            message = "O telefone deve ser no formato +55(83)3341-2007 ou (83)98834-2007")
    private String telefone;

    // Getters and setters
}