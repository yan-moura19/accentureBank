package acc.br.accenturebank.dto;

import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.enums.TipoConta;
import lombok.Data;

@Data
public class ContaResponseDTO {
    private int idConta;
    private String numero;
    private float saldo;
    private boolean ativa;
    private boolean pixAtivo;
    private String chavePix;
    private TipoConta tipoConta;
    private Agencia agencia;

}