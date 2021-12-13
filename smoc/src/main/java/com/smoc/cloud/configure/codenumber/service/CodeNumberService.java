package com.smoc.cloud.configure.codenumber.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.CodeNumberInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.codenumber.entity.ConfigNumberCodeInfo;
import com.smoc.cloud.configure.codenumber.repository.CodeNumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 码号管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CodeNumberService {

    @Resource
    private CodeNumberRepository codeNumberRepository;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<CodeNumberInfoValidator> page(PageParams<CodeNumberInfoValidator> pageParams) {
        return codeNumberRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<ConfigNumberCodeInfo> data = codeNumberRepository.findById(id);
        if(!data.isPresent()){
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        ConfigNumberCodeInfo entity = data.get();
        CodeNumberInfoValidator codeNumberInfoValidator = new CodeNumberInfoValidator();
        BeanUtils.copyProperties(entity, codeNumberInfoValidator);

        //转换日期
        codeNumberInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));
        if(!StringUtils.isEmpty(entity.getPriceEffectiveDate())){
            codeNumberInfoValidator.setPriceEffectiveDate(DateTimeUtils.getDateFormat(entity.getPriceEffectiveDate()));
        }
        if(!StringUtils.isEmpty(entity.getSrcIdEffectiveDate())){
            codeNumberInfoValidator.setSrcIdEffectiveDate(DateTimeUtils.getDateFormat(entity.getSrcIdEffectiveDate()));
        }
        if(!StringUtils.isEmpty(entity.getAccessTime())){
            codeNumberInfoValidator.setAccessTime(DateTimeUtils.getDateFormat(entity.getAccessTime()));
        }
        if(!StringUtils.isEmpty(entity.getMinConsumeEffectiveDate())){
            codeNumberInfoValidator.setMinConsumeEffectiveDate(DateTimeUtils.getDateFormat(entity.getMinConsumeEffectiveDate()));
        }

        return ResponseDataUtil.buildSuccess(codeNumberInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param codeNumberInfoValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<ConfigNumberCodeInfo> save(CodeNumberInfoValidator codeNumberInfoValidator, String op) {

        Iterable<ConfigNumberCodeInfo> data = codeNumberRepository.findBySrcIdAndCarrierAndProvinceAndBusinessType(codeNumberInfoValidator.getSrcId(),codeNumberInfoValidator.getCarrier(),codeNumberInfoValidator.getProvince(),codeNumberInfoValidator.getBusinessType());

        ConfigNumberCodeInfo entity = new ConfigNumberCodeInfo();
        BeanUtils.copyProperties(codeNumberInfoValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ConfigNumberCodeInfo info = (ConfigNumberCodeInfo) iter.next();
                if (!entity.getId().equals(info.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(codeNumberInfoValidator.getCreatedTime()));
        if(!StringUtils.isEmpty(codeNumberInfoValidator.getPriceEffectiveDate())){
            entity.setPriceEffectiveDate(DateTimeUtils.getDateFormat(codeNumberInfoValidator.getPriceEffectiveDate()));
        }
        if(!StringUtils.isEmpty(codeNumberInfoValidator.getSrcIdEffectiveDate())){
            entity.setSrcIdEffectiveDate(DateTimeUtils.getDateFormat(codeNumberInfoValidator.getSrcIdEffectiveDate()));
        }
        if(!StringUtils.isEmpty(codeNumberInfoValidator.getAccessTime())){
            entity.setAccessTime(DateTimeUtils.getDateFormat(codeNumberInfoValidator.getAccessTime()));
        }
        if(!StringUtils.isEmpty(codeNumberInfoValidator.getMinConsumeEffectiveDate())){
            entity.setMinConsumeEffectiveDate(DateTimeUtils.getDateFormat(codeNumberInfoValidator.getMinConsumeEffectiveDate()));
        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[码号管理][{}]数据:{}",op,JSON.toJSONString(entity));
        codeNumberRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }



}
