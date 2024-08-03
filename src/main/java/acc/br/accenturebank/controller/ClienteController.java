package acc.br.accenturebank.controller;

import acc.br.accenturebank.dto.ClienteDTO;
import acc.br.accenturebank.model.Cliente;


import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteRepository;

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.getAllClientes();
    }

    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable int id) {
        return clienteRepository.getClienteById(id);
    }

    @PostMapping
    public Cliente createCliente(@RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setSenha(clienteDTO.getSenha());
        cliente.setContato(clienteDTO.getContato());
        cliente.setContas(new ArrayList<>());
        return clienteRepository.createCliente(cliente);
    }

    @PutMapping("/{id}")
    public Cliente updateCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        cliente.setIdCliente(id);
        return clienteRepository.updateCliente(id,cliente);
    }

    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable int id) {
        clienteRepository.deleteCliente(id);
    }
}
