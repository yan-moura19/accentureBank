package acc.br.accenturebank;

import acc.br.accenturebank.controller.ContaController;
import acc.br.accenturebank.dto.conta.*;
import acc.br.accenturebank.dto.pix.CreatePixDTO;
import acc.br.accenturebank.dto.transacao.TransacaoSimpleDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.model.enums.TipoChavePix;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

        CreateContaDTO createContaDTO = CreateContaDTO.builder()
                .tipoConta(TipoConta.CORRENTE)
                .idAgencia(1)
                .idCliente(1)
                .build();


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


        ContaResponseDTO contaResponseDTO = new ContaResponseDTO(conta);


        when(contaService.createConta(any(CreateContaDTO.class))).thenReturn(conta);


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

    @Test
    public void testGetContaById() throws Exception {

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

        // Mock the service method
        when(contaService.getContaById(anyLong())).thenReturn(conta);

        // Perform the GET request and validate the response
        mockMvc.perform(get("/contas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()))
                .andExpect(jsonPath("$.numero").value(conta.getNumero()))
                .andExpect(jsonPath("$.saldo").value(conta.getSaldo().toString()))
                .andExpect(jsonPath("$.saldoSeparado").value(conta.getSaldoSeparado().toString()))
                .andExpect(jsonPath("$.ativa").value(conta.isAtiva()))
                .andExpect(jsonPath("$.pixAtivo").value(conta.isPixAtivo()))
                .andExpect(jsonPath("$.tipoConta").value(conta.getTipoConta().name()))
                .andExpect(jsonPath("$.agencia.id").value(conta.getAgencia().getId()))
                .andExpect(jsonPath("$.agencia.nome").value(conta.getAgencia().getNome()))
                .andExpect(jsonPath("$.cliente.id").value(conta.getCliente().getId()))
                .andExpect(jsonPath("$.cliente.cpf").value(conta.getCliente().getCpf()));
    }

    @Test
    public void testUpdateConta() throws Exception {
        UpdateContaDTO updateContaDTO = UpdateContaDTO.builder()
                .tipoConta(TipoConta.CORRENTE)
                .idAgencia(1)
                .idCliente(1)
                .saldo(BigDecimal.ZERO)
                .saldoSeparado(BigDecimal.ZERO)
                .ativa(true)
                .pixAtivo(false)
                .build(); // Configure conforme necessário
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

        ContaResponseDTO contaResponseDTO = new ContaResponseDTO(conta);

        when(contaService.updateConta(anyLong(), any(UpdateContaDTO.class))).thenReturn(conta);

        mockMvc.perform(put("/contas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateContaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }

    @Test
    public void testDeleteConta() throws Exception {
        mockMvc.perform(delete("/contas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(contaService, times(1)).deleteConta(1L);
    }

    @Test
    public void testCriaChavePix() throws Exception {
        CreatePixDTO createPixDTO = CreatePixDTO.builder()
                .tipo(TipoChavePix.EMAIL)
                .chave("victorvirgolino@gmail.com")
                .IdConta("1")
                .build();
        Pix pix = new Pix();

        when(contaService.addPixToConta(any(CreatePixDTO.class))).thenReturn(pix);

        mockMvc.perform(post("/contas/1/chavepix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPixDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pix.getId()));
    }

    @Test
    public void testSepararValor() throws Exception {
        ValorDTO valorDTO = ValorDTO.builder()
                .valor(BigDecimal.valueOf(100))
                .build();

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

        ContaResponseDTO contaResponseDTO = new ContaResponseDTO(conta);

        when(contaService.separarValor(anyLong(), any(ValorDTO.class))).thenReturn(conta);

        mockMvc.perform(post("/contas/1/separar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }

    @Test
    public void testResgatarValor() throws Exception {
        ValorDTO valorDTO = ValorDTO.builder()
                .valor(BigDecimal.valueOf(100))
                .build();

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
        ContaResponseDTO responseDTO = new ContaResponseDTO(conta);

        when(contaService.resgatarSaldoSeparado(anyLong(), any(ValorDTO.class))).thenReturn(conta);

        mockMvc.perform(post("/contas/1/resgatar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }

    @Test
    public void testTransferir() throws Exception {
        TransferenciaDTO transferenciaDTO = TransferenciaDTO.builder()
                .idContaOrigem(1)
                .numeroContaDestino("00000002")
                .valor(BigDecimal.valueOf(100))
                .build();

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

        ContaResponseDTO responseDTO = new ContaResponseDTO(conta);

        when(contaService.transferir(any(TransferenciaDTO.class))).thenReturn(conta);

        mockMvc.perform(post("/contas/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferenciaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }

    @Test
    public void testRealizarRecarga() throws Exception {
        RecargaDTO recargaDTO = RecargaDTO.builder()
                .numeroCelular("(83)98766-0717")
                .valor(BigDecimal.valueOf(100))
                .build();
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

        ContaResponseDTO responseDTO = new ContaResponseDTO(conta);

        when(contaService.realizarRecarga(anyLong(), any(RecargaDTO.class))).thenReturn(conta);

        mockMvc.perform(post("/contas/1/recarga")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recargaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }

    @Test
    public void testRealizarDeposito() throws Exception {
        ValorDTO valorDTO = ValorDTO.builder()
                .valor(BigDecimal.valueOf(100))
                .build();

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
        ContaResponseDTO responseDTO = new ContaResponseDTO(conta);

        when(contaService.realizarDeposito(anyLong(), any(ValorDTO.class))).thenReturn(conta);

        mockMvc.perform(post("/contas/1/deposito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }

    @Test
    public void testRealizarSaque() throws Exception {
        ValorDTO valorDTO = ValorDTO.builder()
                .valor(BigDecimal.valueOf(100))
                .build();

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
        ContaResponseDTO responseDTO = new ContaResponseDTO(conta);

        when(contaService.realizarSaque(anyLong(), any(ValorDTO.class))).thenReturn(conta);

        mockMvc.perform(post("/contas/1/saque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }

    @Test
    public void testRealizarPagamento() throws Exception {
        ValorDTO valorDTO = ValorDTO.builder()
                .valor(BigDecimal.valueOf(100))
                .build();

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
        ContaResponseDTO responseDTO = new ContaResponseDTO(conta);

        when(contaService.realizarPagamento(anyLong(), any(ValorDTO.class))).thenReturn(conta);

        mockMvc.perform(post("/contas/1/pagar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }

    @Test
    public void testGetExtrato() throws Exception {
        List<TransacaoSimpleDTO> extrato = new ArrayList<>();
        // adicionar transações ao extrato conforme necessário

        when(contaService.getExtrato(anyLong())).thenReturn(extrato);

        mockMvc.perform(get("/contas/1/extrato")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(extrato.size()));
    }


//    @Test
//    public void testGetExtratoPeriodo() throws Exception {
//        ExtratoPeriodoDTO extratoPeriodoDTO = new ExtratoPeriodoDTO();
//        extratoPeriodoDTO.setStartDate(LocalDate.parse("2024-08-15"));
//        extratoPeriodoDTO.setEndDate(LocalDate.parse("2024-08-15"));
//
//        List<TransacaoSimpleDTO> extrato = new ArrayList<>();
//
//
//        when(contaService.getExtratoPeriodo(anyLong(), any(ExtratoPeriodoDTO.class))).thenReturn(extrato);
//
//        mockMvc.perform(post("/contas/1/extrato/periodo")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(extratoPeriodoDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(extrato.size()));
//    }


    @Test
    public void testAtivarConta() throws Exception {
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
        ContaResponseDTO responseDTO = new ContaResponseDTO(conta);

        when(contaService.ativarConta(anyLong())).thenReturn(conta);

        mockMvc.perform(put("/contas/1/ativar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }

    @Test
    public void testDesativarConta() throws Exception {
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
        ContaResponseDTO responseDTO = new ContaResponseDTO(conta);

        when(contaService.desativarConta(anyLong())).thenReturn(conta);

        mockMvc.perform(put("/contas/1/desativar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(conta.getId()));
    }


}

