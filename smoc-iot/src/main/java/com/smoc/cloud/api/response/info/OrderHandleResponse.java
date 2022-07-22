package com.smoc.cloud.api.response.info;


import lombok.Getter;
import lombok.Setter;

/**
 * 系统标准响应 单卡订单处理结果查询响应模型
 */
@Setter
@Getter
public class OrderHandleResponse {

    /**
     * 物联网卡办理订单号
     */
    private String orderNum;

    /**
     * 订单状态
     * acknowledged: 初始。
     * ready: 准备。
     * future: 等待。
     * inProgress: 处理中。
     * amending: 修改中。
     * suspended: 暂停。
     * failed: 异常。
     * aborted: 终止。
     * cancelling: 取消中。
     * cancelled: 已取消。
     * completed: 完成。
     */
    private String orderStatus;

    /**
     * 订单最后处理时间
     */
    private String handleDate;

    /**
     * 订单创建时间
     */
    private String createDate;
}
