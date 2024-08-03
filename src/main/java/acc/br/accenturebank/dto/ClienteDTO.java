package acc.br.accenturebank.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private String cpf;
    private String nome;
    private String email;
    private String senha;
    private String contato;

    // Getters and setters
}