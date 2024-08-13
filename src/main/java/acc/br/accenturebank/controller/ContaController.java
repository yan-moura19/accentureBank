package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.conta.ValorDTO;
import acc.br.accenturebank.dto.conta.*;
import acc.br.accenturebank.dto.pix.CreatePixDTO;
import acc.br.accenturebank.dto.transacao.TransacaoSimpleDTO;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public ContaResponseDTO updateConta(@PathVariable long id, @RequestBody UpdateContaDTO updateContaDTO) {
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
    public void deleteConta(@PathVariable long id) {
        contaService.deleteConta(id);
    }

    @PostMapping("/{id}/chavepix")
    @ResponseStatus(HttpStatus.OK)
    public Pix criaChavePix(@PathVariable long id, @RequestBody CreatePixDTO  createPixDTO) {
        Pix pix = contaService.addPixToConta(createPixDTO);
        return pix;
    }

    @PostMapping("/{id}/separar")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO separarValor(@PathVariable long id, @RequestBody ValorDTO valorDTO) {
        Conta conta = contaService.separarValor(id, valorDTO);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("/{id}/resgatar")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO resgatarValor(@PathVariable long id, @RequestBody ValorDTO valorDTO) {
        Conta conta = contaService.resgatarSaldoSeparado(id, valorDTO);
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
    public ContaResponseDTO realizarRecarga(@PathVariable long id, @RequestBody RecargaDTO recargaDTO) {
        Conta conta = contaService.realizarRecarga(id, recargaDTO);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("/{id}/deposito")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO realizarDeposito(@PathVariable long id, @RequestBody ValorDTO valorDTO) {
        Conta conta = contaService.realizarDeposito(id, valorDTO);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("/{id}/saque")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO realizarSaque(@PathVariable long id, @RequestBody ValorDTO valorDTO) {
        Conta conta = contaService.realizarSaque(id, valorDTO);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("/{id}/pagar")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO realizarPagamento(@PathVariable long id, @RequestBody ValorDTO valorDTO) {
        Conta conta = contaService.realizarPagamento(id, valorDTO);
        return new ContaResponseDTO(conta);
    }

    @GetMapping("{id}/extrato")
    @ResponseStatus(HttpStatus.OK)
    public List<TransacaoSimpleDTO> getExtrato(@PathVariable long id){
        return contaService.getExtrato(id);
    }

    @PostMapping("{id}/extrato/periodo")
    @ResponseStatus(HttpStatus.OK)
    public List<TransacaoSimpleDTO> getExtratoPeriodo(long id, ExtratoPeriodoDTO extratoPeriodoDTO){
        return contaService.getExtratoPeriodo(id, extratoPeriodoDTO);
    }

    @PutMapping("{id}/ativar")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO ativarConta(@PathVariable long id){
        Conta conta = contaService.ativarConta(id);
        return new ContaResponseDTO(conta);
    }

    @PutMapping("{id}/desativar")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO desativarConta(@PathVariable long id){
        Conta conta = contaService.desativarConta(id);
        return new ContaResponseDTO(conta);
    }

    @PutMapping("{id}/ativarPix")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO ativarPix(@PathVariable long id){
        Conta conta = contaService.ativarPix(id);
        return new ContaResponseDTO(conta);
    }

    @PutMapping("{id}/desativarPix")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO desativarPix(@PathVariable long id){
        Conta conta = contaService.desativarPix(id);
        return new ContaResponseDTO(conta);
    }

    @PostMapping("{id}/pix")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponseDTO realizarPix(@PathVariable long id, @RequestBody PixDTO pixDTO){
        Conta conta = contaService.realizarPix(id, pixDTO);
        return new ContaResponseDTO(conta);
    }











}