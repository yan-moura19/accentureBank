package acc.br.accenturebank.controller;

import acc.br.accenturebank.model.Conta;

import acc.br.accenturebank.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @GetMapping
    public List<Conta> getAllContas() {
        return contaService.getAllContas();
    }

    @GetMapping("/{id}")
    public Conta getContaById(@PathVariable Long id) {
        return contaService.getContaById(id);
    }

    @PostMapping
    public Conta createConta(@RequestBody Conta conta) {
        return contaService.createConta(conta);
    }

    @PutMapping("/{id}")
    public Conta updateConta(@PathVariable int id, @RequestBody Conta conta) {
        return contaService.updateConta(id, conta);
    }

    @DeleteMapping("/{id}")
    public void deleteConta(@PathVariable Long id) {
        contaService.deleteConta(id);
    }
}