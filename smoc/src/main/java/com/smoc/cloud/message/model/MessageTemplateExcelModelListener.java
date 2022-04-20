package com.smoc.cloud.message.model;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.smoc.cloud.common.smoc.message.model.MessageTemplateExcelModel;
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
public class MessageTemplateExcelModelListener extends AnalysisEventListener<MessageTemplateExcelModel> {

    @Getter
    List<String> excelModelList = new ArrayList<String>();

    @Override
    public void invoke(MessageTemplateExcelModel data, AnalysisContext context) {
        if(!StringUtils.isEmpty(data.getMobile()) && Utils.isPhone(data.getMobile())){
            StringBuilder sb = new StringBuilder();
            sb.append(data.getMobile().trim());
            if(!StringUtils.isEmpty(data.getColumn1())){
                sb.append("|"+data.getColumn1());
            }
            if(!StringUtils.isEmpty(data.getColumn2())){
                sb.append("|"+data.getColumn2());
            }
            if(!StringUtils.isEmpty(data.getColumn3())){
                sb.append("|"+data.getColumn3());
            }
            if(!StringUtils.isEmpty(data.getColumn4())){
                sb.append("|"+data.getColumn4());
            }
            if(!StringUtils.isEmpty(data.getColumn5())){
                sb.append("|"+data.getColumn5());
            }
            if(!StringUtils.isEmpty(data.getColumn6())){
                sb.append("|"+data.getColumn6());
            }
            if(!StringUtils.isEmpty(data.getColumn7())){
                sb.append("|"+data.getColumn7());
            }
            if(!StringUtils.isEmpty(data.getColumn8())){
                sb.append("|"+data.getColumn8());
            }
            if(!StringUtils.isEmpty(data.getColumn9())){
                sb.append("|"+data.getColumn9());
            }
            if(!StringUtils.isEmpty(data.getColumn10())){
                sb.append("|"+data.getColumn10());
            }
            excelModelList.add(sb.toString());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

}
