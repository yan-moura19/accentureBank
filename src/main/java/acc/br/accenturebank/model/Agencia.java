package acc.br.accenturebank.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "agencias")
@AllArgsConstructor
@NoArgsConstructor
public class Agencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nome;

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
