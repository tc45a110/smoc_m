package com.smoc.cloud.customer.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.customer.repository.AccountChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 业务账号通道配置管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountChannelService {

    @Resource
    private AccountChannelRepository accountChannelRepository;


    /**
     * 查询配置的业务账号通道
     *
     * @param accountChannelInfoQo
     * @return
     */
    public ResponseData<Map<String, AccountChannelInfoQo>> findAccountChannelConfig(AccountChannelInfoQo accountChannelInfoQo) {

        Map<String, AccountChannelInfoQo> map = new HashMap<>();
        String[] carrier = accountChannelInfoQo.getCarrier().split(",");
        for (int i = 0; i < carrier.length; i++) {
            map.put(carrier[i], null);
        }

        List<AccountChannelInfoQo> list = accountChannelRepository.findAccountChannelConfig(accountChannelInfoQo);
        //循环已配置的通道
        if (!StringUtils.isEmpty(list)) {
            for (AccountChannelInfoQo info : list) {
                map.put(accountChannelInfoQo.getCarrier(), info);
            }
        }

        return ResponseDataUtil.buildSuccess(map);
    }

    /**
     * 检索通道列表
     * @param channelBasicInfoQo
     * @return
     */
    public ResponseData<List<ChannelBasicInfoQo>> findChannelList(ChannelBasicInfoQo channelBasicInfoQo) {
        List<ChannelBasicInfoQo> list = accountChannelRepository.findChannelList(channelBasicInfoQo);
        return ResponseDataUtil.buildSuccess(list);
    }
}
