package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.CreateAgenciaDTO;
import acc.br.accenturebank.dto.UpdateAgenciaDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.service.AgenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/agencias")
public class AgenciaController {

    @Autowired
    private AgenciaService agenciaService;

    @PostMapping
    public Agencia createAgencia(@Valid @RequestBody CreateAgenciaDTO agenciaDTO) {
        return agenciaService.createAgencia(agenciaDTO);
    }

    @PutMapping("/{id}")
    public Agencia updateAgencia(@PathVariable int id, @Valid @RequestBody UpdateAgenciaDTO agenciaDTO) {
        return agenciaService.updateAgencia(id, agenciaDTO);
    }

    @GetMapping
    public List<Agencia> getAllAgencias() {
        return agenciaService.getAllAgencias();
    }

    @GetMapping("/{id}")
    public Agencia getAgenciaById(@PathVariable int id) {
        return agenciaService.getAgenciaById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAgencia(@PathVariable int id) {
        agenciaService.deleteAgencia(id);
    }
}