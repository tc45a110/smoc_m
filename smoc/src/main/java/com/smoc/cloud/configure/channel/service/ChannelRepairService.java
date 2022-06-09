package com.smoc.cloud.configure.channel.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.configure.channel.entity.ConfigChannelRepairRule;
import com.smoc.cloud.configure.channel.repository.ChannelRepairRepository;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
     * 初始化备用通道
     * @param channelId
     * @return
     */
    public ResponseData<Map<String, ConfigChannelRepairRuleValidator>> editRepairRule(String channelId) {
        Map<String, ConfigChannelRepairRuleValidator> map = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            ConfigChannelRepairRuleValidator info = new ConfigChannelRepairRuleValidator();
            map.put(""+i, info);
        }

        List<ConfigChannelRepairRuleValidator> list = channelRepairRepository.findByChannelIdAndBusinessType(channelId,"CHANNEL");

        if (!StringUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                map.put(""+i, list.get(i));
            }
        }

        return ResponseDataUtil.buildSuccess(map);
    }

    /**
     * 保存补发通道
     * @param configChannelRepairValidator
     * @param op
     * @return
     */
    @Transactional
    public ResponseData batchSave(ConfigChannelRepairValidator configChannelRepairValidator, String op) {

        //先删除在添加
        channelRepairRepository.deleteByChannelIdAndBusinessType(configChannelRepairValidator.getChannelId(),configChannelRepairValidator.getBusinessType());

        channelRepairRepository.batchSave(configChannelRepairValidator);

        //记录日志
        log.info("[失败补发通道管理][补发配置][{}]数据:{}",op, JSON.toJSONString(configChannelRepairValidator));

        return ResponseDataUtil.buildSuccess();
    }
}
