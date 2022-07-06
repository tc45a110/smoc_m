package com.smoc.cloud.scheduler.service.filters.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularPattern {

    public static void main(String[] args) throws Exception {
        Random rand = new Random();
        Set<String> set = new HashSet<>();
        set.add("error");
        set.add("blue");
        System.out.println(set.contains("d"));
    }
}
