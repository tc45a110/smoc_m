package com.smoc.cloud.common.smoc.intelligence;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class IntellectMaterialTypeValidator {

    private String id;

    private String parentId;

    private String enterpriseId;

    private String title;

    private String typeDescribe;

    private String displaySort;

    private String status;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;
}
