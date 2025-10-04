/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 *
 * @author admin
 */
// class xử lí password
public class PasswordUtil {

    public static final int ITERATIONS = 100_000;

    // tra ve password da hash dang byte[] (de luu vao VARBINARY)
    public static byte[][] hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Kết hợp salt + password
            md.update(salt);
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            for (int i = 1; i < ITERATIONS; i++) {
                md.reset();
                hash = md.digest(hash);
            }

            // Tra ve byte[] thay vi Base64 string
            return new byte[][]{hash, salt};
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

        // dien so ngau nhien vao mang
        random.nextBytes(salt);

        return salt;
    }

    public static boolean verifyPassword(String password, byte[] passwordHash, byte[] passwordSalt) {
        try {
            // Hash password với salt từ database
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(passwordSalt);
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            for (int i = 1; i < ITERATIONS; i++) {
                md.reset();
                hash = md.digest(hash);
            }

            // So sánh hash
            return Arrays.equals(hash, passwordHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
