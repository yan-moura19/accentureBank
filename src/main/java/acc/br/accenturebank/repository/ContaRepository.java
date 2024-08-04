package acc.br.accenturebank.repository;

import acc.br.accenturebank.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByNumero(String numeroConta);
}
