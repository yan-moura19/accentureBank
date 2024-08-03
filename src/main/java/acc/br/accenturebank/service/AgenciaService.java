package acc.br.accenturebank.service;

import acc.br.accenturebank.model.Agencia;
import acc.br.accenturebank.repository.AgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    public List<Agencia> getAllAgencias() {
        return agenciaRepository.findAll();
    }

    public Agencia getAgenciaById(int id) {
        return agenciaRepository.findById(id).orElse(null);
    }

    public Agencia createAgencia(Agencia agencia) {
        return agenciaRepository.save(agencia);
    }

    public Agencia updateAgencia(int id, Agencia agencia) {
        agencia.setIdAgencia(id);
        return agenciaRepository.save(agencia);
    }

    public void deleteAgencia(int id) {
        agenciaRepository.deleteById(id);
    }
}