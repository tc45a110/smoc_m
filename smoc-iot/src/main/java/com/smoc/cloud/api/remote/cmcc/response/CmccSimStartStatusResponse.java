package com.smoc.cloud.api.remote.cmcc.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CmccSimStartStatusResponse {

    /**
     * 是否管理停机冻结：
     * 0：否
     * 1：是
     */
    private String manageStopRestartStatus;

    /**
     * 管理停机冻结原因
     * （只有当物联卡被管理冻结时该参 数才有值）
     */
    private String reason;
}
