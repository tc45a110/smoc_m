package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 2020/5/31 11:09
 **/
@Setter
@Getter
public class ConfiguationParamsValidator {

    private String id;
    private String userId;
    private String paramCode;
    private String paramValue;
    private String paramValueDesc;
    private String status;
    private Date dataDate;
}
