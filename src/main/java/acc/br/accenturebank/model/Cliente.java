package acc.br.accenturebank.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCliente;
    private String cpf;
    private String nome;
    private String email;
    private String senha;
    private String contato;

    @JsonManagedReference
    @OneToMany(mappedBy = "cliente")
    private List<Conta> contas = new ArrayList<>();



}
