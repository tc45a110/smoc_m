package com.smoc.cloud.scheduler.initialize;

import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义的全局变量
 */
public class Reference {

    /**
     * 账号基本信息
     */
    public static Map<String, AccountBaseInfo> accounts = new HashMap<>();
}
