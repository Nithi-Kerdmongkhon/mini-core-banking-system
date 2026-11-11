package demo.common_entity_demo.entity;

import demo.common_entity_demo.enumurations.CommonStatusAccount;
import demo.common_entity_demo.enumurations.TransactionStatus;
import demo.common_entity_demo.enumurations.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="TRANSACTIONS")
@Getter
@Setter
public class Transaction extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "accountId")
    private Account accountId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;
    private String reference;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
