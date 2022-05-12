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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

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

        IntellectTemplateInfo existEntity = null;
        List<IntellectTemplateInfo> list = intellectTemplateInfoRepository.findIntellectTemplateInfoByTemplateId(intellectTemplateInfoValidator.getTemplateId());
        if (null != list && list.size() > 0) {
            existEntity = list.get(0);
        }

        IntellectTemplateInfo entity = new IntellectTemplateInfo();
        BeanUtils.copyProperties(intellectTemplateInfoValidator, entity);

        if (null == existEntity) {
            //转换日期格式
            entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(intellectTemplateInfoValidator.getCreatedTime()));
        } else {
            entity.setId(existEntity.getId());
            entity.setCreatedBy(existEntity.getCreatedBy());
            entity.setCreatedTime(existEntity.getCreatedTime());
            entity.setUpdatedBy(intellectTemplateInfoValidator.getCreatedBy());
            entity.setUpdatedTime(DateTimeUtils.getDateTimeFormat(intellectTemplateInfoValidator.getCreatedTime()));
        }

        //记录日志
        log.info("[智能短信][模版管理][{}]数据:{}", JSON.toJSONString(entity));
        intellectTemplateInfoRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据templateId 修改 tplId
     *
     * @param templateId
     * @param tplId
     * @return
     */
    @Transactional
    public ResponseData updateTplIdAndStatus(String templateId, String tplId, Integer status) {
        intellectTemplateInfoRepository.updateTplIdAndStatus(templateId, tplId, status);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * tplId 修改 status
     *
     * @param status
     * @param templateId
     * @return
     */
    @Transactional
    public ResponseData updateCheckStatus(String templateId, Integer status) {
        intellectTemplateInfoRepository.updateCheckStatus(templateId, status);
        return ResponseDataUtil.buildSuccess();

    }

    /**
     * tplId 修改 status
     *
     * @param status
     * @param tplId
     * @return
     */
    @Transactional
    public ResponseData updateCheckStatusByTplId(String tplId, Integer status) {
        intellectTemplateInfoRepository.updateCheckStatusByTplId(tplId, status);
        return ResponseDataUtil.buildSuccess();

    }

    /**
     * templateId 修改  status
     *
     * @param status
     * @param templateId
     * @return
     */
    @Transactional
    public ResponseData updateStatusByTemplateId(String templateId, Integer status) {
        intellectTemplateInfoRepository.updateStatusByTemplateId(templateId, status);
        return ResponseDataUtil.buildSuccess();

    }
}
