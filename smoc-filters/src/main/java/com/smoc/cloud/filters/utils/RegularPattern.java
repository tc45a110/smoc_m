package com.smoc.cloud.filters.utils;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularPattern {

    public static void main(String[] args) throws Exception {
//        String mobile = "【兴业银行】尾号大的客户，退订回T";
//        Pattern pattern = Pattern.compile("【广发银行】尾号.*客户，退订回T |【兴业银行】尾号.*客户，退订回T");
//        Matcher matcher = pattern.matcher(mobile);
//        if(matcher.find()){
//            System.out.println(true);
//        }else{
//            System.out.println(false);
//        }

        String phone = "13510351258";
        System.out.println(phone.substring(0,4));
    }
}
