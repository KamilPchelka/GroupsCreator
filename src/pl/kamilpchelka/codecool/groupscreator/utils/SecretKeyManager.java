package pl.kamilpchelka.codecool.groupscreator.utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SecretKeyManager {

    public static SecretKey getSecretKey(String pasword) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = (pasword).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-512");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        return new SecretKeySpec(key, "AES");
    }
}
