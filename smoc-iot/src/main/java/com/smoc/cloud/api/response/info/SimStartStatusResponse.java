package com.smoc.cloud.api.response.info;


import lombok.Getter;
import lombok.Setter;

/**
 * 系统标准响应
 */
@Getter
@Setter
public class SimStartStatusResponse {

    /**
     * 是否停机冻结：
     * 0：否
     * 1：是
     */
    private String simStatus;

    /**
     * 管理停机冻结原因
     * （只有当物联卡被管理冻结时该参 数才有值）
     */
    private String reason;
}
