package mini_core_banking_system.demo.common;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
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

    public String generateReference(){
        return "DEP-" + UUID.randomUUID();
    }
}
