package com.houarizegai.learnsql.java.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hashing {
    
    // This function return the hash using MD5 algorithm
    public static String getHash(String plainPassword) {
        StringBuffer hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainPassword.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format
            hashedPassword = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                hashedPassword.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            
        } catch (NoSuchAlgorithmException ex) {
                System.out.println("Error msg : " + ex.getMessage());
        }

        return hashedPassword.toString();
    }

}
