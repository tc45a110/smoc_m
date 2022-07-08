package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.ConfigRouteContentRuleValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.ConfigRouteContentRule;
import com.smoc.cloud.customer.repository.ConfigRouteContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 账号内容路由接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigRouteContentService {

    @Resource
    private ConfigRouteContentRepository configRouteContentRepository;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public PageList<ConfigRouteContentRuleValidator> page(PageParams<ConfigRouteContentRuleValidator> pageParams) {
        return configRouteContentRepository.page(pageParams);
    }

    /**
     * 业务账号列表
     * @param pageParams
     * @return
     */
    public PageList<AccountContentRepairQo> accountList(PageParams<AccountContentRepairQo> pageParams) {
        return configRouteContentRepository.accountList(pageParams);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<ConfigRouteContentRule> data = configRouteContentRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        ConfigRouteContentRule entity = data.get();
        ConfigRouteContentRuleValidator configContentRepairRuleValidator = new ConfigRouteContentRuleValidator();
        BeanUtils.copyProperties(entity, configContentRepairRuleValidator);

        //转换日期
        configContentRepairRuleValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(configContentRepairRuleValidator);
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    public ResponseData save(ConfigRouteContentRuleValidator configContentRepairRuleValidator, String op) {
        //转BaseUser存放对象
        ConfigRouteContentRule entity = new ConfigRouteContentRule();
        BeanUtils.copyProperties(configContentRepairRuleValidator, entity);

        List<ConfigRouteContentRule> data = configRouteContentRepository.findByAccountIdAndCarrierAndRouteContentAndRouteStatus(configContentRepairRuleValidator.getAccountId(),configContentRepairRuleValidator.getCarrier(),configContentRepairRuleValidator.getRouteContent(),"1");

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError("改业务下已经存在路由通道！");
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ConfigRouteContentRule organization = (ConfigRouteContentRule) iter.next();
                if (!entity.getId().equals(organization.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(configContentRepairRuleValidator.getCreatedTime()));

        //记录日志
        log.info("[内容路由配置][路由配置][{}]数据:{}",op, JSON.toJSONString(configContentRepairRuleValidator));

        configRouteContentRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据ID 删除
     * @param id
     * @return
     */
    @Transactional
    public ResponseData deleteById(String id) {
        ConfigRouteContentRule data = configRouteContentRepository.findById(id).get();
        //记录日志
        log.info("[内容路由配置][delete]数据:{}",JSON.toJSONString(data));
        configRouteContentRepository.updateStatusById(id);

        return ResponseDataUtil.buildSuccess();
    }

    public ResponseData findContentRepair(String accountId, String carrier) {
        ConfigRouteContentRule entity = configRouteContentRepository.findByAccountIdAndCarrier(accountId,carrier);
        if(!StringUtils.isEmpty(entity)){
            ConfigRouteContentRuleValidator configContentRepairRuleValidator = new ConfigRouteContentRuleValidator();
            BeanUtils.copyProperties(entity, configContentRepairRuleValidator);

            //转换日期
            configContentRepairRuleValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));
            return ResponseDataUtil.buildSuccess(configContentRepairRuleValidator);
        }

        return ResponseDataUtil.buildSuccess();
    }
}
