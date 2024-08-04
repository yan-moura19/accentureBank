package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.CreateTransacaoDTO;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @GetMapping
    public List<Transacao> getAllTransacoes() {
        return transacaoService.getAllTransacoes();
    }

    @GetMapping("/{id}")
    public Transacao getTransacaoById(@PathVariable int id) {
        return transacaoService.getTransacaoById(id);
    }

    @PostMapping
    public Transacao createTransacao(@RequestBody CreateTransacaoDTO createTransacaoDTO) throws Exception {
        return transacaoService.createTransacao(createTransacaoDTO);
    }

    @PutMapping("/{id}")
    public Transacao updateTransacao(@PathVariable int id, @RequestBody Transacao transacao) {
        return transacaoService.updateTransacao(id, transacao);
    }

    @DeleteMapping("/{id}")
    public void deleteTransacao(@PathVariable int id) {
        transacaoService.deleteTransacao(id);
    }
}
