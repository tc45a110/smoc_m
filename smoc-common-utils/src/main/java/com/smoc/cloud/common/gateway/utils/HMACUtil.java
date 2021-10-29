package com.smoc.cloud.common.gateway.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 签名、验签算法  支持md5_HMAC、sha256_HMAC、sha384_HMAC、sha512_HMAC
 **/
@Slf4j
public class HMACUtil {

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * sha256_HMAC加密
     *
     * @param message 消息
     * @param key     秘钥
     * @return 加密后字符串
     */
    public static String sha256_HMAC_sign(String message, String key) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.trim().getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.trim().getBytes("UTF-8"));
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error("Error HmacSHA256：{}", e.getMessage());
        }
        return hash;
    }

    /**
     * sha384_HMAC加密
     *
     * @param message 消息
     * @param key     秘钥
     * @return 加密后字符串
     */
    public static String sha384_HMAC_sign(String message, String key) {
        String hash = "";
        try {
            Mac sha384_HMAC = Mac.getInstance("HmacSHA384");
            SecretKeySpec secret_key = new SecretKeySpec(key.trim().getBytes("UTF-8"), "HmacSHA384");
            sha384_HMAC.init(secret_key);
            byte[] bytes = sha384_HMAC.doFinal(message.trim().getBytes("UTF-8"));
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error("Error HmacSHA384：{}", e.getMessage());
        }
        return hash;
    }

    /**
     * sha256_HMAC加密
     *
     * @param message 消息
     * @param key     秘钥
     * @return 加密后字符串
     */
    public static String sha512_HMAC_sign(String message, String key) {
        String hash = "";
        try {
            Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(key.trim().getBytes("UTF-8"), "HmacSHA512");
            sha512_HMAC.init(secret_key);
            byte[] bytes = sha512_HMAC.doFinal(message.trim().getBytes("UTF-8"));
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error("Error HmacSHA512：{}", e.getMessage());
        }
        return hash;
    }

    /**
     * md5_HMAC加密
     *
     * @param message 消息
     * @param key     秘钥
     * @return 加密后字符串
     */
    public static String md5_HMAC_sign(String message, String key) {
        String hash = "";
        try {
            Mac md5_HMAC = Mac.getInstance("HmacMD5");
            SecretKeySpec secret_key = new SecretKeySpec(key.trim().getBytes("UTF-8"), "HmacMD5");
            md5_HMAC.init(secret_key);
            byte[] bytes = md5_HMAC.doFinal(message.trim().getBytes("UTF-8"));
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error("Error HmacMD5：{}", e.getMessage());
        }
        return hash;
    }

    /**
     * sha256_HMAC 验证签名
     *
     * @param message   消息
     * @param signature 签名
     * @param key       秘钥
     * @return Boolean
     */
    public static Boolean verifySignatureSha256_HMAC(String message, String signature, String key) {

        String sign = sha256_HMAC_sign(message, key);

        return sign.equals(signature.trim());
    }

    /**
     * sha384_HMAC 验证签名
     *
     * @param message   消息
     * @param key       秘钥
     * @param signature 签名
     * @return Boolean
     */
    public static Boolean verifySignatureSha384_HMAC(String message, String signature, String key) {

        String sign = sha384_HMAC_sign(message, key);

        return sign.equals(signature.trim());
    }

    /**
     * sha512_HMAC 验证签名
     *
     * @param message   消息
     * @param signature 签名
     * @param key       秘钥
     * @return Boolean
     */
    public static Boolean verifySignatureSha512_HMAC(String message, String signature, String key) {

        String sign = sha512_HMAC_sign(message, key);

        return sign.equals(signature.trim());
    }

    /**
     * md5_HMAC 验证签名
     *
     * @param message   消息
     * @param signature 签名
     * @param key       秘钥
     * @return Boolean
     */
    public static Boolean verifySignatureMd5_HMAC(String message, String signature, String key) {

        String sign = md5_HMAC_sign(message, key);

        return sign.equals(signature.trim());
    }

    /**
     * 签名
     *
     * @param message
     * @param key
     * @param signStyle
     * @return
     */
    public static String sign(String message, String key, String signStyle) {
        switch (signStyle) {
            case "sha256_HMAC":
                return sha256_HMAC_sign(message, key);
            case "sha384_HMAC":
                return sha384_HMAC_sign(message, key);
            case "sha512_HMAC":
                return sha512_HMAC_sign(message, key);
            default:
                return md5_HMAC_sign(message, key);

        }
    }

    /**
     * 验签
     *
     * @param message
     * @param signature
     * @param key
     * @param signStyle
     * @return
     */
    public static boolean verifySign(String message, String signature, String key, String signStyle) {
        switch (signStyle) {
            case "sha256_HMAC":
                return verifySignatureSha256_HMAC(message, signature, key);
            case "sha384_HMAC":
                return verifySignatureSha384_HMAC(message, signature, key);
            case "sha512_HMAC":
                return verifySignatureSha512_HMAC(message, signature, key);
            default:
                return verifySignatureMd5_HMAC(message, signature, key);

        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(sign("koala-task", "W*7h%8X@KVv904!au&trO^e", "md5_HMAC"));//W*7h%8X@KVv904!au&trO^e
        System.out.println(sign("koala-task", "B%4#1F9@5va*8A!5EX0^Cd#*nC#@we83dSV", "sha256_HMAC"));//B%4#1F9@5va*8A!5EX0^Cd#*nC#@we83dSV
        System.out.println(sign("koala-task", "B%4#1F9@5va*8A!5EX0^Cd#*nC#@we83dSV", "sha384_HMAC"));
        System.out.println(sign("koala-task", "B%4#1F9@5va*8A!5EX0^Cd#*nC#@we83dSV", "sha512_HMAC"));
        System.out.println(verifySign("koala-task", "3a51d3977b7e03fb0702ed466ae3ebbd", "W*7h%8X@KVv904!au&trO^e", "md5_HMAC"));
        System.out.println(verifySign("koala-task", "71bcf4c6c2dc5301ab8e76abbbd5d743400b50440b3c424c7abbf2d826e3e6e4", "B%4#1F9@5va*8A!5EX0^Cd#*nC#@we83dSV", "sha256_HMAC"));
        System.out.println(verifySign("koala-task", "cae2923a4ae9ed033899d7941e888de6b35fcd33b7640ed59c5f2401a62721a99b13f462cbe80a6174ea683a7b439e82", "B%4#1F9@5va*8A!5EX0^Cd#*nC#@we83dSV", "sha384_HMAC"));
        System.out.println(verifySign("koala-task", "6938e4a069977b07a5e6f65bbdeb285ee3521b51a49f9012ae6aec36dedf958c4c6ed7de916610e19b62f48dc575ab3a2b1b1d69901b2d076b4d6e7ec2482337", "B%4#1F9@5va*8A!5EX0^Cd#*nC#@we83dSV", "sha512_HMAC"));
    }
}