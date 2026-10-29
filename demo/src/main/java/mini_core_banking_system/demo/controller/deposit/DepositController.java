package mini_core_banking_system.demo.controller.deposit;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.enumurations.CommonStatus;
import lombok.RequiredArgsConstructor;
import mini_core_banking_system.demo.dto.deposit.RequestDepositDTO;
import mini_core_banking_system.demo.implement.customer.CustomerServiceImpl;
import mini_core_banking_system.demo.interfaces.deposit.DepositService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class DepositController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final DepositService depositService;

    @PostMapping("/accounts/{accountNo}/deposits")
    public ResponseBodyDTO deposit(@PathVariable("accountNo")String accountNo, @RequestBody RequestDepositDTO json){
        try {
            ResponseBodyDTO res = depositService.handle(accountNo,json);
            return res;
        } catch (Exception e) {
            logger.error("Error from view-customer service : {}", e.getMessage(), e);
            return new ResponseBodyDTO(CommonStatus.ERROR_EXCEPTION_OCCURRED);
        }
    }

}
