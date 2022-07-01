package com.smoc.cloud.common.smoc.configuate.validator;

import com.smoc.cloud.common.smoc.filter.ExcelModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SystemNumberCarrierValidator {

    private String carrier;
    private String numberCode;
    private String carrierName;

}
