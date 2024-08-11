package acc.br.accenturebank.model;

import acc.br.accenturebank.model.enums.TipoConta;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "contas", uniqueConstraints = {@UniqueConstraint(columnNames = "numero"),})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String numero;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativa;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean pixAtivo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @Column(nullable = false)
    private BigDecimal saldoSeparado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAgencia")
    @JsonBackReference
    private Agencia agencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente")
    @JsonBackReference
    private Cliente cliente;

    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Pix> chavesPix;

    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Transacao> transacoes;
}