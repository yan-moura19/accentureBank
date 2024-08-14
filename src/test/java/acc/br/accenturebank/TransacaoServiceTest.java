package acc.br.accenturebank;

import acc.br.accenturebank.dto.transacao.CreateTransacaoDTO;
import acc.br.accenturebank.dto.transacao.TransacaoResponseDTO;
import acc.br.accenturebank.dto.transacao.TransacaoSimpleDTO;
import acc.br.accenturebank.dto.transacao.UpdateTransacaoDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Transacao;
import acc.br.accenturebank.model.enums.Operacao;
import acc.br.accenturebank.repository.TransacaoRepository;
import acc.br.accenturebank.service.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    private Transacao transacao;
    private Conta conta;
    private Agencia agencia;
    private Cliente cliente;

    @BeforeEach
    void setUp() {

        agencia = new Agencia();
        cliente = new Cliente();

        conta = new Conta();
        conta.setId(1);
        conta.setAgencia(agencia);
        conta.setCliente(cliente);

        transacao = new Transacao();
        transacao.setId(1);
        transacao.setConta(conta);
        transacao.setDataTransacao(LocalDateTime.now());
        transacao.setOperacao(Operacao.DEPOSITO);
        transacao.setDescricao("Descrição");
        transacao.setValor(new BigDecimal("100.00"));
    }

    @Test
    void createTransacao() {
        CreateTransacaoDTO createTransacaoDTO = new CreateTransacaoDTO();
        createTransacaoDTO.setConta(conta);
        createTransacaoDTO.setDataTransacao(transacao.getDataTransacao());
        createTransacaoDTO.setOperacao(transacao.getOperacao());
        createTransacaoDTO.setDescricao(transacao.getDescricao());
        createTransacaoDTO.setValor(transacao.getValor());

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

        Transacao result = transacaoService.createTransacao(createTransacaoDTO);

        assertNotNull(result);
        assertEquals(transacao.getId(), result.getId());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void getTransacaoById() {
        when(transacaoRepository.findById(transacao.getId())).thenReturn(Optional.of(transacao));

        Transacao result = transacaoService.getTransacaoById(transacao.getId());

        assertNotNull(result);
        assertEquals(transacao.getId(), result.getId());
        verify(transacaoRepository, times(1)).findById(transacao.getId());
    }

    @Test
    void getLast10Transacoes() {
        when(transacaoRepository.findTop10ByContaOrderByDataTransacaoDesc(conta)).thenReturn(Arrays.asList(transacao));

        List<TransacaoSimpleDTO> result = transacaoService.getLast10Transacoes(conta);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(transacaoRepository, times(1)).findTop10ByContaOrderByDataTransacaoDesc(conta);
    }

    @Test
    void getTransacoesByPeriodo() {
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(10).withNano(0);
        LocalDateTime endDateTime = LocalDateTime.now().withNano(0);

        when(transacaoRepository.findByContaAndDataTransacaoBetween(eq(conta), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(transacao));

        List<TransacaoSimpleDTO> result = transacaoService.getTransacoesByPeriodo(conta, startDateTime.toLocalDate(), endDateTime.toLocalDate());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(transacaoRepository, times(1)).findByContaAndDataTransacaoBetween(eq(conta), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getAllTransacoes() {



        when(transacaoRepository.findAll()).thenReturn(Arrays.asList(transacao));

        List<TransacaoResponseDTO> result = transacaoService.getAllTransacoes();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(transacaoRepository, times(1)).findAll();
    }

    @Test
    void updateTransacao() {
        UpdateTransacaoDTO updateTransacaoDTO = new UpdateTransacaoDTO();
        updateTransacaoDTO.setDataTransacao(transacao.getDataTransacao());
        updateTransacaoDTO.setOperacao(transacao.getOperacao());
        updateTransacaoDTO.setDescricao(transacao.getDescricao());
        updateTransacaoDTO.setValor(transacao.getValor());
        updateTransacaoDTO.setConta(conta);

        when(transacaoRepository.findById(transacao.getId())).thenReturn(Optional.of(transacao));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

        Transacao result = transacaoService.updateTransacao(transacao.getId(), updateTransacaoDTO);

        assertNotNull(result);
        assertEquals(transacao.getId(), result.getId());
        verify(transacaoRepository, times(1)).findById(transacao.getId());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void deleteTransacao() {
        when(transacaoRepository.findById(transacao.getId())).thenReturn(Optional.of(transacao));

        transacaoService.deleteTransacao(transacao.getId());

        verify(transacaoRepository, times(1)).findById(transacao.getId());
        verify(transacaoRepository, times(1)).deleteById(transacao.getId());
    }
}
