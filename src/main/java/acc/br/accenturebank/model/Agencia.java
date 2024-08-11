package acc.br.accenturebank.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "agencias")
public class Agencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAgencia;

    @Column(nullable = false)
    private String nomeAgencia;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String telefone;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "agencia"
    )
    @JsonManagedReference
    private List<Conta> contas;

}
