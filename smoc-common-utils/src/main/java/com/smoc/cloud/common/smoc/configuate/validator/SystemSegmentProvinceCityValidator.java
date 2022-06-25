package com.smoc.cloud.common.smoc.configuate.validator;

import com.smoc.cloud.common.smoc.filter.ExcelModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SystemSegmentProvinceCityValidator {
    private String id;
    private String segment;
    private String provinceCode;
    private String provinceName;
    private String cityName;

    List<ExcelModel> excelModelList = new ArrayList();
}
