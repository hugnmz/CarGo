package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;


// class xu ly password
public class PasswordUtil {

    // so lan lap lai khi hash password
    public static final int ITERATIONS = 100_000;

    // tra ve password da hash dang byte[] (de l    uu vao varbinary)
    public static byte[][] hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // ket hop salt + password
            md.update(salt);
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // lap lai nhieu lan de tang do bao mat
            for (int i = 1; i < ITERATIONS; i++) {
                md.reset();
                hash = md.digest(hash);
            }

            // tra ve byte[] thay vi base64 string
            return new byte[][]{hash, salt};
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // tao salt ngau nhien
    public static byte[] generateSalt() {

        // tao securerandom instance
        SecureRandom random = new SecureRandom();

        // tao mang byte 16 bytes
        byte[] salt = new byte[16];

        // dien so ngau nhien vao mang
        random.nextBytes(salt);

        return salt;
    }

    // kiem tra password co dung khong
    public static boolean verifyPassword(String password, byte[] passwordHash, byte[] passwordSalt) {
        try {
            // hash password voi salt tu database
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(passwordSalt);
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // lap lai nhieu lan nhu khi tao hash
            for (int i = 1; i < ITERATIONS; i++) {
                md.reset();
                hash = md.digest(hash);
            }

            // so sanh hash
            return Arrays.equals(hash, passwordHash);
        } catch (Exception e) {
            throw new util.exception.ApplicationException(util.MessageUtil.getError("error.system"), e);
        }
    }

}
