package com.smoc.cloud.common.http.server.utils;

import java.util.HashMap;
import java.util.Map;

public class Constant {

    //需要账号有对应的业务类型，才可以执行
    public static final Map<String, String> BUSINESS_ACCOUNT_MAP;

    static {
        BUSINESS_ACCOUNT_MAP = new HashMap<>();
        BUSINESS_ACCOUNT_MAP.put("addTemplate", "TEXT_SMS");
        BUSINESS_ACCOUNT_MAP.put("sendMessageByTemplate", "TEXT_SMS");
        BUSINESS_ACCOUNT_MAP.put("addMultimediaTemplate", "MULTI_SMS");
        BUSINESS_ACCOUNT_MAP.put("sendMultimediaMessageByTemplate", "MULTI_SMS");
        BUSINESS_ACCOUNT_MAP.put("sendInterMessageByTemplate", "INTERNATIONAL_SMS");
        BUSINESS_ACCOUNT_MAP.put("addInterTemplate", "INTERNATIONAL_SMS");
    }
}
