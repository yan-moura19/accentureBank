package acc.br.accenturebank.dto;

import acc.br.accenturebank.model.enums.Operacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

//DTO de Criação de Transação
@Data
public class CreateAgenciaDTO {

    @NotBlank(message = "O Nome da Agência não pode ser vazia.")
    private String nomeAgencia;

    @NotBlank(message = "O endereço  não pode ser vazio.")
    private String endereco;

    @NotBlank(message = "O telefone não pode ser vazio .")
    @Pattern(regexp = "^(\\d{2})?\\(\\d{2}\\)\\d{4,5}-\\d{4}$",
            message = "O telefone deve ser no formato +55(83)3341-2007 ou (83)3341-2007")
    private String telefone;
}



