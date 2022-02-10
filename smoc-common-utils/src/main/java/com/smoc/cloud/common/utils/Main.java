package com.smoc.cloud.common.utils;


/**
 * 测试类
 * 2019/5/22 12:31
 **/
public class Main {

    public static void main(String[] args) {

        String s = "XYBA100014,XYBA100012";
        String accountIds = "'"+s.replace(",","','")+"'";

        //String flag= MD5.md5("F6D42B072BA02649A66CB4B94F593686"+"7F6C5E670F4CCDC8591BB91F5054C3DB");

        System.out.println(accountIds);
    }
}
