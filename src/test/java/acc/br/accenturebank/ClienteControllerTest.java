//package acc.br.accenturebank;
//
//import acc.br.accenturebank.controller.ClienteController;
//import acc.br.accenturebank.dto.cliente.ClienteResponseDTO;
//import acc.br.accenturebank.dto.cliente.CreateClienteDTO;
//import acc.br.accenturebank.dto.cliente.UpdateClienteDTO;
//import acc.br.accenturebank.model.Cliente;
//import acc.br.accenturebank.service.ClienteService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//
//public class ClienteControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private ClienteService clienteService;
//
//    @InjectMocks
//    private ClienteController clienteController;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
//    }

//    @Test
//    public void testCreateCliente() throws Exception {
//        LocalDate date = LocalDate.of(2002,1,1);
//
//        CreateClienteDTO createClienteDTO = CreateClienteDTO.builder()
//                .cpf("09311037460")
//                .nome("victor")
//                .email("victor@gmail.com")
//                .cep("58402025")
//                .numeroEndereco("60")
//                .dataNascimento(date)
//                .senha("Teste")
//                .telefone("8333412982")
//                .build();
//        Cliente cliente = new Cliente();
//        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(cliente);
//
//        when(clienteService.createCliente(any(CreateClienteDTO.class))).thenReturn(cliente);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
//                        .contentType("application/json")
//                        .content("{ /* JSON de createClienteDTO */ }"))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cliente.getId()))
//                .andDo(print());
//
//        verify(clienteService, times(1)).createCliente(any(CreateClienteDTO.class));
//    }
//
//    @Test
//    public void testUpdateCliente() throws Exception {
//        int id = 1;
//        UpdateClienteDTO updateClienteDTO = new UpdateClienteDTO(/* parâmetros necessários */);
//        Cliente cliente = new Cliente(/* parâmetros necessários */);
//        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(cliente);
//
//        when(clienteService.updateCliente(eq(id), any(UpdateClienteDTO.class))).thenReturn(cliente);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}", id)
//                        .contentType("application/json")
//                        .content("{ /* JSON de updateClienteDTO */ }"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cliente.getId()))
//                .andDo(print());
//
//        verify(clienteService, times(1)).updateCliente(eq(id), any(UpdateClienteDTO.class));
//    }
//
//    @Test
//    public void testGetClienteById() throws Exception {
//        int id = 1;
//        Cliente cliente = new Cliente(/* parâmetros necessários */);
//        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(cliente);
//
//        when(clienteService.getClienteById(id)).thenReturn(cliente);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{id}", id))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cliente.getId()))
//                .andDo(print());
//
//        verify(clienteService, times(1)).getClienteById(id);
//    }
//
//    @Test
//    public void testGetAllClientes() throws Exception {
//        List<Cliente> clientes = new ArrayList<>();
//        clientes.add(new Cliente(/* parâmetros necessários */));
//        List<ClienteResponseDTO> clienteResponseDTOs = new ArrayList<>();
//        for (Cliente cliente : clientes) {
//            clienteResponseDTOs.add(new ClienteResponseDTO(cliente));
//        }
//
//        when(clienteService.getAllClientes()).thenReturn(clienteResponseDTOs);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/clientes"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(clientes.get
