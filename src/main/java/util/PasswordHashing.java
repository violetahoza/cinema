package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashing {
    public static String hashPassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hashBytes = md.digest(password.getBytes());//computes the hash value of the password using the SHA-256 algorithm
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
