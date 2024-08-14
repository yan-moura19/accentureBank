package acc.br.accenturebank;

import acc.br.accenturebank.controller.AgenciaController;
import acc.br.accenturebank.dto.agencia.AgenciaResponseDTO;
import acc.br.accenturebank.dto.agencia.CreateAgenciaDTO;
import acc.br.accenturebank.dto.agencia.UpdateAgenciaDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.service.AgenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AgenciaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AgenciaService agenciaService;

    @InjectMocks
    private AgenciaController agenciaController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(agenciaController).build();
    }

    @Test
    public void testCreateAgencia() throws Exception {
        CreateAgenciaDTO createAgenciaDTO = CreateAgenciaDTO.builder()
                .nomeAgencia("Agencia Central")
                .endereco("Rua Principal, 123")
                .telefone("(83)3341-2987")
                .build();

        Agencia agencia = Agencia.builder()
                .id(1)
                .nome("Agencia Central")
                .endereco("Rua Principal, 123")
                .telefone("(83)3341-2987")
                .contas(new ArrayList<>())
                .build();

        when(agenciaService.createAgencia(any(CreateAgenciaDTO.class))).thenReturn(agencia);

        String jsonRequest = objectMapper.writeValueAsString(createAgenciaDTO);

        mockMvc.perform(post("/agencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Agencia Central")))
                .andExpect(jsonPath("$.endereco", is("Rua Principal, 123")))
                .andExpect(jsonPath("$.telefone", is("(83)3341-2987")));

        ArgumentCaptor<CreateAgenciaDTO> captor = ArgumentCaptor.forClass(CreateAgenciaDTO.class);
        verify(agenciaService).createAgencia(captor.capture());

        // Verificar que o DTO capturado é o mesmo que o DTO enviado na requisição
        CreateAgenciaDTO capturedDTO = captor.getValue();
        assertEquals(capturedDTO.getNomeAgencia(), createAgenciaDTO.getNomeAgencia());
        assertEquals(capturedDTO.getEndereco(), createAgenciaDTO.getEndereco());
        assertEquals(capturedDTO.getTelefone(), createAgenciaDTO.getTelefone());
    }

    @Test
    public void testUpdateAgencia() throws Exception {
        UpdateAgenciaDTO updateAgenciaDTO = UpdateAgenciaDTO.builder()
                .nomeAgencia("Agencia Central")
                .endereco("Rua Principal, 123")
                .telefone("(83)3341-2987")
                .build();

        Agencia agencia = Agencia.builder()
                .id(1)
                .nome("Agencia Central2")
                .endereco("Rua Principal, 124")
                .telefone("(83)3341-2981")
                .contas(new ArrayList<>())
                .build();

        when(agenciaService.updateAgencia(eq(1), any(UpdateAgenciaDTO.class))).thenReturn(agencia);

        String jsonRequest = objectMapper.writeValueAsString(updateAgenciaDTO);

        mockMvc.perform(put("/agencias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Agencia Central2")))
                .andExpect(jsonPath("$.endereco", is("Rua Principal, 124")))
                .andExpect(jsonPath("$.telefone", is("(83)3341-2981")));

        ArgumentCaptor<UpdateAgenciaDTO> captor = ArgumentCaptor.forClass(UpdateAgenciaDTO.class);
        verify(agenciaService).updateAgencia(eq(1), captor.capture());

        // Verificar que o DTO capturado é o mesmo que o DTO enviado na requisição
        UpdateAgenciaDTO capturedDTO = captor.getValue();
        assertEquals(capturedDTO.getNomeAgencia(), updateAgenciaDTO.getNomeAgencia());
        assertEquals(capturedDTO.getEndereco(), updateAgenciaDTO.getEndereco());
        assertEquals(capturedDTO.getTelefone(), updateAgenciaDTO.getTelefone());
    }

    @Test
    public void testGetAllAgencias() throws Exception {
        List<AgenciaResponseDTO> agencias = new ArrayList<>();
        agencias.add(new AgenciaResponseDTO(Agencia.builder()
                .id(1)
                .nome("Agencia Central")
                .endereco("Rua Principal, 123")
                .telefone("(83)3341-2987")
                .contas(new ArrayList<>())
                .build()));
        agencias.add(new AgenciaResponseDTO(Agencia.builder()
                .id(2)
                .nome("Agencia Norte")
                .endereco("Avenida Norte, 456")
                .telefone("(83)3341-2988")
                .contas(new ArrayList<>())
                .build()));

        when(agenciaService.getAllAgencias()).thenReturn(agencias);

        mockMvc.perform(get("/agencias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is("Agencia Central")))
                .andExpect(jsonPath("$[1].nome", is("Agencia Norte")))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void testGetAgenciaById() throws Exception {
        Agencia agencia = Agencia.builder()
                .id(1)
                .nome("Agencia Central")
                .endereco("Rua Principal, 123")
                .telefone("(83)3341-2987")
                .contas(new ArrayList<>())
                .build();

        AgenciaResponseDTO agenciaResponseDTO = new AgenciaResponseDTO(agencia);

        when(agenciaService.getAgenciaById(1)).thenReturn(agencia);

        mockMvc.perform(get("/agencias/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Agencia Central")))
                .andExpect(jsonPath("$.endereco", is("Rua Principal, 123")))
                .andExpect(jsonPath("$.telefone", is("(83)3341-2987")));
    }

    @Test
    public void testDeleteAgencia() throws Exception {
        mockMvc.perform(delete("/agencias/1"))
                .andExpect(status().isOk());

        verify(agenciaService).deleteAgencia(1);
    }
}
