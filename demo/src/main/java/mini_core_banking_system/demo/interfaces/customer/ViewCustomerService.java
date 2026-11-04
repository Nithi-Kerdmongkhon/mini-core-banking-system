package mini_core_banking_system.demo.interfaces.customer;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import mini_core_banking_system.demo.dto.customer.RequestAddCustomerDTO;

public interface ViewCustomerService {
    ResponseBodyDTO handle(Long id);
}
