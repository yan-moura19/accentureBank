package acc.br.accenturebank.model;
import jakarta.persistence.*;
import lombok.Data;


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

    @OneToMany(mappedBy = "cliente")
    private List<Conta> contas;



}
