package com.smoc.cloud.api.response.pool;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimGroupUsedThisMonthPool {

    /**
     *累积量类型编码
     */
    private String accmTypeCode;

    /**
     *累积量类型名称
     */
    private String accmTypeName;

    /**
     *累计使用量，单位:kb
     */
    private String useAmount;
}
