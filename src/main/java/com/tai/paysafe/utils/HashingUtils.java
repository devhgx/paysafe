package com.tai.paysafe.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtils{
    private static String SHA_256 = "SHA-256";
    public static String hash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(SHA_256);
        byte[] encodedHash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
