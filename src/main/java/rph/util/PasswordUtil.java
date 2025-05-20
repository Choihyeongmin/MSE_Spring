package rph.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;

public class PasswordUtil {  //salt and pepper

    @Value("${PASSWORD_PEPPER}")
    private static final String PEPPER = System.getenv("PASSWORD_PEPPER"); //.env

    // Salt 생성
    public static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 해시 처리
    public static String hashPassword(String rawPassword, String salt) {
        try {
            String toHash = rawPassword + salt + PEPPER;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(toHash.getBytes());
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}