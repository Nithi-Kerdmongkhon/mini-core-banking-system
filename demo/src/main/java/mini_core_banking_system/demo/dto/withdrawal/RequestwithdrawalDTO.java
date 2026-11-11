package mini_core_banking_system.demo.dto.withdrawal;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestwithdrawalDTO {
    private BigDecimal amount;
    private String reference;
}
