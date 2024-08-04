package acc.br.accenturebank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecargaCelularRequest {
    private String numeroCelular;
    private BigDecimal valor;
}
