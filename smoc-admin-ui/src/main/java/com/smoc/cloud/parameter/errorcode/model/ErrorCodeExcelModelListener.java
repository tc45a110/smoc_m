package com.smoc.cloud.parameter.errorcode.model;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.smoc.cloud.common.smoc.parameter.model.ErrorCodeExcelModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取excel
 */
@Slf4j
public class ErrorCodeExcelModelListener extends AnalysisEventListener<ErrorCodeExcelModel> {

    @Getter
    List<ErrorCodeExcelModel> excelModelList = new ArrayList<ErrorCodeExcelModel>();

    @Override
    public void invoke(ErrorCodeExcelModel data, AnalysisContext context) {
        if(!StringUtils.isEmpty(data.getErrorCode())){
            excelModelList.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

}
