package com.smoc.cloud.filter.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取excel
 */
@Slf4j
public class KeywordsExcelModelListener extends AnalysisEventListener<ExcelModel> {

    @Getter
    List<ExcelModel> excelModelList = new ArrayList<ExcelModel>();

    @Override
    public void invoke(ExcelModel data, AnalysisContext context) {
        excelModelList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

}
