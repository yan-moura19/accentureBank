package acc.br.accenturebank.dto;

import lombok.Data;

@Data
public class TransferenciaRequest {
    private Long idContaOrigem;
    private String numeroContaDestino;
    private float valor;


}
