package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

/**
 * 系统标准请求 物联网卡批量办理结果查询请求模型
 */
@Setter
@Getter
public class BatchSimHandleRequest extends BaseRequest {

    /**
     * 批次Id
     */
    private String batchId;
}
