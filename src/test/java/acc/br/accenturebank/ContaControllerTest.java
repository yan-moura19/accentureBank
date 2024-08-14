package acc.br.accenturebank;

import acc.br.accenturebank.controller.ContaController;
import acc.br.accenturebank.dto.conta.ContaResponseDTO;
import acc.br.accenturebank.dto.conta.CreateContaDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.enums.TipoConta;
import acc.br.accenturebank.service.ContaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ContaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contaController).build();
    }

    @Test
    public void testCreateConta() throws Exception {
        // Prepare the DTO
        CreateContaDTO createContaDTO = CreateContaDTO.builder()
                .tipoConta(TipoConta.CORRENTE)
                .idAgencia(1)
                .idCliente(1)
                .build();

        // Prepare the Agencia
        Agencia agencia = Agencia.builder()
                .id(1)
                .nome("Test Agencia")
                .endereco("Test Endereco")
                .telefone("8333412973")
                .contas(new ArrayList<>())
                .build();

        // Prepare the Cliente
        Cliente cliente = Cliente.builder()
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

        // Prepare the Conta
        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.ZERO)
                .saldoSeparado(BigDecimal.ZERO)
                .ativa(true)
                .pixAtivo(false)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();

        // Prepare the Response DTO
        ContaResponseDTO contaResponseDTO = new ContaResponseDTO(conta);

        // Mock the service method
        when(contaService.createConta(any(CreateContaDTO.class))).thenReturn(conta);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createContaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(conta.getId()))
                .andExpect(jsonPath("$.numero").value(conta.getNumero()))
                .andExpect(jsonPath("$.saldo").value(conta.getSaldo().toString()))
                .andExpect(jsonPath("$.saldoSeparado").value(conta.getSaldoSeparado().toString()))
                .andExpect(jsonPath("$.ativa").value(conta.isAtiva()))
                .andExpect(jsonPath("$.pixAtivo").value(conta.isPixAtivo()))
                .andExpect(jsonPath("$.tipoConta").value(conta.getTipoConta().name()))
                .andExpect(jsonPath("$.agencia.id").value(conta.getAgencia().getId()))
                .andExpect(jsonPath("$.cliente.id").value(conta.getCliente().getId()));
    }
}

