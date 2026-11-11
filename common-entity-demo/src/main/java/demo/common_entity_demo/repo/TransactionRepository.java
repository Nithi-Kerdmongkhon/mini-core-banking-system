package demo.common_entity_demo.repo;

import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.entity.Transaction;
import demo.common_entity_demo.enumurations.TransactionStatus;
import demo.common_entity_demo.enumurations.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    boolean existsByReference(String reference);

    @Query("""
    select coalesce(sum(t.amount), 0) from Transaction t
    where t.accountId = :account
      and t.type = :type
      and t.status = :status
      and t.createdAt >= :start and t.createdAt < :end
    """)
    BigDecimal sumAmountInRange(@Param("account") Account account,
                                @Param("type") TransactionType type,
                                @Param("status") TransactionStatus status,
                                @Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);

}
