package acc.br.accenturebank.repository;

import acc.br.accenturebank.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
