package mini_core_banking_system.demo.implement.account;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.entity.Customer;
import demo.common_entity_demo.enumurations.AccountMapTypeToCode;
import demo.common_entity_demo.enumurations.CommonStatus;
import demo.common_entity_demo.enumurations.CommonStatusAccount;
import demo.common_entity_demo.repo.AccountRepository;
import demo.common_entity_demo.repo.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import mini_core_banking_system.demo.dto.account.RequestCreateAccountDTO;
import mini_core_banking_system.demo.implement.customer.CustomerServiceImpl;
import mini_core_banking_system.demo.interfaces.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final AccountNumberServiceImpl accountNumberService;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    @Value("${bank.branch-code}")
    private String branchCode;

    @Transactional
    public ResponseBodyDTO open(RequestCreateAccountDTO json, Long idCustomer){

        String type = json.getAccountType();
        logger.info(type);
        AccountMapTypeToCode accountType;
        try {
            accountType = AccountMapTypeToCode.fromCode(type);
        } catch (IllegalArgumentException e) {
            return new ResponseBodyDTO(CommonStatus.INVALID_ACCOUNT_TYPE);
        }

        Optional<Customer> customer = customerRepository.findById(idCustomer);
        if (customer.isEmpty()){
            return new ResponseBodyDTO(CommonStatus.NOT_FOUND_CUSTOMER);
        }
        Customer cm = customer.get();

        String no = accountNumberService.generate(branchCode,accountType.getPrefix());
        Account account = new Account();
        account.setAccountNo(no);
        if (json.getBalance()== null || json.getBalance().compareTo(BigDecimal.ZERO) < 0){
            return new ResponseBodyDTO(CommonStatus.INVALID_BALANCE);
        }
        account.setBalance(json.getBalance());
        account.setStatus(CommonStatusAccount.ACTIVE);
        account.setCustomerId(cm);
        account.setAccountType(json.getAccountType());
        account.setOpenedAt(LocalDateTime.now());

        accountRepository.save(account);



        return new ResponseBodyDTO(CommonStatus.SUCCESS);
    }

}
