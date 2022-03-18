package com.smoc.cloud.template.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.template.AccountResourceInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.template.entity.AccountResourceInfo;
import com.smoc.cloud.template.repository.AccountResourceInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 资源管理
 */
@Slf4j
@Service
public class AccountResourceInfoService {

    @Resource
    private AccountResourceInfoRepository accountResourceInfoRepository;


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountResourceInfoValidator>> page(PageParams<AccountResourceInfoValidator> pageParams) {
        PageList<AccountResourceInfoValidator> data = accountResourceInfoRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 根据id 查询
     *
     * @param templateId
     * @return
     */
    public ResponseData<AccountResourceInfoValidator> findById(String templateId) {

        Optional<AccountResourceInfo> entityOptional = accountResourceInfoRepository.findById(templateId);
        if (!entityOptional.isPresent()) {
            ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        AccountResourceInfo entity = entityOptional.get();
        AccountResourceInfoValidator accountResourceInfoValidator = new AccountResourceInfoValidator();
        BeanUtils.copyProperties(entity, accountResourceInfoValidator);
        accountResourceInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(accountResourceInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param accountResourceInfoValidator
     * @param op                           操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<AccountResourceInfoValidator> save(AccountResourceInfoValidator accountResourceInfoValidator, String op) {

        AccountResourceInfo entity = new AccountResourceInfo();
        BeanUtils.copyProperties(accountResourceInfoValidator, entity);

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(accountResourceInfoValidator.getCreatedTime()));


        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[资源管理][{}]数据:{}", op, JSON.toJSONString(entity));
        accountResourceInfoRepository.saveAndFlush(entity);


        return ResponseDataUtil.buildSuccess(accountResourceInfoValidator);
    }

    @Transactional
    public ResponseData deleteById(String id) {

        AccountResourceInfo data = accountResourceInfoRepository.findById(id).get();
        //记录日志
        log.info("[模板管理][delete]数据:{}",JSON.toJSONString(data));
        accountResourceInfoRepository.updateStatus(id,"0");

        return ResponseDataUtil.buildSuccess();
    }
}
