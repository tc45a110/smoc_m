package com.smoc.cloud.api.remote.cmcc.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CmccSimStatusResponse {

    /**
     * 1：待激活
     * 2：已激活
     * 4：停机
     * 6：可测试
     * 7：库存
     * 8：预销户
     */
    private String cardStatus;

    /**
     * 最后一次变更时间
     */
    private String lastChangeDate;
}
