package acc.br.accenturebank.controller;

import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.service.AgenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agencias")
public class AgenciaController {

    @Autowired
    private AgenciaService agenciaService;

    @GetMapping
    public List<Agencia> getAllAgencias() {
        return agenciaService.getAllAgencias();
    }

    @GetMapping("/{id}")
    public Agencia getAgenciaById(@PathVariable int id) {
        return agenciaService.getAgenciaById(id);
    }

    @PostMapping
    public Agencia createAgencia(@RequestBody Agencia agencia) {
        return agenciaService.createAgencia(agencia);
    }

    @PutMapping("/{id}")
    public Agencia updateAgencia(@PathVariable int id, @RequestBody Agencia agencia) {
        return agenciaService.updateAgencia(id, agencia);
    }

    @DeleteMapping("/{id}")
    public void deleteAgencia(@PathVariable int id) {
        agenciaService.deleteAgencia(id);
    }
}