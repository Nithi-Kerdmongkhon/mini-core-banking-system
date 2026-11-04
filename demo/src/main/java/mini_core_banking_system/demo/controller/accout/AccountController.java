package mini_core_banking_system.demo.controller.accout;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.enumurations.CommonStatus;
import lombok.RequiredArgsConstructor;
import mini_core_banking_system.demo.dto.account.RequestCreateAccountDTO;
import mini_core_banking_system.demo.dto.customer.RequestAddCustomerDTO;
import mini_core_banking_system.demo.implement.customer.CustomerServiceImpl;
import mini_core_banking_system.demo.interfaces.account.AccountService;
import mini_core_banking_system.demo.interfaces.account.ViewAccountService;
import mini_core_banking_system.demo.interfaces.customer.ViewCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final AccountService accountService;
    private final ViewAccountService viewAccountService;

    @PostMapping("/customers/{customerId}/accounts")
    public ResponseBodyDTO createAccount(@PathVariable Long customerId, @RequestBody RequestCreateAccountDTO request){
        try {
            ResponseBodyDTO res = accountService.open(request,customerId);
            return res;
        } catch (Exception e) {
            logger.error("Error from craete account service :   "+e.getMessage());
            e.printStackTrace();
            return new ResponseBodyDTO(CommonStatus.ERROR_EXCEPTION_OCCURRED);
        }

    }

    @GetMapping("/accounts/{accountNo}")
    public ResponseBodyDTO viewAccount(@PathVariable String accountNo){
        try {
            ResponseBodyDTO res = viewAccountService.handle(accountNo);
            return  res;
        } catch (Exception e) {
            logger.error("Error from view account service :   "+e.getMessage());
            e.printStackTrace();
            return new ResponseBodyDTO(CommonStatus.ERROR_EXCEPTION_OCCURRED);
        }

    }
}
