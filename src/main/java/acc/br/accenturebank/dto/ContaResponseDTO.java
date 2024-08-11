package acc.br.accenturebank.dto;

import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.model.enums.TipoConta;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContaResponseDTO {
    private int id;
    private String numero;
    private BigDecimal saldo;
    private BigDecimal saldoSeparado;
    private boolean ativa;
    private boolean pixAtivo;
    private TipoConta tipoConta;
    private Agencia agencia;
    private Cliente cliente;
    private Pix[] chavesPix;

    public ContaResponseDTO(Conta conta) {
        this.id = conta.getIdConta();
        this.numero = conta.getNumero();
        this.saldo = conta.getSaldo();
        this.saldoSeparado = conta.getSaldoSeparado();
        this.ativa = conta.isAtiva();
        this.pixAtivo = conta.isPixAtivo();
        this.tipoConta = conta.getTipoConta();
        this.agencia = conta.getAgencia();
        this.cliente = conta.getCliente();
        this.chavesPix = (Pix[]) conta.getChavesPix().toArray();
    }
}
