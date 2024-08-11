package acc.br.accenturebank.model;

import acc.br.accenturebank.model.enums.Operacao;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import java.time.LocalDateTime;

@Data
@Entity
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTransacao;

    @Column(nullable = false)
    private LocalDateTime dataTransacao;

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


    public Transacao(
            Conta conta,
            LocalDateTime dataTransacao,
            Operacao operacao,
            String descricao,
            BigDecimal valor) {
        this.conta = conta;
        this.dataTransacao = dataTransacao;
        this.operacao = operacao;
        this.descricao = descricao;
        this.valor = valor;
    }
}
