package com.smoc.cloud.configure.channel.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseContractInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.channel.entity.ConfigChannelRepairRule;
import com.smoc.cloud.configure.channel.repository.ChannelRepairRepository;
import com.smoc.cloud.customer.entity.EnterpriseContractInfo;
import com.smoc.cloud.customer.entity.SystemAttachmentInfo;
import com.smoc.cloud.filter.entity.FilterBlackList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 通道失败补发接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelRepairService {

    @Resource
    private ChannelRepairRepository channelRepairRepository;


    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<ConfigChannelRepairValidator> page(PageParams<ConfigChannelRepairValidator> pageParams) {
        return channelRepairRepository.page(pageParams);
    }

    /**
     * 根据运营商、业务类型、信息分类查询符合要求的备用通道
     * @param channelBasicInfoValidator
     * @return
     */
    public ResponseData<List<ConfigChannelRepairValidator>> findSpareChannel(ChannelBasicInfoValidator channelBasicInfoValidator) {
        List<ConfigChannelRepairValidator> list = channelRepairRepository.findSpareChannel(channelBasicInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<ConfigChannelRepairRule> data = channelRepairRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        ConfigChannelRepairRule entity = data.get();
        ConfigChannelRepairRuleValidator configChannelRepairRuleValidator = new ConfigChannelRepairRuleValidator();
        BeanUtils.copyProperties(entity, configChannelRepairRuleValidator);

        //转换日期
        configChannelRepairRuleValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(configChannelRepairRuleValidator);
    }

    @Transactional
    public ResponseData save(ConfigChannelRepairRuleValidator configChannelRepairRuleValidator, String op) {

        //转BaseUser存放对象
        ConfigChannelRepairRule entity = new ConfigChannelRepairRule();
        BeanUtils.copyProperties(configChannelRepairRuleValidator, entity);

        List<ConfigChannelRepairRule> data = channelRepairRepository.findByChannelIdAndChannelRepairIdAndBusinessTypeAndRepairStatus(configChannelRepairRuleValidator.getChannelId(),configChannelRepairRuleValidator.getChannelRepairId(),configChannelRepairRuleValidator.getBusinessType(),"1");

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError("主通道下已经存在该备用通道，请更换！");
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ConfigChannelRepairRule organization = (ConfigChannelRepairRule) iter.next();
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
        log.info("[失败补发通道管理][补发配置][{}]数据:{}",op, JSON.toJSONString(configChannelRepairRuleValidator));

        channelRepairRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据ID 删除
     * @param id
     * @return
     */
    @Transactional
    public ResponseData deleteById(String id) {
        ConfigChannelRepairRule data = channelRepairRepository.findById(id).get();
        //记录日志
        log.info("[失败补发通道管理][delete]数据:{}",JSON.toJSONString(data));
        channelRepairRepository.updateStatusById(id);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 查询已经存在的备用通道
     * @param configChannelRepairRuleValidator
     * @return
     */
    public ResponseData<List<ConfigChannelRepairRuleValidator>> findChannelRepairByChannelId(ConfigChannelRepairRuleValidator configChannelRepairRuleValidator) {

        List<ConfigChannelRepairRuleValidator> list = channelRepairRepository.findByChannelIdAndBusinessType(configChannelRepairRuleValidator.getChannelId(),configChannelRepairRuleValidator.getBusinessType());
        return ResponseDataUtil.buildSuccess(list);
    }

}
