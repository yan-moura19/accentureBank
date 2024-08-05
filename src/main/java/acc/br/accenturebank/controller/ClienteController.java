package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.CreateClienteDTO;
import acc.br.accenturebank.dto.UpdateClienteDTO;
import acc.br.accenturebank.model.Cliente;


import acc.br.accenturebank.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteRepository;


    @PostMapping
    public Cliente createCliente(@Valid @RequestBody CreateClienteDTO createClienteDTO) {

        return clienteRepository.createCliente(createClienteDTO);
    }

    @PutMapping("/{id}")
    public Cliente updateCliente(@PathVariable int id, @Valid @RequestBody UpdateClienteDTO updateClienteDTO) {

        return clienteRepository.updateCliente(id, updateClienteDTO);
    }

    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable int id) {
        return clienteRepository.getClienteById(id);
    }

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.getAllClientes();
    }

    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable int id) {
        clienteRepository.deleteCliente(id);
    }


}
