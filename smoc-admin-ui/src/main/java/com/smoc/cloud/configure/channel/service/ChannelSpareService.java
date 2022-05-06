package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelSpareChannelValidator;
import com.smoc.cloud.configure.channel.remote.ChannelSpareFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 补发通道管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelSpareService {

    @Autowired
    private ChannelSpareFeignClient channelSpareFeignClient;


    /**
     * 根据通道id获取备用通道
     * @param id
     * @return
     */
    public ResponseData<ConfigChannelSpareChannelValidator> findByChannelId(String id) {
        try {
            ResponseData<ConfigChannelSpareChannelValidator> data = this.channelSpareFeignClient.findByChannelId(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改通道接口参数
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(ConfigChannelSpareChannelValidator configChannelSpareChannelValidator, String op) {
        try {
            ResponseData data = this.channelSpareFeignClient.save(configChannelSpareChannelValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据原通道属性查询符合要求的备用通道
     * @param channelBasicInfoValidator
     * @return
     */
    public ResponseData<List<ConfigChannelSpareChannelValidator>> findSpareChannel(ChannelBasicInfoValidator channelBasicInfoValidator) {
        try {
            ResponseData<List<ConfigChannelSpareChannelValidator>> data = this.channelSpareFeignClient.findSpareChannel(channelBasicInfoValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除系统数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.channelSpareFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    public ResponseData<ConfigChannelSpareChannelValidator> findById(String id) {
        try {
            ResponseData<ConfigChannelSpareChannelValidator> data = this.channelSpareFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
