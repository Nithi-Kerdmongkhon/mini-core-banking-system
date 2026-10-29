package mini_core_banking_system.demo.dto.deposit;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestDepositDTO {
    private BigDecimal amount;
}
