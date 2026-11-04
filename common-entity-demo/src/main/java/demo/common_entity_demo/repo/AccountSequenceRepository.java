package demo.common_entity_demo.repo;

import demo.common_entity_demo.entity.AccountSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountSequenceRepository extends JpaRepository<AccountSequence, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM AccountSequence s WHERE s.seqKey = :key")
    Optional<AccountSequence> findByKeyForUpdate(@Param("key") String key);

}
