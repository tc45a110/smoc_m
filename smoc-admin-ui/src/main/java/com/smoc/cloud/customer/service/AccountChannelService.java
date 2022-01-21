package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.customer.remote.AccountChannelFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 账号通道配置管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountChannelService {

    @Autowired
    private AccountChannelFeignClient accountChannelFeignClient;

    /**
     * 查询配置的业务账号通道
     * @param accountChannelInfoQo
     * @return
     */
    public ResponseData<Map<String, AccountChannelInfoQo>> findAccountChannelConfig(AccountChannelInfoQo accountChannelInfoQo) {
        try {
            ResponseData<Map<String, AccountChannelInfoQo>> list = this.accountChannelFeignClient.findAccountChannelConfig(accountChannelInfoQo);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 检索通道列表
     * @param channelBasicInfoQo
     * @return
     */
    public ResponseData<List<ChannelBasicInfoQo>> findChannelList(ChannelBasicInfoQo channelBasicInfoQo) {
        try {
            ResponseData<List<ChannelBasicInfoQo>> list = this.accountChannelFeignClient.findChannelList(channelBasicInfoQo);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
