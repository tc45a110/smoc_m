package com.smoc.cloud.scheduler.initialize;

import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.entity.AccountFinanceInfo;
import com.smoc.cloud.scheduler.initialize.model.AccountChannelBusinessModel;
import com.smoc.cloud.scheduler.initialize.model.ContentRouteBusinessModel;
import com.smoc.cloud.scheduler.initialize.model.MessagePriceBusinessModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义的全局变量
 */
public class Reference {

    /**
     * 业务账号基本信息
     */
    public static Map<String, AccountBaseInfo> accounts = new HashMap<>();

    /**
     * 业务账号财务账号信息
     */
    public static Map<String, AccountFinanceInfo> accountFinances = new HashMap<>();

    /**
     * 业务账号 运营商价格
     */
    public static Map<String, MessagePriceBusinessModel> accountMessagePrices = new HashMap<>();

    /**
     * 业务账号通道路由业务模型
     */
    public static Map<String, AccountChannelBusinessModel> accountChannelBusinessModels = new HashMap<>();

    /**
     * 业务账号内容路由业务模型
     */
    public static Map<String, ContentRouteBusinessModel> contentRouteBusinessModels = new HashMap<>();
}
