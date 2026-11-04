package demo.common_entity_demo.repo;

import demo.common_entity_demo.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerRepository extends JpaRepository<Ledger,Long> {

}
