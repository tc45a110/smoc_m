package com.smoc.cloud.common.smoc.configuate.validator;

import com.smoc.cloud.common.smoc.filter.ExcelModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class ConfigNumberInfoValidator {
    private String id;
    private String numberCode;
    private String carrier;
    private String province;
    private String numberCodeType;
    private String status;
    private String isSync;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    List<ExcelModel> excelModelList = new ArrayList<>();
}
