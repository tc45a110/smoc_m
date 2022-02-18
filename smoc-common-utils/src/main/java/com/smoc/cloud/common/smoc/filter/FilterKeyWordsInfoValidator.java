package com.smoc.cloud.common.smoc.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class FilterKeyWordsInfoValidator {
    private String id;
    private String keyWordsBusinessType;
    private String businessId;
    private String keyWordsType;
    private String keyWords;
    private String keyDesc;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

}
