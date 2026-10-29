package demo.common_entity_demo.repo;

import demo.common_entity_demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByAccountNo(String accountNo);
}
