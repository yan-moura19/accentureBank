package acc.br.accenturebank.repository;

import acc.br.accenturebank.model.Pix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PixRepository extends JpaRepository<Pix, Integer> {
    Optional<Pix> findByChave(String chave);
}
