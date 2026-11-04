package demo.common_entity_demo.entity;

import demo.common_entity_demo.enumurations.CommonStatusAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="ACCOUNT")
@Getter
@Setter
public class Account extends BaseEntity{
    private String accountNo;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "customerId")
    private Customer customerId;

    private String accountType;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private CommonStatusAccount status;

    private LocalDateTime openedAt;
    private LocalDateTime updatedAt;

}
