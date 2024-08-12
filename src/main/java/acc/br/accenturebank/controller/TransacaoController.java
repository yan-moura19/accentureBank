package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.transacao.CreateTransacaoDTO;
import acc.br.accenturebank.dto.transacao.TransacaoResponseDTO;
import acc.br.accenturebank.dto.transacao.UpdateTransacaoDTO;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransacaoResponseDTO createTransacao(@RequestBody CreateTransacaoDTO createTransacaoDTO) throws Exception {
        Transacao transacao = transacaoService.createTransacao(createTransacaoDTO);
        return new TransacaoResponseDTO(transacao);
    }

    @GetMapping
    public List<TransacaoResponseDTO> getAllTransacoes() {
        return transacaoService.getAllTransacoes();
    }

    @GetMapping("/{id}")
    public TransacaoResponseDTO getTransacaoById(@PathVariable int id) {
        Transacao transacao = transacaoService.getTransacaoById(id);
        return new TransacaoResponseDTO(transacao);
    }



    @PutMapping("/{id}")
    public TransacaoResponseDTO updateTransacao(@PathVariable int id, @RequestBody UpdateTransacaoDTO updateTransacaoDTO) {
        Transacao transacao = transacaoService.updateTransacao(id, updateTransacaoDTO);
        return new TransacaoResponseDTO(transacao);
    }

    @DeleteMapping("/{id}")
    public void deleteTransacao(@PathVariable int id) {
        transacaoService.deleteTransacao(id);
    }
}
