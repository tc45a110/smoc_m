package com.smoc.cloud.common.auth.qo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 2020/5/30 14:44
 **/
@Setter
@Getter
public class ConfiguationParams {

    private String id;
    private String userId;
    private String paramCode;
    private String paramValue;
    private String paramValueDesc;
    private String status;
    private Date dataDate;
}
