package com.smoc.cloud.api.remote.cmcc.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 移动物联网卡接口对接 物联网卡批量办理结果
 */
@Getter
@Setter
public class CmccBatchSimResponse {

    /**
     * 任务状态：
     * 0：待处理
     * 1：处理中
     * 2：处理完成
     * 3：包含有处理失败记录的处理完成
     * 4：处理失败
     */
    private String jobStatus;

    /**
     * 物联卡批量操作结果列表，当任务状态
     * jobStatus 为 2、3 时返回处理结果，为 0、 1、4 时无
     */
    private List<CmccBatchSimItemResponse> resultList;

    /**
     * 返回号码类型：
     * 1：msisdn
     */
    private String resultType;

    /**
     * 返回号码
     */
    private String resultId;

    /**
     * 查询状态，0-成功，非 0-失败
     */
    private String status;

    /**
     * 错误信息，错误码对应的错误描述，参考错
     * 误码列表
     */
    private String message;
}
