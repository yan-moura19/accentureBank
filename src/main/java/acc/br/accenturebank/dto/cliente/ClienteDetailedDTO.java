package acc.br.accenturebank.dto.cliente;

import acc.br.accenturebank.dto.conta.ContaResponseDTO;
import acc.br.accenturebank.dto.conta.ContaSimpleDTO;
import acc.br.accenturebank.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDetailedDTO {
    private int id;
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private String cep;
    private String numeroEndereco;
    private String complemento;
    private LocalDate dataNascimento;
    private List<ContaResponseDTO> contas;

    public ClienteDetailedDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.cpf = cliente.getCpf();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.telefone = cliente.getTelefone();
        this.cep = cliente.getCep();
        this.numeroEndereco = cliente.getNumeroEndereco();
        this.complemento = cliente.getComplemento();
        this.dataNascimento = cliente.getDataNascimento();
        this.contas = cliente.getContas().stream()
                .map(ContaResponseDTO::new)
                .collect(Collectors.toList());
    }
}
