package acc.br.accenturebank.model;

import acc.br.accenturebank.model.enums.TipoChavePix;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Pix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private TipoChavePix tipo;
    private String chave;

    @ManyToOne
    @JoinColumn(name = "idConta")
    private Conta conta;

    // Getters and Setters
}