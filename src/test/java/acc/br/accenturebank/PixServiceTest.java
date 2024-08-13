package acc.br.accenturebank;

import acc.br.accenturebank.dto.pix.PixResponseDTO;
import acc.br.accenturebank.dto.pix.UpdatePixDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Cliente;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.model.enums.TipoChavePix;
import acc.br.accenturebank.model.enums.TipoConta;
import acc.br.accenturebank.repository.PixRepository;
import acc.br.accenturebank.service.PixService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PixServiceTest {

    @Mock
    private PixRepository pixRepository;

    @InjectMocks
    private PixService pixService;

    private Pix pix;

    private Conta conta;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        conta = Conta.builder()
                .id(1)
                .numero("123456")
                .saldo(BigDecimal.valueOf(1000))
                .tipoConta(TipoConta.CORRENTE)
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

        Agencia agencia = Agencia.builder()
                .id(1)
                .nome("Test Agencia")
                .endereco("Test Endereco")
                .telefone("8333412973")
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

        pix = Pix.builder()
                .id(1)
                .tipo(TipoChavePix.EMAIL)
                .chave("teste@teste.com")
                .conta(conta)
                .build();
    }

    @Test
    public void testCreatePix_Success() {
        when(pixRepository.save(any(Pix.class))).thenReturn(pix);
        Pix createdPix = pixService.createPix(pix);
        assertNotNull(createdPix);
        assertEquals("teste@teste.com", createdPix.getChave());
        verify(pixRepository, times(1)).save(any(Pix.class));
    }

    @Test
    public void testCreatePix_InvalidChave() {
        pix.setChave("chaveInvalida");
        assertThrows(IllegalArgumentException.class, () -> pixService.createPix(pix));
    }

    @Test
    public void testUpdatePix_Success() {
        UpdatePixDTO updatePixDTO = UpdatePixDTO.builder()
                .tipo(TipoChavePix.TELEFONE)
                .chave("+5511999999999")
                .build();

        when(pixRepository.findById(pix.getId())).thenReturn(Optional.of(pix));
        when(pixRepository.save(any(Pix.class))).thenReturn(pix);

        Pix updatedPix = pixService.updatePix(pix.getId(), updatePixDTO);

        assertNotNull(updatedPix);
        assertEquals(TipoChavePix.TELEFONE, updatedPix.getTipo());
        assertEquals("+5511999999999", updatedPix.getChave());
        verify(pixRepository, times(1)).findById(pix.getId());
        verify(pixRepository, times(1)).save(any(Pix.class));
    }

    @Test
    public void testGetPixById_NotFound() {
        when(pixRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pixService.getPixById(pix.getId()));
    }

    @Test
    public void testGetPixByChave_Success() {
        when(pixRepository.findByChave(pix.getChave())).thenReturn(Optional.of(pix));
        Pix foundPix = pixService.getPixByChave(pix.getChave());
        assertNotNull(foundPix);
        assertEquals(pix.getId(), foundPix.getId());
        verify(pixRepository, times(1)).findByChave(pix.getChave());
    }

    @Test
    public void testGetPixByChave_NotFound() {
        when(pixRepository.findByChave(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pixService.getPixByChave("inexistente@teste.com"));
    }

    @Test
    public void testGetAllPix() {
        when(pixRepository.findAll()).thenReturn(List.of(pix));
        List<PixResponseDTO> pixList = pixService.getAllPix();
        assertFalse(pixList.isEmpty());
        assertEquals(1, pixList.size());
        verify(pixRepository, times(1)).findAll();
    }

    @Test
    public void testDeletePix_Success() {
        when(pixRepository.findById(pix.getId())).thenReturn(Optional.of(pix));
        doNothing().when(pixRepository).deleteById(pix.getId());
        pixService.deletePix(pix.getId());
        verify(pixRepository, times(1)).findById(pix.getId());
        verify(pixRepository, times(1)).deleteById(pix.getId());
    }
}
