package com.smoc.cloud.common.utils;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试类
 * 2019/5/22 12:31
 **/
public class Main {

    public static void main(String[] args) {

       String filePath = "2022-08-12/3967315a016f4c6eb010aa6a6895e90e.jpg";
       //System.out.println(filePath.substring(11,filePath.length()));

        Pattern pattern = Pattern.compile("【中原消费金融】|【美团生活费】");
        String message =  "【qq美团生活费】";
        Matcher isMatch = pattern.matcher(message);
        if (isMatch.find()) {
            System.out.print("true");
        }else{
            System.out.print("false");
        }


    }
}
