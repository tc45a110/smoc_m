package com.smoc.cloud.filters.utils;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularPattern {

    public static void main(String[] args) throws Exception {
        String mobile = "【兴业银行】尾号大的客户，退订回T";
        Pattern pattern = Pattern.compile("【广发银行】尾号.*客户，退订回T |【兴业银行】尾号.*客户，退订回T");
        Matcher matcher = pattern.matcher(mobile);
        if(matcher.find()){
            System.out.println(true);
        }else{
            System.out.println(false);
        }

//        String d = "1D2B1";
//        String[] days =d.split("D");
//        System.out.println(new Gson().toJson(days));
//        System.out.println(days.length);
    }
}
