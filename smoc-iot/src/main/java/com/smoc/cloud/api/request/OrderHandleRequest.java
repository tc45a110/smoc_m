package com.smoc.cloud.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统标准请求 单卡订单处理结果查询请求模型
 */
@Setter
@Getter
public class OrderHandleRequest extends BaseRequest {

    /**
     * 物联网卡业务办理订单号
     */
    private String orderNum;
}
