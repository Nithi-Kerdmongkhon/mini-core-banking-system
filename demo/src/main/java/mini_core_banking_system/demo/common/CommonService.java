package mini_core_banking_system.demo.common;

import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.enumurations.CommonStatus;
import demo.common_entity_demo.enumurations.CommonStatusAccount;
import demo.common_entity_demo.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CommonService {


    //nullOrEmpty
    public boolean isNullOrEmpty(String text) {
        return text == null || text.length() == 0;
    }

    //Email Pattern
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private  static Pattern PATTERN = Pattern.compile(EMAIL_REGEX);
    public static boolean isValidEmail(String email){
        if (email == null){
            return false;
        }
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();

    }

    public String generateReferenceDEP(String reference){
        return "DEP-" + reference;
    }
    public String generateReferenceWITH(String reference){
        return "WITH-" + reference;
    }
}
