package acc.br.accenturebank;

import acc.br.accenturebank.controller.ClienteController;
import acc.br.accenturebank.dto.cliente.ClienteDetailedDTO;
import acc.br.accenturebank.dto.cliente.ClienteResponseDTO;
import acc.br.accenturebank.dto.cliente.CreateClienteDTO;
import acc.br.accenturebank.dto.cliente.UpdateClienteDTO;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
    }

    @Test
    public void testCreateCliente() throws Exception {
        LocalDate date = LocalDate.of(2002, 1, 1);

        Cliente cliente = Cliente.builder()
                .id(1)
                .cpf("09311037460")
                .nome("victor")
                .email("victor@gmail.com")
                .senha("Teste")
                .telefone("8333412982")
                .cep("58402025")
                .telefone("8333412982")
                .cep("58402025")
                .numeroEndereco("60")
                .dataNascimento(date)
                .contas(new ArrayList<>())
                .build();

        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(cliente);

        when(clienteService.createCliente(any(CreateClienteDTO.class))).thenReturn(cliente);

        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"cpf\": \"09311037460\",\n" +
                                "  \"nome\": \"victor\",\n" +
                                "  \"email\": \"victor@gmail.com\",\n" +
                                "  \"cep\": \"55103-817\",\n" +
                                "  \"numeroEndereco\": \"54\",\n" +
                                "  \"complemento\": \"ap 1004\",\n" +
                                "  \"dataNascimento\": \"2002-01-01\",\n" +
                                "  \"senha\": \"Teste123?\",\n" +
                                "  \"telefone\": \"(46)4625-8291\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cliente.getId()))
                .andDo(print());

        verify(clienteService, times(1)).createCliente(any(CreateClienteDTO.class));
    }

    @Test
    public void testGetClienteById() throws Exception {
        int id = 1;
        Cliente cliente = new Cliente(/* parâmetros necessários */);
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(cliente);

        when(clienteService.getClienteById(id)).thenReturn(cliente);

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cliente.getId()))
                .andDo(print());

        verify(clienteService, times(1)).getClienteById(id);
    }

    @Test
    public void testGetAllClientes() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        LocalDate date = LocalDate.of(2002, 1, 1);

        Cliente cliente1 = Cliente.builder()
                .id(1)
                .cpf("09311037460")
                .nome("victor")
                .email("victor@gmail.com")
                .senha("Teste")
                .telefone("8333412982")
                .cep("58402025")
                .telefone("8333412982")
                .cep("58402025")
                .numeroEndereco("60")
                .dataNascimento(date)
                .contas(new ArrayList<>())
                .build();

        clientes.add(cliente1);

        List<ClienteDetailedDTO> clienteDetailedDTOS = new ArrayList<>();
        for (Cliente cliente : clientes) {
            clienteDetailedDTOS.add(new ClienteDetailedDTO(cliente));
        }

        when(clienteService.getAllClientes()).thenReturn(clienteDetailedDTOS);

        mockMvc.perform(MockMvcRequestBuilders.get("/clientes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(clientes.get(0).getId()))
                .andDo(print());

        verify(clienteService, times(1)).getAllClientes();
    }

    @Test
    public void testUpdateCliente() throws Exception {
        int id = 1;
        UpdateClienteDTO updateClienteDTO = UpdateClienteDTO.builder()
                .email("victor@gmail.com")
                .cep("58402025")
                .numeroEndereco("60")
                .complemento("ap 1004")
                .telefone("(46)4625-8291")
                .build();
        Cliente cliente = Cliente.builder()
                .id(1)
                .cpf("09311037460")
                .nome("victor")
                .email("victor@gmail.com")
                .senha("Teste123?")
                .telefone("(46)4625-8291")
                .cep("58402025")
                .numeroEndereco("60")
                .complemento("ap 1004")
                .dataNascimento(LocalDate.of(2002, 1, 1))
                .contas(new ArrayList<>())
                .build();

        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(cliente);

        when(clienteService.updateCliente(eq(id), any(UpdateClienteDTO.class))).thenReturn(cliente);

        mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}", id)
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"cpf\": \"09311037460\",\n" +
                                "  \"nome\": \"victor\",\n" +
                                "  \"email\": \"victor@gmail.com\",\n" +
                                "  \"cep\": \"58402025\",\n" +
                                "  \"numeroEndereco\": \"60\",\n" +
                                "  \"complemento\": \"ap 1004\",\n" +
                                "  \"dataNascimento\": \"2002-01-01\",\n" +
                                "  \"senha\": \"Teste123?\",\n" +
                                "  \"telefone\": \"(46)4625-8291\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cliente.getId()))
                .andDo(print());

        verify(clienteService, times(1)).updateCliente(eq(id), any(UpdateClienteDTO.class));
    }

    @Test
    public void testDeleteCliente() throws Exception {
        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        verify(clienteService, times(1)).deleteCliente(id);
    }

}

