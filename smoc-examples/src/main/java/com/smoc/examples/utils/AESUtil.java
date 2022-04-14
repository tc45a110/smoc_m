package com.smoc.examples.utils;


import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES AES/CBC/PKCS7Padding
 */
public class AESUtil {
    
    private static final String CHARSET_NAME = "UTF-8";
    private static final String AES_NAME = "AES";
    // 加密模式
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";
    // 密钥 32位
    public static final String KEY = "tr4Bnk9341IjreWh0BeJHU87RUdaVn98";
    // 偏移量 16位
    public static final String IV = "t34v6spUZ3B9Ro7P";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     *
     * @param message
     * @param KEY
     * @param IV
     * @return
     */
    public static String encrypt(String message, String KEY, String IV) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            result = cipher.doFinal(message.getBytes(CHARSET_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(result);
    }

    /**
     * 解密
     *
     * @param message
     * @param KEY
     * @param IV
     * @return
     */
    public static String decrypt(String message, String KEY, String IV) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return new String(cipher.doFinal(Base64.decodeBase64(message)), CHARSET_NAME);
        } catch (Exception e) {
            throw new Exception("解析异常");
        }
    }

    public static void main(String[] args) {
        //身份证号 AES 加密
        String cardNo = AESUtil.encrypt("372523198002165313","fNT44A1FM57Wu8HF5D7C6bHX372699MP","E96nx1xL929I86Te");
        System.out.println(cardNo);
    }
}