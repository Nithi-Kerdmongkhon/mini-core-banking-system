package mini_core_banking_system.demo.implement.account;

import demo.common_entity_demo.entity.AccountSequence;
import demo.common_entity_demo.repo.AccountSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountNumberServiceImpl {

    private final AccountSequenceRepository seqRepo;

    public String generate(String branchCode,String accountType){

        String key = branchCode + "-" + accountType;

        //ดึง Sequence จาก Database ถ้าไม่มีจะสร้างใหม่
        AccountSequence seq = seqRepo.findByKeyForUpdate(key).orElseGet(() -> {
            AccountSequence s = new AccountSequence();
            s.setSeqKey(key);
            s.setCurrentValue(0);
            return seqRepo.saveAndFlush(s);
        });

        long next = seq.getCurrentValue()+1;
        if (next > 999_999) {
            throw new IllegalStateException("Sequence exhausted for " + key);
        }

        seq.setCurrentValue(next);

        //สร้าง Account Number
        //String.format(แม่แบบ,ค่าที่ต้องการแทนที่ แม่แบบ)
        // ประกอบเลขบัญชี (ยังไม่รวม check digit) = สาขา(3) + ประเภท(3) + เลขรัน(6 หลัก เติม 0 ข้างหน้า)
        // เช่น "189" + "100" + "000001" = "189100000001"
        // %s = ข้อความ, %06d = เลขจำนวนเต็ม 6 หลัก เติม 0 ด้านหน้า
        String body = String.format("%s%s%06d", branchCode, accountType, next);
        return body + checkDigit(body);

    }

    //เช็ค digit คือการคำนวณเลขต่อท้ายจากเลข 12 หลักอ่านจากขวา -> ซ้าย
    //คูณสลับกัน (คูณ, ไม่คูณ, คูณ, ...)บวกทุกหลักรวมกัน และเข้าสูตร "(10 - (sum % 10)) % 10;" หาว่าต้องบวกอีกเท่าไรให้ลงท้าย 0
    //เช่น (ผลรวม = 17) --> (17 % 10 = 7) --> (10 - 7 = 3 )--> (3 % 10 = 3 )
    // % 10 บรรทัดสุดท้ายมีไว้ดักกรณีเดียวคือผลรวมลงท้าย 0 เช่น ผลรวม 20 → 20 % 10 = 0 → 10 - 0 = 10 → 10 % 10 = 0 (check digit ต้องเป็น 0 ไม่ใช่ 10)

    private int checkDigit(String body) {
        int sum = 0;
        boolean doubleIt = true;
        for (int i = body.length() - 1; i >= 0; i--) {
            int d = body.charAt(i) - '0';
            if (doubleIt) {
                d *= 2;
                if (d > 9) d -= 9;
            }
            sum += d;
            doubleIt = !doubleIt;
        }
        return (10 - (sum % 10)) % 10;
    }

}
