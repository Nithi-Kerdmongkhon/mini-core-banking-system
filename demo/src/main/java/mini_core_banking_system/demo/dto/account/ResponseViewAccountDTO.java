package mini_core_banking_system.demo.dto.account;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ResponseViewAccountDTO {
    private String name;
    private String accountNo;
    private String accountType;
    private LocalDateTime createDat;
    private BigDecimal balance;

}
