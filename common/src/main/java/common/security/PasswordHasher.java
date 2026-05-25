package common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    private PasswordHasher() {}

    public static String hashPassword(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD2");

            byte[] bytes = md.digest(password.getBytes());

            StringBuilder builder = new StringBuilder();

            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("MD2 algorithm not found", e);
        }
    }
}
