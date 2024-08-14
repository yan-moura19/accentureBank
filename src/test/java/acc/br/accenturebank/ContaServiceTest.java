package acc.br.accenturebank;

import acc.br.accenturebank.dto.conta.*;
import acc.br.accenturebank.dto.pix.CreatePixDTO;
import acc.br.accenturebank.dto.transacao.CreateTransacaoDTO;
import acc.br.accenturebank.dto.transacao.TransacaoSimpleDTO;
import acc.br.accenturebank.exception.PeriodoInvalidoException;
import acc.br.accenturebank.exception.SaldoInsuficienteException;
import acc.br.accenturebank.model.*;
import acc.br.accenturebank.model.enums.Operacao;
import acc.br.accenturebank.model.enums.TipoChavePix;
import acc.br.accenturebank.model.enums.TipoConta;
import acc.br.accenturebank.repository.ContaRepository;
import acc.br.accenturebank.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private AgenciaService agenciaService;

    @Mock
    private TransacaoService transacaoService;

    @Mock
    private PixService pixService;

    @InjectMocks
    private ContaService contaService;

    private Conta conta;
    private Cliente cliente;
    private Agencia agencia;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testCreateConta_Success() {

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

        Agencia agencia = Agencia.builder()
                .id(1)
                .nome("Test Agencia")
                .endereco("Test Endereco")
                .telefone("8333412973")
                .contas(new ArrayList<>())
                .build();

        Conta conta = Conta.builder()
                .numero("00000001")
                .build();


        CreateContaDTO createContaDTO = new CreateContaDTO(TipoConta.CORRENTE, 1, 1);

        when(clienteService.getClienteById(1)).thenReturn(cliente);
        when(agenciaService.getAgenciaById(1)).thenReturn(agencia);
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.createConta(createContaDTO);

        assertNotNull(result);
        assertEquals("00000001", result.getNumero());
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    public void testCreateConta_ClienteNotFound() {

        int clienteId = 1;
        CreateContaDTO createContaDTO = new CreateContaDTO(TipoConta.CORRENTE, clienteId, 1);


        when(clienteService.getClienteById(clienteId)).thenReturn(null);


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            contaService.createConta(createContaDTO);
        });


        assertEquals("Cliente com id %d não foi encontrado.".formatted(clienteId), exception.getMessage());


        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    public void testCreateConta_AgenciaNotFound() {
        // Arrange
        int agenciaId = 1;
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

        CreateContaDTO createContaDTO = new CreateContaDTO(TipoConta.CORRENTE, 1, agenciaId);

        // Simula o cliente encontrado, mas a agência não encontrada
        when(clienteService.getClienteById(1)).thenReturn(cliente);
        when(agenciaService.getAgenciaById(agenciaId)).thenReturn(null);

        // Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            contaService.createConta(createContaDTO);
        });

        // Assert
        assertEquals("Agência com id %d não foi encontrada.".formatted(agenciaId), exception.getMessage());

        // Verifica que a conta não foi salva
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    public void testGetContaById_Success() {
        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .build();

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));

        Conta result = contaService.getContaById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    public void testGetContaById_ContaNotFound() {

        long contaId = 1L;


        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            contaService.getContaById(contaId);
        });


        assertEquals("Conta com id %d não foi encontrada.".formatted(contaId), exception.getMessage());


        verify(contaRepository, times(1)).findById(contaId);
    }

    @Test
    public void testUpdateConta_Success() {
        UpdateContaDTO updateContaDTO = new UpdateContaDTO();
        updateContaDTO.setTipoConta(TipoConta.POUPANCA);
        updateContaDTO.setIdAgencia(1);
        updateContaDTO.setIdCliente(1);
        updateContaDTO.setSaldo(BigDecimal.valueOf(1000));
        updateContaDTO.setSaldoSeparado(BigDecimal.valueOf(500));
        updateContaDTO.setAtiva(true);
        updateContaDTO.setPixAtivo(true);

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

        Cliente cliente2 = Cliente.builder()
                .id(1)
                .cpf("12345678902")
                .nome("Test Cliente2")
                .email("test2@example.com")
                .senha("password")
                .telefone("123456781")
                .cep("12345679")
                .numeroEndereco("124")
                .complemento("Apto 2")
                .dataNascimento(LocalDate.of(1992, 1, 1))
                .contas(new ArrayList<>())
                .build();

        Agencia agencia = Agencia.builder()
                .id(1)
                .nome("Test Agencia")
                .endereco("Test Endereco")
                .telefone("8333412973")
                .contas(new ArrayList<>())
                .build();

        Agencia agencia2 = Agencia.builder()
                .id(1)
                .nome("Test Agencia2")
                .endereco("Test Endereco2")
                .telefone("8333412972")
                .contas(new ArrayList<>())
                .build();


        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(10))
                .saldoSeparado(BigDecimal.valueOf(5))
                .ativa(false)
                .pixAtivo(false)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(clienteService.getClienteById(anyInt())).thenReturn(cliente2);
        when(agenciaService.getAgenciaById(anyInt())).thenReturn(agencia2);
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.updateConta(1, updateContaDTO);

        assertNotNull(result);
        assertEquals(TipoConta.POUPANCA, result.getTipoConta());
        assertEquals(BigDecimal.valueOf(1000), result.getSaldo());
        assertEquals(BigDecimal.valueOf(500), result.getSaldoSeparado());
    }

    @Test
    public void testUpdateConta_AgenciaNotFound() {
        int contaId = 1;
        int novoIdAgencia = 2;

        UpdateContaDTO updateContaDTO = new UpdateContaDTO();
        updateContaDTO.setIdAgencia(novoIdAgencia);

        Conta conta = Conta.builder().id(contaId).build();
        when(contaRepository.findById((long) contaId)).thenReturn(Optional.of(conta));
        when(agenciaService.getAgenciaById(novoIdAgencia)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            contaService.updateConta(contaId, updateContaDTO);
        });

        assertEquals("Agência com id %d não foi encontrada.".formatted(novoIdAgencia), exception.getMessage());
        verify(contaRepository, times(1)).findById((long) contaId);
        verify(agenciaService, times(1)).getAgenciaById(novoIdAgencia);
    }

    @Test
    public void testUpdateConta_ClienteNotFound() {
        int contaId = 1;
        int novoIdCliente = 3;

        UpdateContaDTO updateContaDTO = new UpdateContaDTO();
        updateContaDTO.setIdCliente(novoIdCliente);

        Conta conta = Conta.builder().id(contaId).build();
        when(contaRepository.findById((long) contaId)).thenReturn(Optional.of(conta));
        when(agenciaService.getAgenciaById(anyInt())).thenReturn(new Agencia());
        when(clienteService.getClienteById(novoIdCliente)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            contaService.updateConta(contaId, updateContaDTO);
        });

        assertEquals("Cliente com id %d não foi encontrado.".formatted(novoIdCliente), exception.getMessage());
        verify(contaRepository, times(1)).findById((long) contaId);
        verify(agenciaService, times(1)).getAgenciaById(anyInt());
        verify(clienteService, times(1)).getClienteById(novoIdCliente);
    }

    @Test
    public void testDeleteConta_Success() {
        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(10))
                .saldoSeparado(BigDecimal.valueOf(5))
                .ativa(false)
                .pixAtivo(false)
                .tipoConta(TipoConta.CORRENTE)
                .build();

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));

        contaService.deleteConta(1);

        verify(contaRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testGetContaByNumero_ContaNotFound() {
        String numeroConta = "00000001";

        when(contaRepository.findByNumero(numeroConta)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            contaService.getContaByNumero(numeroConta);
        });

        assertEquals("Conta de numero %s não foi encontrada.".formatted(numeroConta), exception.getMessage());
        verify(contaRepository, times(1)).findByNumero(numeroConta);
    }

    @Test
    public void testSepararValor_Success() {

        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(100))
                .saldoSeparado(BigDecimal.valueOf(0))
                .ativa(false)
                .pixAtivo(false)
                .tipoConta(TipoConta.CORRENTE)
                .build();

        ValorDTO valorDTO = new ValorDTO(BigDecimal.valueOf(50.00));

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.separarValor(1, valorDTO);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50.0), result.getSaldoSeparado());
        assertEquals(BigDecimal.valueOf(50.0), result.getSaldo());
    }

    @Test
    public void testSepararValor_ValorNegativo() {
        long id = 1L;
        ValorDTO valorDTO = new ValorDTO(BigDecimal.valueOf(-100));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.separarValor(id, valorDTO);
        });

        assertEquals("O valor não pode ser negativo.", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testSepararValor_SaldoInsuficiente() {
        int id = 1;
        ValorDTO valorDTO = new ValorDTO(BigDecimal.valueOf(500));
        Conta conta = Conta.builder()
                .id(id)
                .saldo(BigDecimal.valueOf(100))
                .saldoSeparado(BigDecimal.ZERO)
                .build();

        when(contaRepository.findById((long) id)).thenReturn(Optional.of(conta));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> {
            contaService.separarValor(id, valorDTO);
        });

        assertEquals("Saldo insuficiente para separar.", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testResgatarSaldoSeparado_Success() {
        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(50))
                .saldoSeparado(BigDecimal.valueOf(100))
                .ativa(false)
                .pixAtivo(false)
                .tipoConta(TipoConta.CORRENTE)
                .build();

        ValorDTO valorDTO = new ValorDTO(BigDecimal.valueOf(50));

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.resgatarSaldoSeparado(1, valorDTO);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100), result.getSaldo());
        assertEquals(BigDecimal.valueOf(50), result.getSaldoSeparado());
    }

    @Test
    public void testResgatarSaldoSeparado_ValorNegativo() {
        long id = 1L;
        ValorDTO valorDTO = new ValorDTO(BigDecimal.valueOf(-100));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.resgatarSaldoSeparado(id, valorDTO);
        });

        assertEquals("O valor não pode ser negativo.", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testResgatarSaldoSeparado_SaldoSeparadoInsuficiente() {
        int id = 1;
        ValorDTO valorDTO = new ValorDTO(BigDecimal.valueOf(500));
        Conta conta = Conta.builder()
                .id(id)
                .saldo(BigDecimal.valueOf(100))
                .saldoSeparado(BigDecimal.valueOf(100))
                .build();

        when(contaRepository.findById((long) id)).thenReturn(Optional.of(conta));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> {
            contaService.resgatarSaldoSeparado(id, valorDTO);
        });

        assertEquals("SaldoSeparado insuficiente para resgatar.", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testTransferir_Success() {
        TransferenciaDTO transferenciaDTO = new TransferenciaDTO(
                1,
                "00000002",
                BigDecimal.valueOf(100));

        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(1000))
                .saldoSeparado(BigDecimal.ZERO)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();

        Conta contaDestino = Conta.builder()
                .id(2)
                .numero("00000002")
                .saldo(BigDecimal.ZERO)
                .saldoSeparado(BigDecimal.ZERO)
                .cliente(cliente)
                .agencia(agencia)
                .tipoConta(TipoConta.CORRENTE)
                .chavesPix(new ArrayList<>())
                .transacoes(new ArrayList<>())
                .build();

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.findByNumero(anyString())).thenReturn(Optional.of(contaDestino));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);
        when(transacaoService.createTransacao(any(CreateTransacaoDTO.class))).thenReturn(new Transacao());

        Conta result = contaService.transferir(transferenciaDTO);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(900), result.getSaldo());
        assertEquals(BigDecimal.valueOf(100), contaDestino.getSaldo());
    }

    @Test
    public void testTransferir_SaldoInsuficiente() {
        int idContaOrigem = 1;
        String numeroContaDestino = "00000002";
        BigDecimal valor = BigDecimal.valueOf(500);

        Conta contaOrigem = Conta.builder()
                .id(idContaOrigem)
                .saldo(BigDecimal.valueOf(100))
                .build();

        when(contaRepository.findById((long) idContaOrigem)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByNumero(numeroContaDestino)).thenReturn(Optional.of(new Conta()));

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO(idContaOrigem, numeroContaDestino, valor);

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> {
            contaService.transferir(transferenciaDTO);
        });

        assertEquals("Saldo insuficiente para transferência", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testTransferir_ContaDestinoNaoEncontrada() {
        int idContaOrigem = 1;
        String numeroContaDestino = "00000002";
        BigDecimal valor = BigDecimal.valueOf(100);

        Conta contaOrigem = Conta.builder()
                .id(idContaOrigem)
                .saldo(BigDecimal.valueOf(200))
                .build();

        when(contaRepository.findById((long) idContaOrigem)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByNumero(numeroContaDestino)).thenReturn(Optional.empty());

        TransferenciaDTO transferenciaDTO = new TransferenciaDTO(idContaOrigem, numeroContaDestino, valor);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            contaService.transferir(transferenciaDTO);
        });

        assertEquals("Conta de numero 00000002 não foi encontrada.", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testRealizarRecarga_Success() {
        RecargaDTO recargaDTO = new RecargaDTO(
                "1234567890",
                BigDecimal.valueOf(50));

        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(1000))
                .saldoSeparado(BigDecimal.ZERO)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);
        when(transacaoService.createTransacao(any(CreateTransacaoDTO.class))).thenReturn(new Transacao());

        Conta result = contaService.realizarRecarga(1, recargaDTO);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(950), result.getSaldo());
    }

    @Test
    public void testRealizarRecarga_SaldoInsuficiente() {
        int idConta = 1;
        String numeroCelular = "123456789";
        BigDecimal valor = BigDecimal.valueOf(500);

        Conta conta = Conta.builder()
                .id(idConta)
                .saldo(BigDecimal.valueOf(100))
                .build();

        when(contaRepository.findById((long) idConta)).thenReturn(Optional.of(conta));

        RecargaDTO recargaDTO = new RecargaDTO(numeroCelular, valor);

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> {
            contaService.realizarRecarga(idConta, recargaDTO);
        });

        assertEquals("Saldo insuficiente para recarga", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testRealizarDeposito_Success() {
        ValorDTO valorDTO = new ValorDTO(BigDecimal.valueOf(100));

        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(1000))
                .saldoSeparado(BigDecimal.ZERO)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);
        when(transacaoService.createTransacao(any(CreateTransacaoDTO.class))).thenReturn(new Transacao());

        Conta result = contaService.realizarDeposito(1, valorDTO);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1100), result.getSaldo());
    }

    @Test
    public void testRealizarDeposito_ValorNegativo() {
        long idConta = 1L;
        BigDecimal valorNegativo = BigDecimal.valueOf(-100);

        ValorDTO valorDTO = new ValorDTO(valorNegativo);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.realizarDeposito(idConta, valorDTO);
        });

        assertEquals("O valor não pode ser negativo.", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testRealizarSaque_Success() {
        ValorDTO valorDTO = new ValorDTO(BigDecimal.valueOf(50));

        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(1000))
                .saldoSeparado(BigDecimal.ZERO)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);
        when(transacaoService.createTransacao(any(CreateTransacaoDTO.class))).thenReturn(new Transacao());

        Conta result = contaService.realizarSaque(1, valorDTO);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(950), result.getSaldo());
    }

    @Test
    public void testRealizarSaque_ValorNegativo() {
        long idConta = 1L;
        BigDecimal valorNegativo = BigDecimal.valueOf(-100);

        ValorDTO valorDTO = new ValorDTO(valorNegativo);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.realizarSaque(idConta, valorDTO);
        });

        assertEquals("O valor não pode ser negativo.", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testRealizarSaque_SaldoInsuficiente() {
        int idConta = 1;
        BigDecimal valorSaque = BigDecimal.valueOf(100);

        Conta conta = Conta.builder()
                .id(idConta)
                .saldo(BigDecimal.valueOf(50))
                .build();

        ValorDTO valorDTO = new ValorDTO(valorSaque);

        when(contaRepository.findById((long) idConta)).thenReturn(Optional.of(conta));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> {
            contaService.realizarSaque(idConta, valorDTO);
        });

        assertEquals("Saldo insuficiente para saque", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testRealizarPagamento_Sucesso() {
        int idConta = 1;
        BigDecimal valorPagamento = BigDecimal.valueOf(100);

        Conta conta = Conta.builder()
                .id(idConta)
                .saldo(BigDecimal.valueOf(200))
                .build();

        Conta contaAtualizada = Conta.builder()
                .id(idConta)
                .saldo(BigDecimal.valueOf(100))
                .build();

        when(contaRepository.findById((long) idConta)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(contaAtualizada);

        ValorDTO valorDTO = new ValorDTO(valorPagamento);

        Conta result = contaService.realizarPagamento(idConta, valorDTO);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100), result.getSaldo());
        verify(contaRepository, times(1)).save(any(Conta.class));
        verify(transacaoService, times(1)).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testRealizarPagamento_ValorNegativo() {
        long idConta = 1L;
        BigDecimal valorNegativo = BigDecimal.valueOf(-100);

        ValorDTO valorDTO = new ValorDTO(valorNegativo);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            contaService.realizarPagamento(idConta, valorDTO);
        });

        assertEquals("O valor não pode ser negativo.", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testRealizarPagamento_SaldoInsuficiente() {
        int idConta = 1;
        BigDecimal valorPagamento = BigDecimal.valueOf(100);

        Conta conta = Conta.builder()
                .id(idConta)
                .saldo(BigDecimal.valueOf(50))
                .build();

        ValorDTO valorDTO = new ValorDTO(valorPagamento);

        when(contaRepository.findById((long) idConta)).thenReturn(Optional.of(conta));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> {
            contaService.realizarPagamento(idConta, valorDTO);
        });

        assertEquals("Saldo insuficiente para efetuar o pagamento.", exception.getMessage());
        verify(contaRepository, never()).save(any(Conta.class));
        verify(transacaoService, never()).createTransacao(any(CreateTransacaoDTO.class));
    }

    @Test
    public void testGetExtrato_Success() {

        List<TransacaoSimpleDTO> transacoes = new ArrayList<>();

        TransacaoSimpleDTO transacao1 = TransacaoSimpleDTO.builder()
                .id(1)
                .dataTransacao(LocalDateTime.now())
                .operacao(Operacao.PAGAMENTO)
                .descricao("Pagamento 1")
                .valor(BigDecimal.valueOf(100))
                .build();
        TransacaoSimpleDTO transacao2 = TransacaoSimpleDTO.builder()
                .id(1)
                .dataTransacao(LocalDateTime.now())
                .operacao(Operacao.PIX)
                .descricao("Pix 1")
                .valor(BigDecimal.valueOf(200))
                .build();
        TransacaoSimpleDTO transacao3 = TransacaoSimpleDTO.builder()
                .id(1)
                .dataTransacao(LocalDateTime.now())
                .operacao(Operacao.RECEBIMENTO_TRANSFERENCIA)
                .descricao("Recebimento Transferencia 1")
                .valor(BigDecimal.valueOf(300))
                .build();

        transacoes.add(transacao1);
        transacoes.add(transacao2);
        transacoes.add(transacao3);

        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(100))
                .saldoSeparado(BigDecimal.ZERO)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();

        when(transacaoService.getLast10Transacoes(conta)).thenReturn(transacoes);
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));

        List<TransacaoSimpleDTO> result = contaService.getExtrato(1);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
    }

    @Test
    public void testGetExtratoPeriodo_Sucesso() {
        int idConta = 1;
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        ExtratoPeriodoDTO extratoPeriodoDTO = new ExtratoPeriodoDTO(startDate, endDate);

        Conta conta = Conta.builder().id(idConta).build();
        List<TransacaoSimpleDTO> transacoes = new ArrayList<>();
        TransacaoSimpleDTO transacaoSimpleDTO = TransacaoSimpleDTO.builder()
                .id(1)
                .dataTransacao(LocalDateTime.now().minusHours(2))
                .operacao(Operacao.PAGAMENTO)
                .descricao("Pagamento 1")
                .valor(BigDecimal.valueOf(100))
                .build();
        transacoes.add(transacaoSimpleDTO);

        when(contaRepository.findById((long) idConta)).thenReturn(Optional.of(conta));
        when(transacaoService.getTransacoesByPeriodo(conta, startDate, endDate)).thenReturn(transacoes);

        List<TransacaoSimpleDTO> result = contaService.getExtratoPeriodo(idConta, extratoPeriodoDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(contaRepository, times(1)).findById((long) idConta);
        verify(transacaoService, times(1)).getTransacoesByPeriodo(conta, startDate, endDate);
    }

    @Test
    public void testRealizarPix_Success() {

        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(1000))
                .saldoSeparado(BigDecimal.ZERO)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();

        Conta conta2 = Conta.builder()
                .id(2)
                .numero("00000002")
                .saldo(BigDecimal.valueOf(100))
                .saldoSeparado(BigDecimal.ZERO)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();


        PixDTO pixDTO = PixDTO.builder()
                .chave("victorvirgolino@gmail.com")
                .valor(BigDecimal.valueOf(200))
                .build();



        Pix pix = Pix.builder()
                .id(1)
                .tipo(TipoChavePix.EMAIL)
                .chave("victorvirgolino@gmail.com")
                .conta(conta2)
                .build();


        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        when(pixService.getPixByChave("victorvirgolino@gmail.com")).thenReturn(pix);


        Conta conta3 = contaService.realizarPix(1, pixDTO);

        assertNotNull(conta3);
        assertEquals(1, conta3.getId());
        assertEquals(BigDecimal.valueOf(800), conta.getSaldo());
    }
}
