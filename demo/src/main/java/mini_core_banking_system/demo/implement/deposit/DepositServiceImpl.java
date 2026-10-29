package mini_core_banking_system.demo.implement.deposit;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.enumurations.CommonStatus;
import demo.common_entity_demo.enumurations.CommonStatusAccount;
import demo.common_entity_demo.repo.AccountRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import mini_core_banking_system.demo.common.BusinessExceptionService;
import mini_core_banking_system.demo.common.CommonService;
import mini_core_banking_system.demo.dto.deposit.RequestDepositDTO;
import mini_core_banking_system.demo.interfaces.deposit.DepositService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl extends CommonService implements DepositService {

    private static final Logger logger = LoggerFactory.getLogger(DepositService.class);
    private static final int MAX_SCALE = 2;

    private final AccountRepository accountRepository;


    @Override
    @Transactional
    public ResponseBodyDTO handle(String accountNo,RequestDepositDTO json) {

        validateAmount(json.getAmount());
        Account account = validateAccount(accountNo);


        String referenceNo = generateReference();



        return new ResponseBodyDTO(referenceNo,CommonStatus.SUCCESS);
    }
    public Account validateAccount(String accountNo){
        if (accountNo == null || accountNo.isBlank()){
        throw new BusinessExceptionService(CommonStatus.ACCOUNT_IS_NULL_OR_EMPTY);
        }
        Account account = accountRepository.findByAccountNo(accountNo).orElseThrow(() -> new BusinessExceptionService(CommonStatus.NOT_FOUND_ACCOUNT));

        if (account.getStatus().equals(CommonStatusAccount.FROZEN) || account.getStatus().equals(CommonStatusAccount.CLOSED)){
            throw new BusinessExceptionService(CommonStatus.THIS_ACCOUNT_UNABLE_TO_DEPOSIT_MONEY);
        }

        return account;

    }
    public void validateAmount(BigDecimal amount){
        if (amount == null) {
            throw new BusinessExceptionService(CommonStatus.AMOUNT_IS_NULL);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessExceptionService(CommonStatus.AMOUNT_MUST_BE_GREATER_THAN_ZERO);
        }
        if (amount.stripTrailingZeros().scale() > MAX_SCALE) {
            throw new BusinessExceptionService(CommonStatus.AMOUNT_INVALID_PRECISION);
        }

    }

}
