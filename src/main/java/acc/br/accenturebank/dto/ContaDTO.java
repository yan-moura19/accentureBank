package acc.br.accenturebank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContaDTO {
    private String numero;
    private BigDecimal saldo;
    private boolean ativa;
    private boolean pixAtivo;
    private String chavePix;
    private String tipoConta;
    private String idAgencia;

}
