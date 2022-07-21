package com.smoc.cloud.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 系统标准响应 物联网卡批量办理结果查询响应模型
 */
@Setter
@Getter
public class BatchSimHandleResponse {

    /**
     * 批次状态：
     * 0：待处理
     * 1：处理中
     * 2：处理完成
     * 3：包含有处理失败记录的处理完成
     * 4：处理失败
     */
    private String batchStatus;

    /**
     * 返回号码类型：
     * 1：msisdn
     */
    private String batchType;

    /**
     * 返回号码
     */
    private String batchId;

    /**
     * 查询状态，0-成功，非 0-失败
     */
    private String queryStatus;

    /**
     * 错误信息，错误码对应的错误描述，参考错
     * 误码列表
     */
    private String message;


    /**
     * 物联卡批量操作结果列表，当任务状态
     * jobStatus 为 2、3 时返回处理结果，为 0、 1、4 时无
     */
    private List<BatchSimHandleItemResponse> batchList;


}
