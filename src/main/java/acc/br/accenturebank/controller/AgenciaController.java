package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.agencia.AgenciaResponseDTO;
import acc.br.accenturebank.dto.agencia.CreateAgenciaDTO;
import acc.br.accenturebank.dto.agencia.UpdateAgenciaDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.service.AgenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/agencias")
public class AgenciaController {

    @Autowired
    private AgenciaService agenciaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgenciaResponseDTO createAgencia(@Valid @RequestBody CreateAgenciaDTO agenciaDTO) {
        Agencia agencia = agenciaService.createAgencia(agenciaDTO);
        return new AgenciaResponseDTO(agencia);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AgenciaResponseDTO updateAgencia(@PathVariable int id, @Valid @RequestBody UpdateAgenciaDTO agenciaDTO) {
        Agencia agencia = agenciaService.updateAgencia(id, agenciaDTO);
        return new AgenciaResponseDTO(agencia);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AgenciaResponseDTO> getAllAgencias() {
        return agenciaService.getAllAgencias();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AgenciaResponseDTO getAgenciaById(@PathVariable int id) {
        Agencia agencia = agenciaService.getAgenciaById(id);
        return new AgenciaResponseDTO(agencia);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAgencia(@PathVariable int id) {
        agenciaService.deleteAgencia(id);
    }
}