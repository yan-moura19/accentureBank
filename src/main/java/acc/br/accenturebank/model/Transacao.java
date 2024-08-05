package acc.br.accenturebank.model;

import acc.br.accenturebank.model.enums.Operacao;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTransacao;

    @Column(nullable = false)
    private LocalDate dataTransacao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Operacao operacao;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "idConta")
    @JsonBackReference
    private Conta conta;


}
