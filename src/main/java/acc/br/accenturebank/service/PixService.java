package acc.br.accenturebank.service;

import acc.br.accenturebank.model.Pix;
import acc.br.accenturebank.repository.PixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PixService {

    @Autowired
    private PixRepository pixRepository;

    public List<Pix> getAllPix() {
        return pixRepository.findAll();
    }

    public Pix getPixById(int id) {
        return pixRepository.findById(id).orElse(null);
    }

    public Pix createPix(Pix pix) {
        return pixRepository.save(pix);
    }

    public Pix updatePix(int id, Pix pix) {
        pix.setId(id);
        return pixRepository.save(pix);
    }

    public void deletePix(int id) {
        pixRepository.deleteById(id);
    }
}
