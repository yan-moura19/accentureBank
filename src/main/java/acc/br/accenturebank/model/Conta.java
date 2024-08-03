package acc.br.accenturebank.model;

import acc.br.accenturebank.model.enums.TipoConta;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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