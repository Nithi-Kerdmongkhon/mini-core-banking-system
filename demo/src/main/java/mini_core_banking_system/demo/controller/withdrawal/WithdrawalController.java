package mini_core_banking_system.demo.controller.withdrawal;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.enumurations.CommonStatus;
import lombok.RequiredArgsConstructor;
import mini_core_banking_system.demo.common.BusinessExceptionService;
import mini_core_banking_system.demo.dto.withdrawal.RequestwithdrawalDTO;
import mini_core_banking_system.demo.implement.customer.CustomerServiceImpl;
import mini_core_banking_system.demo.interfaces.withdrawal.WithdrawalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WithdrawalController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final WithdrawalService withdrawalService;

    @PostMapping("/accounts/{accountNo}/withdrawals")
    public ResponseBodyDTO withdrawal (@PathVariable(name = "accountNo") String accountNo,@RequestBody RequestwithdrawalDTO json){

        try {
            ResponseBodyDTO res = withdrawalService.handle(accountNo,json);
            return res;

        }catch (BusinessExceptionService e) {
            logger.warn("Business error from deposit service : {}", e.getMessage());
            return new ResponseBodyDTO(e.getStatus());
        }catch (Exception e) {
            logger.error("Error from deposit service : {}", e.getMessage(), e);
            return new ResponseBodyDTO(CommonStatus.ERROR_EXCEPTION_OCCURRED);
        }
    }
}
