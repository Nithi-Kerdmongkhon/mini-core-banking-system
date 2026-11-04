package mini_core_banking_system.demo;

import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.entity.Customer;
import demo.common_entity_demo.entity.Ledger;
import demo.common_entity_demo.enumurations.CommonStatus;
import demo.common_entity_demo.enumurations.CommonStatusAccount;
import demo.common_entity_demo.repo.*;
import mini_core_banking_system.demo.common.BusinessExceptionService;
import mini_core_banking_system.demo.dto.deposit.RequestDepositDTO;
import mini_core_banking_system.demo.interfaces.deposit.DepositService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test: ยิง deposit พร้อมกันหลาย thread บนบัญชีเดียว
 *
 * ข้อควรระวัง:
 *  - ห้าม annotate คลาสนี้ด้วย @Transactional (แต่ละ thread ต้อง commit จริง)
 *  - ต้องรันกับ DB สำหรับ test เท่านั้น (H2 / Testcontainers) เพราะ @BeforeEach ลบข้อมูลทิ้ง
 *    ห้ามชี้ไป DB ของ dev/prod
 */
@SpringBootTest
@ActiveProfiles("test")
class DepositConcurrencyIT {

    private static final String ACCOUNT_NO = "1892000000025";
    private static final BigDecimal AMOUNT = new BigDecimal("100.00");
    private static final int THREADS = 10;

    @Autowired private DepositService depositService;
    @Autowired private AccountRepository accountRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private LedgerRepository ledgerRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private CustomerRepository customerRepository;

    // ---------- Scenario 1: 10 รายการ reference ต่างกัน ----------
//    @BeforeEach
//    void setUp() {
//        // ลบ child -> parent
//        ledgerRepository.deleteAll();
//        auditLogRepository.deleteAll();
//        transactionRepository.deleteAll();
//        accountRepository.deleteAll();
//        customerRepository.deleteAll();
//
//        Customer customer = new Customer();
//        customer.setFirstName("Test");
//        customer.setLastName("Deposit");
//        customer.setEmail("TestDeposite@gmail.com");
//        customer.setPhone("0999999999");
//        customer.setStatus(CommonStatusAccount.ACTIVE);
//        customer.setCustomerNo(UUID.randomUUID().toString());
//        customer.setCreatedAt(LocalDateTime.now());
//
//        customer = customerRepository.save(customer);
//
//        Account account = new Account();
//        account.setAccountNo(ACCOUNT_NO);
//        account.setBalance(BigDecimal.ZERO);
//        account.setStatus(CommonStatusAccount.ACTIVE);
//        account.setCustomerId(customer);
//        account.setAccountType("SAVING");
//        account.setOpenedAt(LocalDateTime.now());
//        accountRepository.save(account);
//    }

    @Test
    void tenDifferentReferences_allSucceed_andBalanceIsExactly1000() throws Exception {
        List<RequestDepositDTO> requests = new ArrayList<>();
        for (int i = 1; i <= THREADS; i++) {
            requests.add(request("DEP-CONC-" + i));
        }

        Map<CommonStatus, Long> result = runConcurrently(requests);

        assertThat(result).containsEntry(CommonStatus.SUCCESS, 10L);
        assertThat(result).hasSize(1);

        // balance ต้องเป็น 10 x 100 = 1000 พอดี (ถ้าไม่ lock จะน้อยกว่านี้ = lost update)
        assertThat(currentBalance()).isEqualByComparingTo("1000.00");

        assertThat(transactionRepository.count()).isEqualTo(10);
        assertThat(ledgerRepository.count()).isEqualTo(10);
        assertThat(auditLogRepository.count()).isEqualTo(10);

        // ledger ต้องต่อเนื่องเป็นลูกโซ่: balanceAfter = 100, 200, ..., 1000 ไม่มีค่าซ้ำ
        List<Integer> balancesAfter = ledgerRepository.findAll().stream()
                .map(Ledger::getBalanceAfter)
                .map(BigDecimal::intValueExact)
                .sorted()
                .collect(Collectors.toList());
        assertThat(balancesAfter)
                .containsExactly(100, 200, 300, 400, 500, 600, 700, 800, 900, 1000);
    }

