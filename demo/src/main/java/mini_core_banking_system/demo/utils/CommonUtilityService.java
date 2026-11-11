package mini_core_banking_system.demo.utils;

import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.enumurations.CommonStatus;
import demo.common_entity_demo.enumurations.CommonStatusAccount;
import demo.common_entity_demo.enumurations.TransactionStatus;
import demo.common_entity_demo.enumurations.TransactionType;
import demo.common_entity_demo.repo.AccountRepository;
import demo.common_entity_demo.repo.TransactionRepository;
import lombok.RequiredArgsConstructor;
import mini_core_banking_system.demo.common.BusinessExceptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class CommonUtilityService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private static final int MAX_SCALE = 2;
    private static final ZoneId ZONE = ZoneId.of("Asia/Bangkok");
    @Value("${bank.withdrawal.daily-limit}")
    private BigDecimal dailyLimit;

    public Account validateAccount(String accountNo){
        if (accountNo == null || accountNo.isBlank()){
            throw new BusinessExceptionService(CommonStatus.ACCOUNT_IS_NULL_OR_EMPTY);
        }
        Account account = accountRepository.findByAccountNoForUpdate(accountNo).orElseThrow(() -> new BusinessExceptionService(CommonStatus.NOT_FOUND_ACCOUNT));

        if (account.getStatus() != CommonStatusAccount.ACTIVE){
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

    public void validateBalance(BigDecimal amount,Account account) {

        Account account1 = validateAccount(account.getAccountNo());
        BigDecimal balance = account1.getBalance();

        //.compareTo ใช้กับการเทียบระหว่าง BigDecimal 2 ตัว
        // -1 = ค่าฝั่งซ้ายน้อยกว่าฝั่งขวา
        // 0 = ค่าทั้งสองฝั่งเท่ากัน
        // 1	ค่าฝั่งซ้ายมากกว่า
        if (balance.compareTo(amount) < 0 ){
            throw new BusinessExceptionService(CommonStatus.INSUFFICIENT_BALANCE);
        }

    }

    public void validateDailyLimit(Account account, BigDecimal amount){

        LocalDate today = LocalDate.now(ZONE);
        BigDecimal usedToday = transactionRepository.sumAmountInRange(
                account, TransactionType.WITHDRAW, TransactionStatus.SUCCESS,
                today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        if (usedToday.add(amount).compareTo(dailyLimit) > 0) {
            throw new BusinessExceptionService(CommonStatus.DAILY_LIMIT_EXCEEDED);
        }

    }
}
