package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.agencia.AgenciaResponseDTO;
import acc.br.accenturebank.dto.agencia.CreateAgenciaDTO;
import acc.br.accenturebank.dto.agencia.UpdateAgenciaDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.repository.AgenciaRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    public Agencia createAgencia(CreateAgenciaDTO agenciaDTO) {
        try {
            List<Conta> contas = new ArrayList<>();

            Agencia agencia = new Agencia();

            agencia.setNome(agenciaDTO.getNomeAgencia());
            agencia.setEndereco(agenciaDTO.getEndereco());
            agencia.setTelefone(agenciaDTO.getTelefone());
            agencia.setContas(contas);

            return agenciaRepository.save(agencia);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao criar a agência: ".concat(e.getMessage()), e);
        }

    }


    public Agencia updateAgencia(int id, UpdateAgenciaDTO agenciaDTO) {
        try {
            Agencia agencia = this.getAgenciaById(id);

            String novoNomeAgencia = agenciaDTO.getNomeAgencia();
            String novoEndereco = agenciaDTO.getEndereco();
            String novoTelefone = agenciaDTO.getTelefone();

            if (novoNomeAgencia != null) {
                agencia.setNome(novoNomeAgencia);
            }

            if (novoEndereco != null) {
                agencia.setEndereco(novoEndereco);
            }

            if (novoTelefone != null) {
                agencia.setTelefone(novoTelefone);
            }

            return agenciaRepository.save(agencia);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao atualizar a agência: ".concat(e.getMessage()), e);
        }

    }

    public Agencia getAgenciaById(int id) throws ResourceNotFoundException {
        return agenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agência com id %d não encontrada.".formatted(id)));
    }

    public List<AgenciaResponseDTO> getAllAgencias() {
        return agenciaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public void deleteAgencia(int id) throws ResourceNotFoundException {
        try {
            //verificar se agencia existe
            this.getAgenciaById(id);

            agenciaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao deletar a agência: ".concat(e.getMessage()), e);
        }
    }

    private AgenciaResponseDTO converterParaDTO(Agencia agencia){
        return new AgenciaResponseDTO(agencia);
    }
}