package acc.br.accenturebank.repository;

import acc.br.accenturebank.model.Conta;
import acc.br.accenturebank.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Integer> {
    List<Transacao> findTop10ByContaOrderByDataTransacaoDesc(Conta conta);
    List<Transacao> findByContaAndDataTransacaoBetween(Conta conta, LocalDateTime startDate, LocalDateTime endDate);
}
