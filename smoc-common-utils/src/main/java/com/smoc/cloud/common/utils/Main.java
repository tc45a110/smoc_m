package com.smoc.cloud.common.utils;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试类
 * 2019/5/22 12:31
 **/
public class Main {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();

        list.add("1");
        list.add("2");

        System.out.println(new Gson().toJson(list));
    }
}
