package com.smoc.cloud.api.response.pool;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 群组本月套餐内流量使用量实时查
 */
@Getter
@Setter
public class SimGroupUsedThisMonth {

    /**
     * 群组主商品为”流量池”
     */
    private SimGroupUsedThisMonthPool flowPoolInfo;

    /**
     * 群组主商品为”流量池共享”
     */
    private SimGroupUsedThisMonthShare flowPoolSharingInfo;
}
