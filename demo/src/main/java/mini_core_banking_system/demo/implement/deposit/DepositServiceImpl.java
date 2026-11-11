package mini_core_banking_system.demo.implement.deposit;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.entity.AuditLog;
import demo.common_entity_demo.entity.Ledger;
import demo.common_entity_demo.entity.Transaction;
import demo.common_entity_demo.enumurations.*;
import demo.common_entity_demo.repo.AccountRepository;
import demo.common_entity_demo.repo.AuditLogRepository;
import demo.common_entity_demo.repo.LedgerRepository;
import demo.common_entity_demo.repo.TransactionRepository;
import mini_core_banking_system.demo.utils.CommonUtilityService;
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
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl extends CommonService implements DepositService {

    private static final Logger logger = LoggerFactory.getLogger(DepositService.class);

    private final CommonUtilityService commonUtilityService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LedgerRepository ledgerRepository;
    private final AuditLogRepository auditLogRepository;


    @Override
    @Transactional
    public ResponseBodyDTO handle(String accountNo,RequestDepositDTO json) {

        commonUtilityService.validateAmount(json.getAmount());
        Account account = commonUtilityService.validateAccount(accountNo);

        String referenceNo = generateReferenceDEP(json.getReference());

        if (transactionRepository.existsByReference(referenceNo)) {
            throw new BusinessExceptionService(CommonStatus.DUPLICATE_TRANSACTION);
        }

        Transaction transaction = new Transaction();
        transaction.setAccountId(account);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(json.getAmount());
        transaction.setReference(referenceNo);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCreatedAt(LocalDateTime.now());

        transaction = transactionRepository.save(transaction);

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(json.getAmount());
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
        audit.setAction(TransactionType.DEPOSIT);
        audit.setEntityType(EntityType.ACCOUNT);
        audit.setEntityId(accountNo);
        audit.setActor("SYSTEM");
        audit.setDetail("amount=" + json.getAmount() + ", ref=" + referenceNo + ", balanceBefore=" + balanceBefore + ", balanceAfter=" + balanceAfter);
        auditLogRepository.save(audit);

        return new ResponseBodyDTO(referenceNo,CommonStatus.SUCCESS);
    }



}
