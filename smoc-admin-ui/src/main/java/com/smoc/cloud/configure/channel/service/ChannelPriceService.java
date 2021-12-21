package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.configure.channel.remote.ChannelPriceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 通道价格管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelPriceService {

    @Autowired
    private ChannelPriceFeignClient channelPriceFeignClient;

    /**
     * 根据通道id和区域编号查询通道价格
     * @param channelPriceValidator
     * @return
     */
    public ResponseData<Map<String, BigDecimal>> editChannelPrice(ChannelPriceValidator channelPriceValidator) {
        try {
            ResponseData<Map<String, BigDecimal>> list = this.channelPriceFeignClient.editChannelPrice(channelPriceValidator);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 区域计价保存
     * @param channelPriceValidator
     * @param op
     * @return
     */
    public ResponseData savePrice(ChannelPriceValidator channelPriceValidator, String op) {
        try {
            ResponseData data = this.channelPriceFeignClient.savePrice(channelPriceValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据通道id和区域查询价格
     * @param channelPriceValidator
     * @return
     */
    public ResponseData<List<ChannelPriceValidator>> findChannelPrice(ChannelPriceValidator channelPriceValidator) {
        try {
            ResponseData<List<ChannelPriceValidator>> list = this.channelPriceFeignClient.findChannelPrice(channelPriceValidator);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
