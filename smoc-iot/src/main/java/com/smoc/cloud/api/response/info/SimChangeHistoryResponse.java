package com.smoc.cloud.api.response.info;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SimChangeHistoryResponse {


    /**
     * 原状态
     * 1：待激活
     * 2：已激活
     * 4：停机
     * 6：可测试
     * 7：库存
     * 8：预销户
     */
    private String descStatus;

    /**
     * 目标状态
     * 1：待激活
     * 2：已激活
     * 4：停机
     * 6：可测试
     * 7：库存
     * 8：预销户
     */
    private String targetStatus;

    /**
     * 变更时间
     */
    private String changeDate;
}
