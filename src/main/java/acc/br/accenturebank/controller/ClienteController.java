package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.cliente.ClienteDetailedDTO;
import acc.br.accenturebank.dto.cliente.ClienteResponseDTO;
import acc.br.accenturebank.dto.cliente.CreateClienteDTO;
import acc.br.accenturebank.dto.cliente.UpdateClienteDTO;
import acc.br.accenturebank.model.Cliente;


import acc.br.accenturebank.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO createCliente(@Valid @RequestBody CreateClienteDTO createClienteDTO) {
        Cliente cliente = clienteService.createCliente(createClienteDTO);
        return new ClienteResponseDTO(cliente);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponseDTO updateCliente(@PathVariable int id, @Valid @RequestBody UpdateClienteDTO updateClienteDTO) {
        Cliente cliente = clienteService.updateCliente(id, updateClienteDTO);
        return new ClienteResponseDTO(cliente);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponseDTO getClienteById(@PathVariable int id) {
        Cliente cliente = clienteService.getClienteById(id);
        return new ClienteResponseDTO(cliente);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClienteDetailedDTO> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCliente(@PathVariable int id) {
        clienteService.deleteCliente(id);
    }


}
