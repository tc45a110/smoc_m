package com.smoc.cloud.common.utils;

import java.util.Random;

public class Utils {

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
