package acc.br.accenturebank.repository;

import acc.br.accenturebank.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
