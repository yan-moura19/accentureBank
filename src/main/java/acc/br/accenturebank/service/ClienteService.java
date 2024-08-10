package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.CreateClienteDTO;
import acc.br.accenturebank.dto.UpdateClienteDTO;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.repository.ClienteRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Optional<Cliente> login(String login, String senha) {
        Optional<Cliente> cliente = clienteRepository.findByCpf(login);

        if (cliente.isEmpty()) {
            cliente = clienteRepository.findByEmail(login);
        }

        if (cliente.isPresent() && cliente.get().getSenha().equals(senha)) {
            return cliente;
        }

        return Optional.empty();
    }

    public Cliente createCliente(CreateClienteDTO createClienteDTO) {

        try {
            Cliente cliente = new Cliente();

            List<Conta> contas = new ArrayList<>();

            cliente.setCpf(createClienteDTO.getCpf());
            cliente.setNome(createClienteDTO.getNome());
            cliente.setEmail(createClienteDTO.getEmail());
            cliente.setSenha(createClienteDTO.getSenha());
            cliente.setTelefone(createClienteDTO.getTelefone());
            cliente.setCep(createClienteDTO.getCep());
            cliente.setNumeroEndereco(createClienteDTO.getNumeroEndereco());
            cliente.setComplemento(createClienteDTO.getComplemento());
            cliente.setDataNascimento(createClienteDTO.getDataNascimento());
            cliente.setContas(contas);

            return clienteRepository.save(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao criar a cliente: ".concat(e.getMessage()), e);
        }
    }

    public Cliente updateCliente(int id, UpdateClienteDTO updateClienteDTO) {

        try {
            Cliente cliente = this.getClienteById(id);

            String novoCpf = updateClienteDTO.getCpf();
            String novoNome = updateClienteDTO.getNome();
            String novoEmail = updateClienteDTO.getEmail();
            String novaSenha = updateClienteDTO.getSenha();
            String novoTelefone = updateClienteDTO.getTelefone();

            if (novoCpf != null) {
                cliente.setCpf(novoCpf);
            }

            if (novoNome != null) {
                cliente.setNome(novoNome);
            }

            if (novoEmail != null) {
                cliente.setEmail(novoEmail);
            }

            if (novaSenha != null) {
                cliente.setSenha(novaSenha);
            }

            if (novoTelefone != null) {
                cliente.setTelefone(novoTelefone);
            }

            return clienteRepository.save(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao atualizar a cliente: ".concat(e.getMessage()), e);
        }

    }

    public Cliente getClienteById(int id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente com id %d não foi encontrado.".formatted(id)));
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public void deleteCliente(int id) {

        try {
            //verificar se o cliente existe
            this.getClienteById(id);

            clienteRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao deletar a cliente: ".concat(e.getMessage()), e);
        }
    }
}