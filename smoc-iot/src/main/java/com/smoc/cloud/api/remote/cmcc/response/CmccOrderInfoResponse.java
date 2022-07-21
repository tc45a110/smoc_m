package com.smoc.cloud.api.remote.cmcc.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 移动物联网卡接口对接 订单信息
 */
@Setter
@Getter
public class CmccOrderInfoResponse {

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 订单状态
     * -acknowledged: 初始。
     * -ready: 准备。
     * -future: 等待。
     * -inProgress: 处理中。
     * -amending: 修改中。
     * -suspended: 暂停。
     * -failed: 异常。
     * -aborted: 终止。
     * -cancelling: 取消中。
     * -cancelled: 已取消。
     * completed: 完成。
     */
    private String status;

    /**
     * 订单状态时间。格式：yyyyMMddHHmmss
     */
    private String statusDate;

    /**
     * 订单创建时间。格式：yyyyMMddHHmmss
     */
    private String createDate;
}
