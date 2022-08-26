package com.business.alarm.worker.util;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
	
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
            e.printStackTrace();
        }
        return hash;
    }
      
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
     ** java生成随机数字10位数
     **
     * * @param length[生成随机数的长度]
     *@return
     *     
     */
    public static String getRandom(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }



}
