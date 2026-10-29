package mini_core_banking_system.demo.dto.account;

import demo.common_entity_demo.enumurations.CommonStatus;
import demo.common_entity_demo.enumurations.CommonStatusAccount;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestCreateAccountDTO {
    private String accountType;
    private BigDecimal balance;
}
