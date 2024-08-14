package acc.br.accenturebank;

import acc.br.accenturebank.dto.cliente.ClienteDetailedDTO;
import acc.br.accenturebank.dto.cliente.ClienteResponseDTO;
import acc.br.accenturebank.dto.cliente.CreateClienteDTO;
import acc.br.accenturebank.dto.cliente.UpdateClienteDTO;
import acc.br.accenturebank.exception.InvalidCredentialsException;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.repository.ClienteRepository;
import acc.br.accenturebank.service.ClienteService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1)
                .cpf("12345678901")
                .nome("Test Cliente")
                .email("test@example.com")
                .senha("password")
                .telefone("123456789")
                .cep("12345678")
                .numeroEndereco("123")
                .complemento("Apto 1")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .contas(new ArrayList<>())
                .build();
    }

    @Test
    void testLoginSuccessful() {
        when(clienteRepository.findByCpf(any(String.class))).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.login("12345678901", "password");

        assertTrue(result.isPresent());
        assertEquals(cliente, result.get());
    }

    @Test
    void testLoginFailure() {

        when(clienteRepository.findByCpf(any(String.class))).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> {
                    clienteService.login("09311037460", "Teste123@");
                });


        assertEquals("Credenciais Inválidas.", exception.getMessage());
    }

    @Test
    void testCreateCliente() {
        CreateClienteDTO createClienteDTO = new CreateClienteDTO(
                "09311037460",
                "Joao",
                "joao@example.com",
                "58402021",
                "60",
                "apto 1002",
                LocalDate.of(1990, 1, 1),
                "Teste123@",
                "83987551234"
                );
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.createCliente(createClienteDTO);

        assertNotNull(result);
        assertEquals(cliente.getCpf(), result.getCpf());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }



    @Test
    void testGetClienteById() {
        when(clienteRepository.findById(any(Integer.class))).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.getClienteById(1);

        assertNotNull(result);
        assertEquals(cliente.getId(), result.getId());
    }

    @Test
    void testGetClienteByIdNotFound() {
        when(clienteRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.getClienteById(1));
    }

    @Test
    void testGetClienteByEmail() {
        when(clienteRepository.findByEmail(any(String.class))).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.getClienteByEmail("joao@example.com");

        assertNotNull(result);
        assertEquals(cliente.getEmail(), result.getEmail());
    }

    @Test
    void testGetClienteByEmailNotFound() {
        when(clienteRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.getClienteByEmail("maria@example.com"));
    }

    @Test
    void testGetAllClientes() {
        List<Cliente> clientes = List.of(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<ClienteDetailedDTO> result = clienteService.getAllClientes();

        assertEquals(1, result.size());
        assertEquals(cliente.getNome(), result.get(0).getNome());
    }

    @Test
    void testDeleteCliente() {
        when(clienteRepository.findById(any(Integer.class))).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).deleteById(any(Integer.class));

        clienteService.deleteCliente(1);

        verify(clienteRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteClienteNotFound() {
        when(clienteRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.deleteCliente(1));
    }
}
