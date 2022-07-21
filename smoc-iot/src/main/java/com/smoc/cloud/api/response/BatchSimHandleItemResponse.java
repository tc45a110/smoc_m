package com.smoc.cloud.api.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统标准响应 物联网卡批量办理结果查询响应模型 结果明细
 */
@Getter
@Setter
public class BatchSimHandleItemResponse {

    private String status;

    private String message;

    private String resultType;

    private String resultId;
}
