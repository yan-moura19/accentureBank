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




    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaResponseDTO createConta(@RequestBody CreateContaDTO createContaDTO) {
        Conta conta =  contaService.createConta(createContaDTO);
        return new ContaResponseDTO(conta);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO getContaById(@PathVariable Long id) {
        Conta conta = contaService.getContaById(id);
        return new ContaResponseDTO(conta);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO updateConta(@PathVariable Long id, @RequestBody UpdateContaDTO updateContaDTO) {
        Conta conta = contaService.updateConta(id, updateContaDTO);
        return new ContaResponseDTO(conta);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Conta> getAllContas() {
        return contaService.getAllContas();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteConta(@PathVariable Long id) {
        contaService.deleteConta(id);
    }


    @PostMapping("/{idConta}/separar")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO separarValor(@PathVariable Long idConta, @RequestParam BigDecimal valor) {
        Conta conta = contaService.separarValor(idConta, valor);
        return new ContaResponseDTO(conta);
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




}