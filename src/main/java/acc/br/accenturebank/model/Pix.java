package acc.br.accenturebank.model;

import acc.br.accenturebank.model.enums.TipoChavePix;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "pixs", uniqueConstraints = {
        @UniqueConstraint(columnNames = "chave"),
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoChavePix tipo;

    @Column(unique = true, nullable = false)
    private String chave;

    @ManyToOne
    @JoinColumn(name = "idConta")
    @JsonBackReference
    private Conta conta;

}