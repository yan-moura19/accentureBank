package acc.br.accenturebank.dto;

import lombok.Data;

@Data
public class RecargaCelularRequest {
    private String numeroCelular;
    private float valor;
}
