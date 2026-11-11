package demo.common_entity_demo.entity;

import demo.common_entity_demo.enumurations.EntryTypeEnum;
import demo.common_entity_demo.enumurations.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="LEDGER")
@Getter
@Setter
public class Ledger extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "accountId")
    private Account accountId;

    @Enumerated(EnumType.STRING)
    private EntryTypeEnum entryType;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
