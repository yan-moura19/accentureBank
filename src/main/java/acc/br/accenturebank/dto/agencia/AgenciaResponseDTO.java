package acc.br.accenturebank.dto.agencia;

import acc.br.accenturebank.dto.conta.ContaSimpleDTO;
import acc.br.accenturebank.model.Agencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgenciaResponseDTO {
    private int id;
    private String nome;
    private String endereco;
    private String telefone;
    private List<ContaSimpleDTO> contas;

    public AgenciaResponseDTO(Agencia agencia) {
        this.id = agencia.getId();
        this.nome = agencia.getNome();
        this.endereco = agencia.getEndereco();
        this.telefone = agencia.getTelefone();
        this.contas = agencia.getContas().stream()
                .map(ContaSimpleDTO::new)
                .collect(Collectors.toList());
    }
}
