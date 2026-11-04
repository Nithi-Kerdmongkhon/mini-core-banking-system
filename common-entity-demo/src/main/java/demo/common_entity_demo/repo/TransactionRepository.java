package demo.common_entity_demo.repo;

import demo.common_entity_demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    boolean existsByReference(String reference);

}
