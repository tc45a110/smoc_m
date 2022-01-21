package com.smoc.cloud.common.utils;


import java.util.Random;

public class PasswordUtils {

    public static String getRandomPassword(int len) {
        String result = null;
        result = makeRandomPassword(len);
        if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*\\d{1,}.*") && result.matches(".*[~!@#$%^&*\\.?]{1,}.*")) {
            return result;
        }
        return result = makeRandomPassword(len);
    }

    public static String makeRandomPassword(int len) {
        char charr[] = "abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ123456789!@#$%&*?".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String s = getRandomPassword(9);
        System.out.println(s);
        System.out.println(DES.encrypt(s));
    }
}
