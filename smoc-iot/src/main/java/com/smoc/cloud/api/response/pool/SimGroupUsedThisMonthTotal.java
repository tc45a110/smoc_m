package com.smoc.cloud.api.response.pool;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimGroupUsedThisMonthTotal {

    /**
     * 使用量，单位 KB
     */
    private String useAmount;
}
