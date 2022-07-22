package com.smoc.cloud.api.remote.cmcc.response.pool;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 群组本月套餐内流量使用量实时查
 */
@Getter
@Setter
public class CmccSimGroupUsedThisMonth {

    /**
     * 群组主商品为”流量池”
     */
    private List<CmccSimGroupUsedThisMonthPool> flowPoolInfo;

    /**
     * 群组主商品为”流量池共享”
     */
    private List<CmccSimGroupUsedThisMonthShare> flowPoolSharingInfo;
}
