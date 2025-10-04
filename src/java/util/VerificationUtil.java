/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 *
 * @author admin
 */

// xác minh email
public class VerificationUtil {
    private static final SecureRandom random = new SecureRandom();
    
    // sinh ma 6 chu so ngau nhien
    public static String generateNumbericCode(){
        int number = random.nextInt(1_000_000);
        return String.format("%06d", number);
    }
    
    // tinh time het han
    public static LocalDateTime expiryAfterMinutes(int minutes){
        return LocalDateTime.now().plusMinutes(minutes);
    }
}
