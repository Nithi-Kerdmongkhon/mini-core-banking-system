package mini_core_banking_system.demo.implement.withdrawal;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.entity.AuditLog;
import demo.common_entity_demo.entity.Ledger;
import demo.common_entity_demo.entity.Transaction;
import demo.common_entity_demo.enumurations.*;
import demo.common_entity_demo.repo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import mini_core_banking_system.demo.common.BusinessExceptionService;
import mini_core_banking_system.demo.common.CommonService;
import mini_core_banking_system.demo.dto.withdrawal.RequestwithdrawalDTO;
import mini_core_banking_system.demo.interfaces.withdrawal.WithdrawalService;
import mini_core_banking_system.demo.utils.CommonUtilityService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl extends CommonService implements WithdrawalService {

    private final CommonUtilityService commonUtilityService;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public ResponseBodyDTO handle(String accountNo, RequestwithdrawalDTO json) {

        commonUtilityService.validateAmount(json.getAmount());
        Account account = commonUtilityService.validateAccount(accountNo);
        commonUtilityService.validateBalance(json.getAmount(),account);

        String referenceNo = generateReferenceWITH(json.getReference());

        if (transactionRepository.existsByReference(referenceNo)) {
            throw new BusinessExceptionService(CommonStatus.DUPLICATE_TRANSACTION);
        }

        commonUtilityService.validateDailyLimit(account, json.getAmount());

        Transaction transaction = new Transaction();
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAccountId(account);
        transaction.setAmount(json.getAmount());
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setReference(referenceNo);
        transaction.setStatus(TransactionStatus.SUCCESS);

        transaction = transactionRepository.save(transaction);

        //ใช้ subtract(-) ในการลดค่า Balance ลง
        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(json.getAmount());
        account.setBalance(balanceAfter);
        accountRepository.save(account);


        Ledger ledger = new Ledger();
        ledger.setTransaction(transaction);
        ledger.setAccountId(account);
        ledger.setEntryType(EntryTypeEnum.CREDIT);
        ledger.setAmount(json.getAmount());
        ledger.setBalanceBefore(balanceBefore);
        ledger.setBalanceAfter(balanceAfter);
        ledger.setCreatedAt(LocalDateTime.now());
        ledgerRepository.save(ledger);

        AuditLog audit = new AuditLog();
        audit.setCreatedAt(LocalDateTime.now());
        audit.setAction(TransactionType.WITHDRAW);
        audit.setEntityType(EntityType.ACCOUNT);
        audit.setEntityId(accountNo);
        audit.setActor("SYSTEM");
        audit.setDetail("amount=" + json.getAmount() + ", ref=" + referenceNo + ", balanceBefore=" + balanceBefore + ", balanceAfter=" + balanceAfter);
        auditLogRepository.save(audit);




        return new ResponseBodyDTO(CommonStatus.SUCCESS);
    }
}
