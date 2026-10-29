package mini_core_banking_system.demo.interfaces.deposit;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import mini_core_banking_system.demo.dto.customer.RequestAddCustomerDTO;
import mini_core_banking_system.demo.dto.deposit.RequestDepositDTO;

public interface DepositService {
    ResponseBodyDTO handle(String accountNo,RequestDepositDTO json);
}
