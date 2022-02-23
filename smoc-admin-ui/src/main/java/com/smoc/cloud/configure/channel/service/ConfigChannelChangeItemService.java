package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeItemValidator;
import com.smoc.cloud.configure.channel.remote.ConfigChannelChangeItemFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通道切换明细
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigChannelChangeItemService {

    @Autowired
    private ConfigChannelChangeItemFeignClient configChannelChangeItemFeignClient;

    /**
     * 查询 有效数据
     *
     * @param changeId
     * @return
     */
    public ResponseData<List<ConfigChannelChangeItemValidator>> findChangeItems(String changeId) {
        try {
            ResponseData responseData = this.configChannelChangeItemFeignClient.findChangeItems(changeId);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询 所有数据
     *
     * @param changeId
     * @return
     */
    public ResponseData<List<ConfigChannelChangeItemValidator>> findChangeAllItems(String changeId) {
        try {
            ResponseData responseData = this.configChannelChangeItemFeignClient.findChangeAllItems(changeId);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
