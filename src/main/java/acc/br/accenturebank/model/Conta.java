package acc.br.accenturebank.model;

import acc.br.accenturebank.model.enums.TipoConta;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "contas", uniqueConstraints = {@UniqueConstraint(columnNames = "numero"),})
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConta;

    @Column(unique = true, nullable = false)
    private String numero;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativa = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean pixAtivo = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @Column(nullable = false)
    private BigDecimal saldoSeparado;

    @ManyToOne
    @JoinColumn(name = "idAgencia")
    @JsonBackReference
    private Agencia agencia;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    @JsonBackReference
    private Cliente cliente;

    @OneToMany(mappedBy = "conta")
    @JsonManagedReference
    private List<Pix> chavesPix;

    @OneToMany(mappedBy = "conta")
    @JsonManagedReference
    private List<Transacao> transacoes;


}