package mini_core_banking_system.demo.controller.customer;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.enumurations.CommonStatus;
import mini_core_banking_system.demo.dto.customer.RequestAddCustomerDTO;
import mini_core_banking_system.demo.implement.customer.CustomerServiceImpl;
import mini_core_banking_system.demo.interfaces.customer.CustomerService;
import mini_core_banking_system.demo.interfaces.customer.ViewCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ViewCustomerService viewCustomerService;

    @PostMapping("/customers")
    public ResponseBodyDTO createCustomer(@RequestBody RequestAddCustomerDTO json){
        try {
            ResponseBodyDTO res = customerService.handle(json);
            return res;
        } catch (Exception e) {
            logger.error("Error from accept-job service :   "+e.getMessage());
            e.printStackTrace();
            return new ResponseBodyDTO(CommonStatus.ERROR_EXCEPTION_OCCURRED);
        }

    }

    @GetMapping("/customers/{id}")
    public ResponseBodyDTO viewCustomer(@PathVariable("id") Long id) {
        try {
            ResponseBodyDTO res = viewCustomerService.handle(id);
            return res;
        } catch (Exception e) {
            logger.error("Error from view-customer service : {}", e.getMessage(), e);
            return new ResponseBodyDTO(CommonStatus.ERROR_EXCEPTION_OCCURRED);
        }
    }
}
