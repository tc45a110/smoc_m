package com.smoc.cloud.api.remote.cmcc.response.info;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CmccSimStopReasonResponse {

    /**
     * 物联卡对应的管理平台
     */
    private String platformType;

    /**
     * 停机原因：
     * 000000000020：强制双向停机
     * 000000002000：主动（申请）双向停机
     * 000020000000：信控双向停机
     * 200000000000：窄带网商品到期失效停机
     * 020000000000：机卡分离停机
     * 000200000000：M2M 管理停机
     * 000000000200：区域限制（位置固定）管理型停机
     * 000000000000：该卡当前不处于“已停机”或系统 暂无停机原因
     * 总共 12 个数字标识停机原因，用 0 与 2 表 示，2 在不同的位置时，表示不同的停机原因。支 持多种停机原因叠加，如：000020000020，表示强 制双向停机和信控双向停机。
     */
    private String stopReason;

    /**
     * 停机原因描述
     */
    private String shutdownReasonDesc;
}
