package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.configure.channel.remote.ChannelRepairFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 通道失败补发管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelRepairService {

    @Autowired
    private ChannelRepairFeignClient channelRepairFeignClient;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ConfigChannelRepairValidator>> page(PageParams<ConfigChannelRepairValidator> pageParams) {
        try {
            PageList<ConfigChannelRepairValidator> pageList = this.channelRepairFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据运营商、业务类型、信息分类查询符合要求的备用通道
     * @param channelBasicInfoValidator
     * @return
     */
    public ResponseData<List<ConfigChannelRepairValidator>> findSpareChannel(ChannelBasicInfoValidator channelBasicInfoValidator) {
        try {
            ResponseData<List<ConfigChannelRepairValidator>> list = this.channelRepairFeignClient.findSpareChannel(channelBasicInfoValidator);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public ResponseData<ConfigChannelRepairRuleValidator> findById(String id) {
        try {
            ResponseData<ConfigChannelRepairRuleValidator> data = this.channelRepairFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据ID 删除
     * @param id
     * @return
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.channelRepairFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存补发通道
     * @param configChannelRepairRuleValidator
     * @param op
     * @return
     */
    public ResponseData save(ConfigChannelRepairRuleValidator configChannelRepairRuleValidator, String op) {
        try {
            ResponseData data = this.channelRepairFeignClient.save(configChannelRepairRuleValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询已经存在的备用通道
     * @param configChannelRepairRuleValidator
     * @return
     */
    public ResponseData<List<ConfigChannelRepairRuleValidator>> findChannelRepairByChannelId(ConfigChannelRepairRuleValidator configChannelRepairRuleValidator) {
        try {
            ResponseData<List<ConfigChannelRepairRuleValidator>> list = this.channelRepairFeignClient.findChannelRepairByChannelId(configChannelRepairRuleValidator);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


}
