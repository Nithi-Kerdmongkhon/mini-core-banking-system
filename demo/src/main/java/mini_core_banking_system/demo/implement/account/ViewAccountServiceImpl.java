package mini_core_banking_system.demo.implement.account;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.enumurations.AccountType;
import demo.common_entity_demo.enumurations.CommonStatus;
import demo.common_entity_demo.repo.AccountRepository;
import demo.common_entity_demo.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import mini_core_banking_system.demo.common.CommonService;
import mini_core_banking_system.demo.dto.account.ResponseViewAccountDTO;
import mini_core_banking_system.demo.interfaces.account.ViewAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewAccountServiceImpl extends CommonService implements ViewAccountService {
    private static final Logger logger = LoggerFactory.getLogger(ViewAccountService.class);
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public ResponseBodyDTO handle(String accountNo) {

        if (accountNo == null || !accountNo.matches("\\d{13}")) {
            return new ResponseBodyDTO(CommonStatus.INVALID_REQUEST);
        }
        Optional<Account>  account = accountRepository.findByAccountNo(accountNo);


        if (account.isEmpty()){
            logger.warn("View customer: not found, accountNo={}", accountNo);
            return new ResponseBodyDTO(CommonStatus.NOT_FOUND_ACCOUNT);
        }
        Account account1 = account.get();
        logger.info("Account Detail : {}",account1);

//        Optional<Customer> customer = customerRepository.findById(account1.getCustomerId().getId());
//        Customer customer1 = customer.get();
        String firstName = account1.getCustomerId() == null ? "" : account1.getCustomerId().getFirstName();
        String lastName = account1.getCustomerId() == null ? "" : account1.getCustomerId().getLastName();

        String type = AccountType.mapPrefix(account1.getAccountType()).getPrefix();

        ResponseViewAccountDTO dto = new ResponseViewAccountDTO();
        dto.setAccountNo(account1.getAccountNo());
        dto.setAccountType(type);
        dto.setName(firstName == "" ? "ไม่ทราบเจ้าของบัญชี" : firstName + " " + lastName);
        dto.setBalance(account1.getBalance());
        dto.setCreateDat(account1.getOpenedAt());


        return new ResponseBodyDTO(dto, CommonStatus.SUCCESS);

    }
}
