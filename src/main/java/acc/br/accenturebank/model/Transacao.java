package acc.br.accenturebank.model;

import acc.br.accenturebank.model.enums.Operacao;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTransacao;
    private LocalDate dataTransacao;
    @Enumerated(EnumType.STRING)
    private Operacao operacao;
    private String descricao;
    private float valor;

    @ManyToOne
    @JoinColumn(name = "idConta")
    @JsonBackReference
    private Conta conta;


}
