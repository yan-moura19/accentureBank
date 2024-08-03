package acc.br.accenturebank.service;

import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public List<Conta> getAllContas() {
        return contaRepository.findAll();
    }

    public Conta getContaById(Long id) {
        return contaRepository.findById(id).orElse(null);
    }

    public Conta createConta(Conta conta) {
        return contaRepository.save(conta);
    }

    public Conta updateConta(int id, Conta conta) {
        conta.setIdConta(id);
        return contaRepository.save(conta);
    }

    public void deleteConta(Long id) {
        contaRepository.deleteById(id);
    }
}