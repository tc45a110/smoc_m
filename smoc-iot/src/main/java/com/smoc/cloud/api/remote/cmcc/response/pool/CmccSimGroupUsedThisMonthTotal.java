package com.smoc.cloud.api.remote.cmcc.response.pool;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmccSimGroupUsedThisMonthTotal {

    /**
     * 使用量，单位 KB
     */
    private String useAmount;
}
