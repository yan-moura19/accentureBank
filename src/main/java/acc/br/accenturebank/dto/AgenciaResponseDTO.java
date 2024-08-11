package acc.br.accenturebank.dto;

import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Conta;
import lombok.Data;

import java.util.List;

@Data
public class AgenciaResponseDTO {
    private int id;
    private String nome;
    private String endereco;
    private String telefone;
    private List<Conta> contas;

    public AgenciaResponseDTO(Agencia agencia) {
        this.id = agencia.getId();
        this.nome = agencia.getNome();
        this.endereco = agencia.getEndereco();
        this.telefone = agencia.getTelefone();
        this.contas = agencia.getContas();
    }
}
