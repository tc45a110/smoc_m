package com.smoc.cloud.configure.channel.group.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupConfigValidator;
import com.smoc.cloud.configure.channel.group.remote.ConfigChannelGroupFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 配置通道组管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigChannelGroupService {

    @Autowired
    private ConfigChannelGroupFeignClient configChannelGroupFeignClient;

    /**
     * 查询通道列表
     * @param channelBasicInfoQo
     * @return
     */
    public ResponseData<List<ChannelBasicInfoQo>> findChannelList(ChannelBasicInfoQo channelBasicInfoQo) {
        try {
            ResponseData<List<ChannelBasicInfoQo>> list = this.configChannelGroupFeignClient.findChannelList(channelBasicInfoQo);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存通道组配置
     * @param channelGroupConfigValidator
     * @param op
     * @return
     */
    public ResponseData saveChannelGroupConfig(ChannelGroupConfigValidator channelGroupConfigValidator, String op) {
        try {
            ResponseData data = this.configChannelGroupFeignClient.saveChannelGroupConfig(channelGroupConfigValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询已配置的通道
     * @param configChannelGroupQo
     * @return
     */
    public ResponseData<List<ConfigChannelGroupQo>> findConfigChannelGroupList(ConfigChannelGroupQo configChannelGroupQo) {
        try {
            ResponseData<List<ConfigChannelGroupQo>> list = this.configChannelGroupFeignClient.findConfigChannelGroupList(configChannelGroupQo);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 移除已配置通道
     * @param id
     * @return
     */
    public ResponseData deleteConfigChannelById(String id) {
        try {
            ResponseData data = this.configChannelGroupFeignClient.deleteConfigChannelById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData<ConfigChannelGroupQo> findConfigChannelById(String id) {
        try {
            ResponseData<ConfigChannelGroupQo> data = this.configChannelGroupFeignClient.findConfigChannelById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
