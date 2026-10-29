package mini_core_banking_system.demo.interfaces.account;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import mini_core_banking_system.demo.dto.account.RequestCreateAccountDTO;

public interface AccountService {
    ResponseBodyDTO open(RequestCreateAccountDTO type, Long idCustomer);
}
