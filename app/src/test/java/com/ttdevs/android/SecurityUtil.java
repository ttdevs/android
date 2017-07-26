package com.ttdevs.android;

import org.java_websocket.util.Base64;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtil {
    @Test
    public void testCSDN() {
        String data = "aaa";
        System.out.println(DESEncrypt(data));
        System.out.println(Base64.encodeBytes("wuhuaihu".getBytes()));
    }

    private static byte[] aes_key = "123456789012.csdn.mobile".getBytes();

    public static String DESEncrypt(String paramString) {
        try {
            byte[] data = paramString.getBytes("UTF-8");
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(aes_key, "DESede");
            Cipher localCipher = Cipher.getInstance("DESede");
            localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec);
            byte[] input = localCipher.doFinal(data);
            // byte[] encode = Base64.encode(input, Base64.NO_WRAP);
            return Base64.encodeBytes(input);
//            byte[] encode = Base64.encodeBytes(input);
//            String str = new String(encode, "UTF-8");
//            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String DESDecrypt(String paramString) {
//        try {
//            byte[] arrayOfByte = Base64.decode(paramString.getBytes("UTF-8"), 2);
//            SecretKeySpec localSecretKeySpec = new SecretKeySpec(aes_key, "DESede");
//            Cipher localCipher = Cipher.getInstance("DESede");
//            localCipher.init(Cipher.DECRYPT_MODE, localSecretKeySpec);
//            byte[] data = localCipher.doFinal(arrayOfByte);
//            String str = new String(data, "UTF-8");
//            return str;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return "";
    }

}