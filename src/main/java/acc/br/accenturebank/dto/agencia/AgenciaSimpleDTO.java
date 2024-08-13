package acc.br.accenturebank.dto.agencia;

import acc.br.accenturebank.model.Agencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgenciaSimpleDTO {
    private int id;
    private String nome;
    private String endereco;
    private String telefone;
   

    public AgenciaSimpleDTO(Agencia agencia) {
        this.id = agencia.getId();
        this.nome = agencia.getNome();
        this.endereco = agencia.getEndereco();
        this.telefone = agencia.getTelefone();
    }
}

