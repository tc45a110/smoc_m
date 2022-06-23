package com.smoc.cloud.configure.number.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.utils.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取excel
 */
@Slf4j
public class CodeNumberExcelModelListener extends AnalysisEventListener<ExcelModel> {

    @Getter
    List<ExcelModel> excelModelList = new ArrayList<ExcelModel>();

    @Override
    public void invoke(ExcelModel data, AnalysisContext context) {
        if(!StringUtils.isEmpty(data.getColumn1())){
            excelModelList.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

}
