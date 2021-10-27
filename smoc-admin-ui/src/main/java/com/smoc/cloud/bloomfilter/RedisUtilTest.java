package com.smoc.cloud.bloomfilter;

import io.rebloom.client.Client;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RedisUtilTest {

    public static void main(String[] args) {
//
//        List<String> array = new ArrayList<>();
//        array.add("101");
//        array.add("102");
//        array.add("103");
//        array.add("104");
//        array.add("105");
//        array.add("106");
//        array.add("107");
//        array.add("108");
//        System.out.println(array.toString());
//        String[] arrayGroup = (String[]) array.toArray(new String[array.size()]);

        BufferedReader reader = null;
        BufferedWriter writer_send = null;
        BufferedWriter writer_error = null;
        boolean[] booleans = null;

        String filePath = "D:\\mobile\\black1.txt";
        File originalFile = new File(filePath);

        int i = 0;
        try {

            RedisConfiguration redisConfiguration = new RedisConfiguration();
            BloomFilterRedis util = new BloomFilterRedis();
            Client client = util.getClient(redisConfiguration.getRedisPool());

            //client.createFilter("filterList", 100000, 0.000001);
//            String[] sendArray = new String[8];
//            sendArray[0] = "15026476022";
//            sendArray[1] = "13581422800";
//            sendArray[2] = "18737183672";
//            sendArray[3] = "15925843278";
//            sendArray[4] = "15953820406";
//            sendArray[5] = "15057542928";
//            sendArray[6] = "15041472266";
//            sendArray[7] = "15811225498";
            //sendArray[8] = "18510816771";
//            System.out.println("1=" + client.exists("testFilter", "13068103751"));
//
//            System.out.println("2=" + client.exists("testFilter", "102"));
//            System.out.println("3=" + client.exists("testFilter", "103"));
//            System.out.println("4=" + client.exists("testFilter", "104"));
//            System.out.println("5=" + client.exists("testFilter", "105"));
//            System.out.println("6=" + client.exists("testFilter", "106"));
//            System.out.println("7=" + client.exists("testFilter", "107"));
//            System.out.println("8=" + client.exists("testFilter", "108"));
//            System.out.println("start:" + System.currentTimeMillis());
//              booleans = client.existsMulti("systemBlackFilter", sendArray);
             //booleans = client.addMulti("filterList", "systemBlackFilter");
            //boolean isExist = client.exists("filterList", "systemBlackFilter");
            //System.out.println("end--:" + isExist);

            reader = new BufferedReader(new FileReader(originalFile));
            String mobile = null;
            List<String> mobileList = new ArrayList<>();
            while ((mobile = reader.readLine()) != null) {
                if (StringUtils.isEmpty(mobile)) {
                    continue;
                }
                mobile = mobile.trim();
                if (0 < i && i < 200000) {
                    mobileList.add(mobile);
                }
                //System.out.println(mobile);
                i++;
            }
//
            String[] mobileArray = (String[]) mobileList.toArray(new String[mobileList.size()]);
            if(null != mobileArray && mobileArray.length>0) {
                System.out.println("start:" + System.currentTimeMillis());
                booleans = client.existsMulti("TEST20200608BlackFilter", mobileArray);
                //booleans = client.addMulti("testFilter", mobileArray);
                System.out.println("end--:" + System.currentTimeMillis());
            }else {
                System.out.println("数组为空");
            }
//
//
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(booleans.length);
        int t=0;
        for (boolean b : booleans) {
            if (!b) {
                t++;
                //System.out.println(b);
            }
        }
        System.out.println(t);
        System.out.println(i + "-end-:" + System.currentTimeMillis());


//        client.createFilter("testFilter",1000000,0.01);
//        client.add("testFilter","181");
//        client.add("testFilter","182");
//        client.add("testFilter","186");
//        client.add("testFilter","185");
        //boolean[] booleans = client.addMulti("testFilter", arrayGroup);
//        boolean[] booleans = client.existsMulti("testFilter","181","182","183","184","185","186","187","188","189","190");
//        for (boolean b:booleans){
//            System.out.println("19="+b);
//        }



    }

}
