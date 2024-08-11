package acc.br.accenturebank.dto;

import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ClienteSimpleDTO {
    private int id;
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private String cep;
    private String numeroEndereco;
    private String complemento;
    private LocalDate dataNascimento;

    public ClienteSimpleDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.cpf = cliente.getCpf();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.telefone = cliente.getTelefone();
        this.cep = cliente.getCep();
        this.numeroEndereco = cliente.getNumeroEndereco();
        this.complemento = cliente.getComplemento();
        this.dataNascimento = cliente.getDataNascimento();
    }
}
