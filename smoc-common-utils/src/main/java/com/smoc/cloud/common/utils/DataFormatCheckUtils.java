package com.smoc.cloud.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataFormatCheckUtils {

    public static boolean isPhone(String phone) {
        if (phone==null||phone.length() != 11) {
            return false;
        } else {
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-9])|(18[0-9])|(19[8|9]))\\d{8}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }
}
