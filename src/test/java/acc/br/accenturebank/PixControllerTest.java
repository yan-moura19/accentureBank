package acc.br.accenturebank;

import acc.br.accenturebank.controller.PixController;
import acc.br.accenturebank.dto.pix.CreatePixDTO;
import acc.br.accenturebank.dto.pix.PixResponseDTO;
import acc.br.accenturebank.dto.pix.UpdatePixDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.model.enums.TipoChavePix;
import acc.br.accenturebank.model.enums.TipoConta;
import acc.br.accenturebank.service.PixService;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PixControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PixService pixService;

    @InjectMocks
    private PixController pixController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pixController).build();
    }

    @Test
    public void testGetPixById() throws Exception {
        Agencia agencia = Agencia.builder()
                .id(1)
                .nome("Test Agencia")
                .endereco("Test Endereco")
                .telefone("8333412973")
                .contas(new ArrayList<>())
                .build();

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

        Pix pix = Pix.builder()
                .id(1)
                .tipo(TipoChavePix.CPF)
                .chave("12345678901")
                .conta(conta)
                .build();

        // Mock the service method
        when(pixService.getPixById(1)).thenReturn(pix);

        // Perform the GET request and validate the response
        mockMvc.perform(get("/pix/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pix.getId()))
                .andExpect(jsonPath("$.chave").value(pix.getChave()))
                .andExpect(jsonPath("$.tipo").value(pix.getTipo().name()));
    }
}
