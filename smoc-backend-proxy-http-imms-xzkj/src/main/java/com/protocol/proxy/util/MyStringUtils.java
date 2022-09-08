package com.protocol.proxy.util;
import sun.misc.BASE64Encoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class MyStringUtils {
    static Pattern pattern = Pattern.compile("\\$\\{\\d{1,2}}");

    public static boolean checkTemplate(String template) {
        String[] lines = template.split("\r\n");
        for(String line: lines){
            if(pattern.matcher(line).find()) {
                return true;
            }
        }
        return false;
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

    /**
     * 将本地图片转换成Base64编码字符串
     *
     * @param file 文件
     * @return
     */
    public static String getFileToBase64(File file) {
        //将文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream inputStream = null;
        byte[] buffer = null;
        //读取图片字节数组
        try {
            inputStream = new FileInputStream(file);
            int count = 0;
            while (count == 0) {
                count = inputStream.available();
            }
            buffer = new byte[count];
            inputStream.read(buffer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对字节数组Base64编码
        return new BASE64Encoder().encode(buffer);
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

    public  static String getSignature(String orderNo,String username,String timestamp,String password) {
        //组织签名字符串
        StringBuffer signData = new StringBuffer();
        //header中的signature-nonce
        signData.append(orderNo);
        //订单号
        signData.append(orderNo);
        //认证账号；
        signData.append(username);
        //时间戳
        signData.append(timestamp);
        //签名 MD5_HMAC 签名KEY,参见给的账号EXCEL文件
        return md5_HMAC_sign(signData.toString(), password);
    }


}