    // ---------- Scenario 2: 10 รายการ reference เดียวกัน ----------

    @Test
    void tenSameReference_onlyOneSucceeds_nineAreDuplicate_balanceIncreasedOnce() throws Exception {
        List<RequestDepositDTO> requests = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            requests.add(request("DEP-CONC-SAME"));
        }

        Map<CommonStatus, Long> result = runConcurrently(requests);

        assertThat(result).containsEntry(CommonStatus.SUCCESS, 1L);
        assertThat(result).containsEntry(CommonStatus.DUPLICATE_TRANSACTION, 9L);

        assertThat(currentBalance()).isEqualByComparingTo("100.00");
        assertThat(transactionRepository.count()).isEqualTo(1);
        assertThat(ledgerRepository.count()).isEqualTo(1);
        assertThat(auditLogRepository.count()).isEqualTo(1);
    }

    // ---------- Scenario 3: ผสม 5 reference ส่งซ้ำอย่างละ 2 ครั้ง = 10 รายการ ----------

    @Test
    void fiveReferencesSentTwiceEach_fiveSucceed_fiveDuplicate_balanceIs500() throws Exception {
        List<RequestDepositDTO> requests = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            requests.add(request("f31ec890-d698-11f1-a500-a3940c7a539l" + i));
            requests.add(request("f31ec890-d698-11f1-a500-a3940c7a539l" + i));
        }

        Map<CommonStatus, Long> result = runConcurrently(requests);

        assertThat(result).containsEntry(CommonStatus.SUCCESS, 5L);
        assertThat(result).containsEntry(CommonStatus.DUPLICATE_TRANSACTION, 5L);

        assertThat(currentBalance()).isEqualByComparingTo("500.00");
        assertThat(transactionRepository.count()).isEqualTo(5);
        assertThat(ledgerRepository.count()).isEqualTo(5);
        assertThat(auditLogRepository.count()).isEqualTo(5);
    }

    // ---------- helpers ----------

    private RequestDepositDTO request(String reference) {
        RequestDepositDTO dto = new RequestDepositDTO();
        dto.setAmount(AMOUNT);
        dto.setReference(reference);
        return dto;
    }

    private BigDecimal currentBalance() {
        return accountRepository.findByAccountNo(ACCOUNT_NO).orElseThrow().getBalance();
    }

    /**
     * ยิงทุก request พร้อมกัน (ทุก thread รอที่ latch แล้วปล่อยพร้อมกัน)
     * คืนจำนวนผลลัพธ์แยกตาม status: SUCCESS หรือรหัส error ที่ได้
     */
    private Map<CommonStatus, Long> runConcurrently(List<RequestDepositDTO> requests) throws Exception {
        int n = requests.size();
        ExecutorService pool = Executors.newFixedThreadPool(n);
        CountDownLatch ready = new CountDownLatch(n);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<CommonStatus>> futures = new ArrayList<>();

        try {
            for (RequestDepositDTO req : requests) {
                futures.add(pool.submit(() -> {
                    ready.countDown();
                    start.await();
                    try {
                        depositService.handle(ACCOUNT_NO, req);
                        return CommonStatus.SUCCESS;
                    } catch (BusinessExceptionService e) {
                        return e.getStatus();
                    }
                }));
            }

            ready.await(10, TimeUnit.SECONDS);
            start.countDown();

            List<CommonStatus> results = new ArrayList<>();
            for (Future<CommonStatus> f : futures) {
                results.add(f.get(30, TimeUnit.SECONDS));
            }
            return results.stream()
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        } finally {
            pool.shutdownNow();
        }
    }
}