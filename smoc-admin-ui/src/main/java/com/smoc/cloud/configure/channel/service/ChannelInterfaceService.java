package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelInterfaceValidator;
import com.smoc.cloud.configure.channel.remote.ChannelInterfaceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * 通道接口参数管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelInterfaceService {

    @Autowired
    private ChannelInterfaceFeignClient channelInterfaceFeignClient;


    /**
     * 根据通道id获取通道接口参数
     * @param id
     * @return
     */
    public ResponseData<ChannelInterfaceValidator> findChannelInterfaceByChannelId(String id) {
        try {
            ResponseData<ChannelInterfaceValidator> data = this.channelInterfaceFeignClient.findChannelInterfaceByChannelId(id);
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
    public ResponseData interfaceSave(ChannelInterfaceValidator channelInterfaceValidator, String op) {
        try {
            ResponseData data = this.channelInterfaceFeignClient.interfaceSave(channelInterfaceValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
