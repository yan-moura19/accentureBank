package acc.br.accenturebank.service;

import acc.br.accenturebank.dto.CreateAgenciaDTO;
import acc.br.accenturebank.dto.UpdateAgenciaDTO;
import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.repository.AgenciaRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    public Agencia createAgencia(CreateAgenciaDTO agenciaDTO) {
        try {
            List<Conta> contas = new ArrayList<>();

            Agencia agencia = new Agencia();

            agencia.setNomeAgencia(agenciaDTO.getNomeAgencia());
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
                agencia.setNomeAgencia(novoNomeAgencia);
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

    public List<Agencia> getAllAgencias() {
        return agenciaRepository.findAll();
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
}