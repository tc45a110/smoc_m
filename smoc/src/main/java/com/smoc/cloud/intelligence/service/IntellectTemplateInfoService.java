package com.smoc.cloud.intelligence.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.intelligence.entity.IntellectMaterial;
import com.smoc.cloud.intelligence.entity.IntellectTemplateInfo;
import com.smoc.cloud.intelligence.repository.IntellectTemplateInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;

@Slf4j
@Service
public class IntellectTemplateInfoService {

    @Resource
    private IntellectTemplateInfoRepository intellectTemplateInfoRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<IntellectTemplateInfoValidator> page(PageParams<IntellectTemplateInfoValidator> pageParams) {

        PageList<IntellectTemplateInfoValidator> pageList = intellectTemplateInfoRepository.page(pageParams);
        return pageList;
    }

    /**
     * 保存或修改
     *
     * @param intellectTemplateInfoValidator
     * @return
     */
    @Transactional
    public ResponseData save(IntellectTemplateInfoValidator intellectTemplateInfoValidator) {

        IntellectTemplateInfo entity = new IntellectTemplateInfo();
        BeanUtils.copyProperties(intellectTemplateInfoValidator, entity);
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(intellectTemplateInfoValidator.getCreatedTime()));

        //记录日志
        log.info("[智能短信][模版管理][{}]数据:{}", JSON.toJSONString(entity));
        intellectTemplateInfoRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }
}
