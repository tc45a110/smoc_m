package com.smoc.cloud.common.auth.validator;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Setter
@Getter
public class SystemExtendBusinessParamValidator {

    private String id;

    private String businessType;

    private String paramTitle;

    private String paramKey;

    private String dataType;

    private String isNull;

    private String dictEnable;

    private Integer paramMaxLength;

    private String showType;

    private String showStyle;

    private String isReadonly;

    private String paramStatus;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

}
