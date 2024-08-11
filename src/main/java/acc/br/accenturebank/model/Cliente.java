package acc.br.accenturebank.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "clientes", uniqueConstraints = {
        @UniqueConstraint(columnNames = "cpf"),
        @UniqueConstraint(columnNames = "email")
})
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCliente;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String numeroEndereco;

    @Column(nullable = false)
    private String complemento;


    private LocalDate dataNascimento;

    @JsonManagedReference
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "cliente"
    )
    private List<Conta> contas = new ArrayList<>();



}
