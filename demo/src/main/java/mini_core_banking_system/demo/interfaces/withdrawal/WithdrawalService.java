package mini_core_banking_system.demo.interfaces.withdrawal;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import mini_core_banking_system.demo.dto.deposit.RequestDepositDTO;
import mini_core_banking_system.demo.dto.withdrawal.RequestwithdrawalDTO;

public interface WithdrawalService {
    ResponseBodyDTO handle(String accountNo, RequestwithdrawalDTO json);
}
