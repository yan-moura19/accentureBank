package acc.br.accenturebank.dto;

import acc.br.accenturebank.util.ValidCPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateClienteDTO {

    @NotBlank(message = "O CPF não pode ser vazio.")
    @ValidCPF
    private String cpf;

    @NotBlank(message = "O Nome não pode ser vazio.")
    private String nome;

    @NotBlank(message = "O email não pode ser vazio.")
    @Email(message = "O email deve ser um email válido.")
    private String email;

    @NotBlank(message = "O cep não pode ser vazio.")
    private String cep;

    @NotBlank(message = "O cep não pode ser vazio.")
    private String numeroEndereco;

    @NotBlank(message = "O complemento não pode ser vazio.")
    private String complemento;


    private LocalDate dataNascimento;

    @NotBlank(message = "A senha não pode ser vazia.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "A senha teve contém pelo menos 8 caracteres, sendo pelo menos:" + "1 letra maiúscula" + "1 número" + "1 caractere especial: @, & , $ , % , * , ! , ?")
    private String senha;

    @NotBlank(message = "O telefone não pode ser vazio.")
    @Pattern(regexp = "^(\\+\\d{2})?\\(\\d{2}\\)\\d{4,5}-\\d{4}$", message = "O telefone deve ser no formato +55(83)3341-2007 ou (83)91232-2007")
    private String telefone;

    // Getters and setters
}