package com.smoc.cloud.intelligence.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.intelligence.entity.IntellectCallbackTemplateStatusReport;
import com.smoc.cloud.intelligence.repository.IntellectCallbackTemplateStatusReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class IntellectCallbackTemplateStatusReportService {

    @Resource
    private IntellectCallbackTemplateStatusReportRepository intellectCallbackTemplateStatusReportRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<IntellectCallbackTemplateStatusReportValidator> page(PageParams<IntellectCallbackTemplateStatusReportValidator> pageParams) {
        return intellectCallbackTemplateStatusReportRepository.page(pageParams);
    }

    /**
     * 保存模版回调，并变更模版状态
     *
     * @param intellectCallbackTemplateStatusReportValidator
     * @return
     */
    @Async
    @Transactional
    public void save(IntellectCallbackTemplateStatusReportValidator intellectCallbackTemplateStatusReportValidator) {

        IntellectCallbackTemplateStatusReport entity = new IntellectCallbackTemplateStatusReport();
        BeanUtils.copyProperties(intellectCallbackTemplateStatusReportValidator, entity);
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(intellectCallbackTemplateStatusReportValidator.getCreatedTime()));
        //记录日志
        log.info("[智能短信][模版管理][模版状态回调][{}]数据:{}", JSON.toJSONString(entity));

        //变更模版状态
        intellectCallbackTemplateStatusReportRepository.updateTemplateStatus(intellectCallbackTemplateStatusReportValidator.getTplId(), intellectCallbackTemplateStatusReportValidator.getState(), intellectCallbackTemplateStatusReportValidator.getAuditState(), intellectCallbackTemplateStatusReportValidator.getAuditDesc());

        //保存模版状态回执
        intellectCallbackTemplateStatusReportRepository.saveAndFlush(entity);

    }

}
