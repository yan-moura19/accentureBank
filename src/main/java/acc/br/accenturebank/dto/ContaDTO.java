package acc.br.accenturebank.dto;

import lombok.Data;

@Data
public class ContaDTO {
    private String numero;
    private double saldo;
    private boolean ativa;
    private boolean pixAtivo;
    private String chavePix;
    private String tipoConta;
    private String idAgencia;

}
