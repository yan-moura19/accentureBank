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
    public void testRealizarSaque_SaldoInsuficiente() {
        ValorDTO valorDTO = new ValorDTO(BigDecimal.valueOf(200));

        Conta conta = Conta.builder()
                .id(1)
                .numero("00000001")
                .saldo(BigDecimal.valueOf(100))
                .saldoSeparado(BigDecimal.ZERO)
                .tipoConta(TipoConta.CORRENTE)
                .agencia(agencia)
                .cliente(cliente)
                .build();

        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> contaService.realizarSaque(1, valorDTO));
        assertEquals("Saldo insuficiente para saque", exception.getMessage());
    }

    @Test
    public void testRelatorioTransacoes_Success() {

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
