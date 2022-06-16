package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigRepairRuleValidator;
import com.smoc.cloud.configure.channel.remote.ConfigRepairRuleFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;



/**
 * 失败补发规则管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigRepairRuleService {

    @Autowired
    private ConfigRepairRuleFeignClient configRepairRuleFeignClient;


    /**
     * 根据id查询
     * @param id
     * @return
     */
    public ResponseData<ConfigRepairRuleValidator> findById(String id) {
        try {
            ResponseData<ConfigRepairRuleValidator> data = this.configRepairRuleFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存补发通道
     * @param configRepairRuleValidator
     * @param op
     * @return
     */
    public ResponseData save(ConfigRepairRuleValidator configRepairRuleValidator, String op) {
        try {
            ResponseData data = this.configRepairRuleFeignClient.save(configRepairRuleValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
