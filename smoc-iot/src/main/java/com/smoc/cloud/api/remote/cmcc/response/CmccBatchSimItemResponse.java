package com.smoc.cloud.api.remote.cmcc.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 移动物联网卡接口对接 物联网卡批量办理结果 明细
 */
@Setter
@Getter
public class CmccBatchSimItemResponse {

    private String status;

    private String message;

    private String resultType;

    private String resultId;

}
