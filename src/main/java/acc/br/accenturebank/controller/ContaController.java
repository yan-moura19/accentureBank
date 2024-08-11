package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.*;
import acc.br.accenturebank.exception.SaldoInsuficienteException;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;

import acc.br.accenturebank.model.enums.TipoConta;
import acc.br.accenturebank.service.AgenciaService;
import acc.br.accenturebank.service.ClienteService;
import acc.br.accenturebank.service.ContaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private AgenciaService agenciaService;

    @GetMapping
    public List<Conta> getAllContas() {
        return contaService.getAllContas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaDetalhesDTO> getContaById(@PathVariable Long id) {
        Conta conta = contaService.getContaById(id);
        ContaDetalhesDTO dto = new ContaDetalhesDTO();
        dto.setIdConta(conta.getIdConta());
        dto.setNumero(conta.getNumero());
        dto.setSaldo(conta.getSaldo());
        dto.setAtiva(conta.isAtiva());
        dto.setPixAtivo(conta.isPixAtivo());
        dto.setTipoConta(conta.getTipoConta());
        dto.setNomeCliente(conta.getCliente().getNome());
        dto.setNomeAgencia(conta.getAgencia().getNomeAgencia());
        dto.setEnderecoAgencia(conta.getAgencia().getEndereco());
        dto.setTelefoneAgencia(conta.getAgencia().getTelefone());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<Conta> realizarDeposito(@PathVariable Long id, @RequestBody BigDecimal valor) {
        Conta contaAtualizada = contaService.realizarDeposito(id, valor);
        return ResponseEntity.ok(contaAtualizada);
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<Conta> realizarSaque(@PathVariable Long id, @RequestBody BigDecimal valor) {
        Conta contaAtualizada = contaService.realizarSaque(id, valor);
        return ResponseEntity.ok(contaAtualizada);
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<Conta> realizarPagamento(@PathVariable Long id, @RequestBody BigDecimal valor) {
        Conta contaAtualizada = contaService.realizarPagamento(id, valor);
        return ResponseEntity.ok(contaAtualizada);
    }

    @PostMapping("/{idConta}/separar")
    public ResponseEntity<Conta> separarValor(@PathVariable Long idConta, @RequestParam BigDecimal valor) {
        Conta conta = contaService.separarValor(idConta, valor);
        return ResponseEntity.ok(conta);
    }

    @PostMapping("/{idConta}/resgatar")
    public ResponseEntity<Conta> resgatarValor(@PathVariable Long idConta, @RequestParam BigDecimal valor) {
        Conta conta = contaService.resgatarValor(idConta, valor);
        return ResponseEntity.ok(conta);
    }

    @PostMapping("/{id}/recarga")
    public ResponseEntity<Conta> realizarRecarga(@PathVariable Long id, @RequestBody RecargaCelularRequest recargaRequest) {
        Conta contaAtualizada = contaService.realizarRecarga(id, recargaRequest.getNumeroCelular(), recargaRequest.getValor());
        return ResponseEntity.ok(contaAtualizada);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Void> transferir(@RequestBody TransferenciaRequest transferenciaRequest) {
        try {
            contaService.transferir(transferenciaRequest.getIdContaOrigem(), transferenciaRequest.getNumeroContaDestino(), transferenciaRequest.getValor());
            return ResponseEntity.ok().build();
        } catch (SaldoInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> createConta(@RequestBody ContaDTO contaDTO, int idCliente) {
        Cliente cliente = clienteService.getClienteById(idCliente);

        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Agencia agencia = agenciaService.getAgenciaById(Integer.parseInt(contaDTO.getIdAgencia()));
        if (agencia == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Conta conta = new Conta();

        conta.setSaldo(BigDecimal.ZERO);
        conta.setAtiva(true);
        conta.setPixAtivo(true);
        conta.setSaldoSeparado(BigDecimal.ZERO);

        conta.setTipoConta(TipoConta.valueOf(contaDTO.getTipoConta()));
        conta.setCliente(cliente);
        conta.setAgencia(agencia);

        Conta savedConta = contaService.createConta(conta);

        ContaResponseDTO responseDTO = new ContaResponseDTO();
        responseDTO.setIdConta(savedConta.getIdConta());
        responseDTO.setNumero(savedConta.getNumero());
        responseDTO.setSaldo(savedConta.getSaldo());
        responseDTO.setAtiva(savedConta.isAtiva());
        responseDTO.setPixAtivo(savedConta.isPixAtivo());

        responseDTO.setTipoConta(savedConta.getTipoConta());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public Conta updateConta(@PathVariable int id, @RequestBody ContaDTO contaDTO) {
        Conta conta = contaService.getContaById((long) id);
        conta.setAtiva(true);
        conta.setPixAtivo(true);
        conta.setTipoConta(TipoConta.valueOf(contaDTO.getTipoConta()));

        return contaService.updateConta(id, conta);
    }

    @DeleteMapping("/{id}")
    public void deleteConta(@PathVariable Long id) {
        contaService.deleteConta(id);
    }
}