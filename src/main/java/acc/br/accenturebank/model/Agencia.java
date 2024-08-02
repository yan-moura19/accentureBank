package acc.br.accenturebank.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Agencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAgencia;
    private String nomeAgencia;
    private String endereco;
    private String telefone;

    @OneToMany(mappedBy = "agencia")
    private List<Conta> contas;

    // Getters and Setters
}
