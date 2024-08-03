package acc.br.accenturebank.model;

import acc.br.accenturebank.model.enums.TipoConta;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConta;
    private String numero;
    private float saldo;
    private boolean ativa;
    private boolean pixAtivo;
    private String chavePix;
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;
    private float saldoSeparado;

    @ManyToOne
    @JoinColumn(name = "idAgencia")
    private Agencia agencia;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "conta")
    private List<Pix> chavesPix;

    @OneToMany(mappedBy = "conta")
    private List<Transacao> transacoes;


}