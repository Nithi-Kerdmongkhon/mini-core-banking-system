package demo.common_entity_demo.entity;

import demo.common_entity_demo.enumurations.CommonStatusAccount;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="CUSTOMER")
@Getter
@Setter
public class Customer extends BaseEntity {

    private String customerNo;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private CommonStatusAccount status;


}
