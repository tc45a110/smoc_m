package com.smoc.cloud.scheduler.service.filters.utils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularPattern {

    public static void main(String[] args) throws Exception {
        BigDecimal price = new BigDecimal("0.0300");
        Integer messageTotal =5;
        BigDecimal consumeTotal = price.multiply(new BigDecimal(messageTotal));
        BigDecimal accountUsableSum = new BigDecimal("1.0300");
        BigDecimal accountCreditSum = new BigDecimal("0");
        BigDecimal result = accountUsableSum.add(accountCreditSum).subtract(consumeTotal);

        BigDecimal zero = new BigDecimal(0);

        System.out.println(result);
        System.out.println(!(result.compareTo(zero)==-1));
    }
}
