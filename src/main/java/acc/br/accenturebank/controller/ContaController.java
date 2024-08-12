package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.*;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.service.ContaService;
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
    public List<ContaResponseDTO> getAllContas() {
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

    @PostMapping("/{idConta}/resgatar")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO resgatarValor(@PathVariable Long idConta, @RequestParam BigDecimal valor) {
        Conta conta = contaService.resgatarSaldoSeparado(idConta, valor);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("/transferencia")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO transferir(@RequestBody TransferenciaDTO transferenciaDTO) {
        Conta conta = contaService.transferir(transferenciaDTO);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("/{id}/recarga")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO realizarRecarga(@PathVariable Long id, @RequestBody RecargaDTO recargaDTO) {
        Conta conta = contaService.realizarRecarga(id, recargaDTO);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("/{id}/deposito")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO realizarDeposito(@PathVariable Long id, @RequestBody Float valor) {

        BigDecimal valorDecimal = new BigDecimal(valor);
        Conta conta = contaService.realizarDeposito(id, valorDecimal);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("/{id}/saque")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO realizarSaque(@PathVariable Long id, @RequestBody BigDecimal valor) {
        Conta conta = contaService.realizarSaque(id, valor);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("/{id}/pagar")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO realizarPagamento(@PathVariable Long id, @RequestBody BigDecimal valor) {
        Conta conta = contaService.realizarPagamento(id, valor);
        return new ContaResponseDTO(conta);
    }








}