/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author admin
 */
// class xử lí password
public class PasswordUtil {

    public static final int ITERATIONS = 100_000;

    // trả về chuỗi password đã hash dạng Base64
    public static String[] hashPassword(String password, byte[] salt) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Kết hợp salt + password
            md.update(salt); 
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

         
            for (int i = 1; i < ITERATIONS; i++) {
                md.reset();
                hash = md.digest(hash);
            }

            String hashB64 = Base64.getEncoder().encodeToString(hash);
            String saltB64 = Base64.getEncoder().encodeToString(salt);
            return new String[]{hashB64, saltB64};
        } catch (Exception e) {

            throw new RuntimeException("Error hashing password", e);
        }
    }

    // tạo salt ngẫu nhiên
    public static byte[] generateSalt() {

        // tạo secureRandom instance;
        SecureRandom random = new SecureRandom();

        // tạo mảng byte 16 bytes
        byte[] salt = new byte[16];

        // điền số ngẫu nhiên vào mảng
        random.nextBytes(salt);

        return salt;
    }

    // So sánh mật khẩu có đúng không, so sánh mk nhập vào với mk đã hash
    public static boolean verifyPassword(String password, byte[] passwordHash,
            byte[] passwordSalt) {

        // Hash mật khẩu người dùng nhập với salt từ database
        String[] hashedPassword = hashPassword(password, passwordSalt);

        // So sánh hash mới với hash đã lưu trong database
        return hashedPassword[0].equals(new String(passwordHash));
    }
}
