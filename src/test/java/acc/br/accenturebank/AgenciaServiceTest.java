package acc.br.accenturebank;

import acc.br.accenturebank.dto.agencia.AgenciaResponseDTO;
import acc.br.accenturebank.dto.agencia.CreateAgenciaDTO;
import acc.br.accenturebank.dto.agencia.UpdateAgenciaDTO;
import acc.br.accenturebank.service.AgenciaService;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.repository.AgenciaRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgenciaServiceTest {

    @Mock
    private AgenciaRepository agenciaRepository;

    @InjectMocks
    private AgenciaService agenciaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAgencia() {
        CreateAgenciaDTO dto = new CreateAgenciaDTO();
        dto.setNomeAgencia("Agência Teste");
        dto.setEndereco("Rua Teste, 123");
        dto.setTelefone("123456789");

        Agencia agencia = new Agencia();
        agencia.setNome(dto.getNomeAgencia());
        agencia.setEndereco(dto.getEndereco());
        agencia.setTelefone(dto.getTelefone());

        when(agenciaRepository.save(any(Agencia.class))).thenReturn(agencia);

        Agencia createdAgencia = agenciaService.createAgencia(dto);

        assertNotNull(createdAgencia);
        assertEquals("Agência Teste", createdAgencia.getNome());
        verify(agenciaRepository, times(1)).save(any(Agencia.class));
    }

    @Test
    public void testUpdateAgencia() {
        int id = 1;
        UpdateAgenciaDTO dto = new UpdateAgenciaDTO();
        dto.setNomeAgencia("Novo Nome");
        dto.setEndereco("Novo Endereço");
        dto.setTelefone("987654321");

        Agencia existingAgencia = new Agencia();
        existingAgencia.setId(id);
        existingAgencia.setNome("Agência Antiga");
        existingAgencia.setEndereco("Endereço Antigo");
        existingAgencia.setTelefone("123456789");

        when(agenciaRepository.findById(id)).thenReturn(Optional.of(existingAgencia));
        when(agenciaRepository.save(existingAgencia)).thenReturn(existingAgencia);

        Agencia updatedAgencia = agenciaService.updateAgencia(id, dto);

        assertNotNull(updatedAgencia);
        assertEquals("Novo Nome", updatedAgencia.getNome());
        assertEquals("Novo Endereço", updatedAgencia.getEndereco());
        assertEquals("987654321", updatedAgencia.getTelefone());
        verify(agenciaRepository, times(1)).save(existingAgencia);
    }

    @Test
    public void testGetAgenciaById() {
        int id = 1;
        Agencia agencia = new Agencia();
        agencia.setId(id);

        when(agenciaRepository.findById(id)).thenReturn(Optional.of(agencia));

        Agencia result = agenciaService.getAgenciaById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(agenciaRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAgenciaByIdNotFound() {
        int id = 1;

        when(agenciaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> agenciaService.getAgenciaById(id));
    }

    @Test
    public void testGetAllAgencias() {
        List<Conta> lista = new ArrayList<>();
        Agencia agencia1 = new Agencia(
                1,
                "Agencia 1",
                "Rua Teste 1",
                "83987660717",
                lista
        );
        Agencia agencia2 = new Agencia(
                2,
                "Agencia 2",
                "Rua Teste 2",
                "83987660718",
                lista
        );
        List<Agencia> agencias = Arrays.asList(agencia1, agencia2);

        when(agenciaRepository.findAll()).thenReturn(agencias);

        List<AgenciaResponseDTO> result = agenciaService.getAllAgencias();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(agenciaRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteAgencia() {
        int id = 1;
        Agencia agencia = new Agencia();
        agencia.setId(id);

        when(agenciaRepository.findById(id)).thenReturn(Optional.of(agencia));

        assertDoesNotThrow(() -> agenciaService.deleteAgencia(id));
        verify(agenciaRepository, times(1)).deleteById(id);
    }
}

