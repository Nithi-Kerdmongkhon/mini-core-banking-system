package demo.common_entity_demo.entity;

import demo.common_entity_demo.enumurations.EntityType;
import demo.common_entity_demo.enumurations.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="AUDIT_LOG")
@Setter
@Getter
public class AuditLog extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private TransactionType action;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;
    private String entityId;
    private String actor;
    private String detail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
