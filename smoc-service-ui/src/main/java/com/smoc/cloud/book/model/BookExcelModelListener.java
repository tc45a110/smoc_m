package com.smoc.cloud.book.model;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.smoc.cloud.common.smoc.customer.qo.BookExcelModel;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
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
public class BookExcelModelListener extends AnalysisEventListener<BookExcelModel> {

    @Getter
    List<BookExcelModel> excelModelList = new ArrayList<BookExcelModel>();

    @Override
    public void invoke(BookExcelModel data, AnalysisContext context) {
        if(!StringUtils.isEmpty(data.getMobile()) && Utils.isPhone(data.getMobile())){
            excelModelList.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

}
