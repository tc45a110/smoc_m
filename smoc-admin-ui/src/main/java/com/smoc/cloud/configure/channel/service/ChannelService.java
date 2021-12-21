package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.configure.channel.remote.ChannelFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;



/**
 * 通道管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelService {

    @Autowired
    private ChannelFeignClient channelFeignClient;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ChannelBasicInfoQo>> page(PageParams<ChannelBasicInfoQo> pageParams) {
        try {
            PageList<ChannelBasicInfoQo> pageList = this.channelFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
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
    public ResponseData<ChannelBasicInfoValidator> findChannelById(String id) {
        try {
            ResponseData<ChannelBasicInfoValidator> data = this.channelFeignClient.findChannelById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(ChannelBasicInfoValidator channelBasicInfoValidator, String op) {

        try {
            ResponseData data = this.channelFeignClient.save(channelBasicInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param channelId
     * @return
     */
    public ResponseData<ChannelBasicInfoValidator> findById(String channelId) {
        try {
            ResponseData<ChannelBasicInfoValidator> data = this.channelFeignClient.findById(channelId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
