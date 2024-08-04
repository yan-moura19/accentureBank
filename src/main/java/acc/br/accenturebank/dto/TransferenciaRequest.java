package acc.br.accenturebank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferenciaRequest {
    private Long idContaOrigem;
    private String numeroContaDestino;
    private BigDecimal valor;


}
