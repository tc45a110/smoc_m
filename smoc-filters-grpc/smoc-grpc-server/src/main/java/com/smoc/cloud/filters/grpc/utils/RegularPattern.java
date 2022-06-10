package com.smoc.cloud.filters.grpc.utils;

import com.google.gson.Gson;

public class RegularPattern {

    public static void main(String[] args) throws Exception {
//        String mobile = "18510816771";
//        Pattern pattern = Pattern.compile("18510");
//        Matcher matcher = pattern.matcher(mobile);
//        if(matcher.find()){
//            System.out.println(true);
//        }else{
//            System.out.println(false);
//        }

        String d = "1D2B1";
        String[] days =d.split("D");
        System.out.println(new Gson().toJson(days));
        System.out.println(days.length);
    }
}
