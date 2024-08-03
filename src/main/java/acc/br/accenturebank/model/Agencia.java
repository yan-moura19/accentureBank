package acc.br.accenturebank.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private List<Conta> contas;

}
