package util;

import java.security.SecureRandom;
import java.time.LocalDateTime;

// class xu ly xac minh email
public class VerificationUtil {
    // securerandom de tao so ngau nhien
    private static final SecureRandom random = new SecureRandom();
    
    // sinh ma 6 chu so ngau nhien
    public static String generateNumbericCode(){
        int number = random.nextInt(1_000_000);
        return String.format("%06d", number);
    }
    
    // tinh thoi gian het han
    public static LocalDateTime expiryAfterMinutes(int minutes){
        return LocalDateTime.now().plusMinutes(minutes);
    }
}
