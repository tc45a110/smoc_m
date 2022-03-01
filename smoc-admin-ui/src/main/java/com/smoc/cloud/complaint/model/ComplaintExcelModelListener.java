package com.smoc.cloud.complaint.model;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取excel
 */
@Slf4j
public class ComplaintExcelModelListener extends AnalysisEventListener<ComplaintExcelModel> {

    @Getter
    List<ComplaintExcelModel> excelModelList = new ArrayList<ComplaintExcelModel>();

    @Override
    public void invoke(ComplaintExcelModel data, AnalysisContext context) {
        if (!StringUtils.isEmpty(data.getCarrier()) && !StringUtils.isEmpty(data.getReportNumber()) && !StringUtils.isEmpty(data.getReportContent()) && !StringUtils.isEmpty(data.getReportDate())) {
            if(!StringUtils.isEmpty(data.getReportChann()) && "12321".equals(data.getReportChann().trim())){
                data.setIs12321("1");
            }else{
                data.setIs12321("0");
            }
            excelModelList.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

}
