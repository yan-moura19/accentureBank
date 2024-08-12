package acc.br.accenturebank.dto.agencia;

import acc.br.accenturebank.model.Agencia;
import lombok.Data;

@Data
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

