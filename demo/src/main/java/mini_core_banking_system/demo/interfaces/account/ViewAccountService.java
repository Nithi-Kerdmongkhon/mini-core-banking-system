package mini_core_banking_system.demo.interfaces.account;

import demo.common_entity_demo.dto.ResponseBodyDTO;

public interface ViewAccountService {
    ResponseBodyDTO handle(String accountNo);
}
