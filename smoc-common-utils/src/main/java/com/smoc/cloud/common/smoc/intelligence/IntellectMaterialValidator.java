package com.smoc.cloud.common.smoc.intelligence;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class IntellectMaterialValidator {


    private String id;

    private String parentId;

    private String materialTypeId;

    private String materialName;

    private String materialType;

    private String fileName;

    private String fileType;

    private Integer fileSize;

    private String imageRate;

    private String business;

    private String fileUrl;

    private String resourceId;

    private String status;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;
}
