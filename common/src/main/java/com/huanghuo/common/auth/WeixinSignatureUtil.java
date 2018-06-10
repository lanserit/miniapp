package com.huanghuo.common.auth;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class WeixinSignatureUtil {
    /**
     * 计算字符串的 sha1 哈希值
     * */
    public static String sha1(String str) {
        return compute(str, "SHA-1");
    }

    /**
     * 计算字符串的 md5 哈希值
     * */
    public static String md5(String str) {
        return compute(str, "MD5");
    }

    public static String compute(String str, String algorithm) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(str.getBytes("utf-8"));
            return byteArrayToHexString(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static String signature(String data, String session_key) {
        return sha1(data + session_key);
    }

    public static String decryptUtf8(String sessionKey, String encryptedData, String ivStr) {
       return decrypt(sessionKey, encryptedData, ivStr, "utf8");
    }

    public static String decrypt(String sessionKey, String encryptedData, String ivStr, String charsetName) {
        byte[] bData = Base64.decodeBase64(encryptedData);
        byte[] bSkey = Base64.decodeBase64(sessionKey);
        byte[] bIv = Base64.decodeBase64(ivStr);
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(bSkey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(bIv);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] originData = cipher.doFinal(bData);
            return new String(originData, charsetName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
