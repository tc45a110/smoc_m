package com.smoc.cloud.configure.channel.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigRepairRuleValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelRepairQo;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.channel.entity.ConfigChannelRepairRule;
import com.smoc.cloud.configure.channel.entity.ConfigRepairRule;
import com.smoc.cloud.configure.channel.repository.ChannelRepairRepository;
import com.smoc.cloud.configure.channel.repository.ConfigRepairRuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 失败补发规则接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigRepairRuleService {

    @Resource
    private ConfigRepairRuleRepository configRepairRuleRepository;


    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<ConfigRepairRule> data = configRepairRuleRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        ConfigRepairRule entity = data.get();
        ConfigRepairRuleValidator configRepairRuleValidator = new ConfigRepairRuleValidator();
        BeanUtils.copyProperties(entity, configRepairRuleValidator);

        //转换日期
        configRepairRuleValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(configRepairRuleValidator);
    }

    @Transactional
    public ResponseData save(ConfigRepairRuleValidator configRepairRuleValidator, String op) {

        //转BaseUser存放对象
        ConfigRepairRule entity = new ConfigRepairRule();
        BeanUtils.copyProperties(configRepairRuleValidator, entity);

        List<ConfigRepairRule> data = configRepairRuleRepository.findByBusinessIdAndBusinessTypeAndRepairStatus(configRepairRuleValidator.getBusinessId(),configRepairRuleValidator.getBusinessType(),"1");

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ConfigRepairRule organization = (ConfigRepairRule) iter.next();
                if (!entity.getId().equals(organization.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        entity.setCreatedTime(new Date());

        //记录日志
        log.info("[失败补发规则][补发配置][{}]数据:{}",op, JSON.toJSONString(configRepairRuleValidator));

        configRepairRuleRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    public ResponseData findByBusinessId(String businessId) {
        ConfigRepairRule entity = configRepairRuleRepository.findByBusinessIdAndRepairStatus(businessId,"1");

        if (StringUtils.isEmpty(entity)) {
            return ResponseDataUtil.buildSuccess();
        }

        ConfigRepairRuleValidator configRepairRuleValidator = new ConfigRepairRuleValidator();
        BeanUtils.copyProperties(entity, configRepairRuleValidator);

        //转换日期
        configRepairRuleValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(configRepairRuleValidator);
    }
}
