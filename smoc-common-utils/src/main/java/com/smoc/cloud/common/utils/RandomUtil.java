package com.smoc.cloud.common.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomUtil {

    /**
     * 字符源
     * 也可以根据扩展需要，增加小写、数字
     */
    public static final String[] GENERATE_SOURCE = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static final int STR_LEN = GENERATE_SOURCE.length;

    /**
     * Collections.shuffle 随机生成3位字符串
     *
     * @return
     */
    public static String generateByShuffle(int len) {
        List<String> list = Arrays.asList(GENERATE_SOURCE);
        //可以理解为洗牌
        Collections.shuffle(list);
        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < STR_LEN; i++) {
            randomStr.append(list.get(i));
        }
        Random rand = new Random();
        int random = rand.nextInt(STR_LEN-len) + len;
        return randomStr.substring(random - len, random);
    }


}
