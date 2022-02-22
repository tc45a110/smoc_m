package com.smoc.cloud.template.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.template.entity.AccountTemplateInfo;
import com.smoc.cloud.template.repository.AccountTemplateInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 模板管理
 */
@Slf4j
@Service
public class AccountTemplateInfoService {

    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountTemplateInfoValidator>> page(PageParams<AccountTemplateInfoValidator> pageParams) {
        PageList<AccountTemplateInfoValidator> data = accountTemplateInfoRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 根据id 查询
     *
     * @param templateId
     * @return
     */
    public ResponseData<AccountTemplateInfoValidator> findById(String templateId) {

        Optional<AccountTemplateInfo> entityOptional = accountTemplateInfoRepository.findById(templateId);
        if (!entityOptional.isPresent()) {
            ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        AccountTemplateInfo entity = entityOptional.get();
        AccountTemplateInfoValidator accountTemplateInfoValidator = new AccountTemplateInfoValidator();
        BeanUtils.copyProperties(entity, accountTemplateInfoValidator);
        if(null != entity.getCheckDate()) {
            accountTemplateInfoValidator.setCheckDate(DateTimeUtils.getDateTimeFormat(entity.getCheckDate()));
        }
        accountTemplateInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(accountTemplateInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param accountTemplateInfoValidator
     * @param op                           操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<AccountTemplateInfoValidator> save(AccountTemplateInfoValidator accountTemplateInfoValidator, String op) {

        AccountTemplateInfo entity = new AccountTemplateInfo();
        BeanUtils.copyProperties(accountTemplateInfoValidator, entity);

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(accountTemplateInfoValidator.getCreatedTime()));
        if (null != accountTemplateInfoValidator.getCheckDate())
            entity.setCheckDate(DateTimeUtils.getDateTimeFormat(accountTemplateInfoValidator.getCheckDate()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[模板管理][模板信息][{}]数据:{}", op, JSON.toJSONString(entity));
        accountTemplateInfoRepository.saveAndFlush(entity);


        return ResponseDataUtil.buildSuccess(accountTemplateInfoValidator);
    }

    /**
     * 注销模板
     * @param templateId
     * @return
     */
    @Transactional
    public ResponseData cancelTemplate(String templateId,String templateStatus){
        accountTemplateInfoRepository.cancelTemplate(templateId,templateStatus);
        return ResponseDataUtil.buildSuccess();
    }
}
