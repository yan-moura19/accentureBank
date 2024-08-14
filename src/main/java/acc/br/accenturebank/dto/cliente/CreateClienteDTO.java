package acc.br.accenturebank.dto.cliente;

import acc.br.accenturebank.validator.ValidBirthDate;
import acc.br.accenturebank.validator.ValidCPF;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "Deve ser um CEP válido no formato 58402-028")
    private String cep;

    @NotBlank(message = "O numero do endereco não pode ser vazio.")
    private String numeroEndereco;

    private String complemento;

    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento deve estar no passado.")
    @ValidBirthDate
    private LocalDate dataNascimento;

    @NotBlank(message = "A senha não pode ser vazia.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "A senha teve contém pelo menos 8 caracteres, sendo pelo menos:" + "1 letra maiúscula" + "1 número" + "1 caractere especial: @, & , $ , % , * , ! , ?")
    private String senha;

    @NotBlank(message = "O telefone não pode ser vazio.")
    @Pattern(regexp = "^(\\+\\d{2})?\\(\\d{2}\\)\\d{4,5}-\\d{4}$", message = "O telefone deve ser no formato +55(83)3341-2007 ou (83)91232-2007")
    private String telefone;

    // Getters and setters
}