package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.ClienteResponseDTO;
import acc.br.accenturebank.dto.CreateClienteDTO;
import acc.br.accenturebank.dto.UpdateClienteDTO;
import acc.br.accenturebank.exception.ClienteNaoEncontradoException;
import acc.br.accenturebank.exception.SenhaIncorretaException;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.repository.ClienteRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente login(String login, String senha) {
        Optional<Cliente> cliente = clienteRepository.findByCpf(login);

        if (cliente.isEmpty()) {
            cliente = clienteRepository.findByEmail(login);
        }

        if (cliente.isEmpty() || cliente == null ) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado.");
        }

        if (!cliente.get().getSenha().equals(senha)) {
            throw new SenhaIncorretaException("Senha incorreta.");
        }

        return cliente.get();
    }

    public Cliente createCliente(CreateClienteDTO createClienteDTO) {

        try {


            List<Conta> contas = new ArrayList<>();

            Cliente cliente = Cliente.builder()
                    .cpf(createClienteDTO.getCpf())
                    .nome(createClienteDTO.getNome())
                    .email(createClienteDTO.getEmail())
                    .senha(createClienteDTO.getSenha())
                    .telefone(createClienteDTO.getTelefone())
                    .cep(createClienteDTO.getCep())
                    .numeroEndereco(createClienteDTO.getNumeroEndereco())
                    .complemento(createClienteDTO.getComplemento())
                    .dataNascimento(createClienteDTO.getDataNascimento())
                    .contas(contas)
                    .build();

            return clienteRepository.save(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao criar a Cliente: ".concat(e.getMessage()), e);
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
            throw new RuntimeException("Falha ao atualizar a Cliente: ".concat(e.getMessage()), e);
        }

    }

    public Cliente getClienteById(int id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente com id %d não foi encontrado.".formatted(id)));
    }

    public Cliente getClienteByEmail(String email){
        return clienteRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Cliente com email %s não foi encontrado.".formatted(email)));
    }

    public List<ClienteResponseDTO> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public void deleteCliente(int id) {

        try {
            //verificar se o cliente existe
            this.getClienteById(id);

            clienteRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao deletar a Cliente: ".concat(e.getMessage()), e);
        }
    }

    private ClienteResponseDTO converterParaDTO(Cliente cliente){
        return new ClienteResponseDTO(cliente);
    }
}