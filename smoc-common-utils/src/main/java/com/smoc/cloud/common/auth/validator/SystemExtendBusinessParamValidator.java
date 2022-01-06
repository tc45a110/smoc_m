package com.smoc.cloud.common.auth.validator;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SystemExtendBusinessParamValidator {

    private String id;

    private String businessType;

    private String paramTitle;

    private String paramKey;

    private String dataType;

    private String placeholder;

    private String isNull;

    private String dictEnable;

    private Integer paramMaxLength;

    private String showType;

    private String showStyle;

    private String regularExp;

    private String isReadonly;

    private Integer displaySort;

    private String paramStatus;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;

}
