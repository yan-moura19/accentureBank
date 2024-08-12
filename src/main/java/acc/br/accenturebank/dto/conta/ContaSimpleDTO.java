package acc.br.accenturebank.dto.conta;

import acc.br.accenturebank.dto.agencia.AgenciaSimpleDTO;
import acc.br.accenturebank.dto.cliente.ClienteSimpleDTO;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.enums.TipoConta;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContaSimpleDTO {
    private int id;
    private String numero;
    private BigDecimal saldo;
    private BigDecimal saldoSeparado;
    private boolean ativa;
    private boolean pixAtivo;
    private TipoConta tipoConta;
    private AgenciaSimpleDTO agencia;
    private ClienteSimpleDTO cliente;


    public ContaSimpleDTO(Conta conta) {
        this.id = conta.getId();
        this.numero = conta.getNumero();
        this.saldo = conta.getSaldo();
        this.saldoSeparado = conta.getSaldoSeparado();
        this.ativa = conta.isAtiva();
        this.pixAtivo = conta.isPixAtivo();
        this.tipoConta = conta.getTipoConta();
        this.agencia = new AgenciaSimpleDTO(conta.getAgencia());
        this.cliente = new ClienteSimpleDTO(conta.getCliente());
    }
}
