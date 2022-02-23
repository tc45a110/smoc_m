package com.smoc.cloud.configure.channel.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.configure.channel.entity.ConfigChannelChangeItem;
import com.smoc.cloud.configure.channel.repository.ConfigChannelChangeItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通道切换明细
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigChannelChangeItemService {


    @Resource
    private ConfigChannelChangeItemRepository configChannelChangeItemRepository;


    /**
     * 根据changeId 查询 有效的变更明细
     *
     * @param changeId
     * @return
     */
    public ResponseData<List<ConfigChannelChangeItem>> findChangeItems(String changeId) {

        List<ConfigChannelChangeItem> list = configChannelChangeItemRepository.findConfigChannelChangeItemByChangeIdAndStatus(changeId, "1");
        return ResponseDataUtil.buildSuccess(list);

    }

    /**
     * 根据changeId 查询 所有的变更明细
     *
     * @param changeId
     * @return
     */
    public ResponseData<List<ConfigChannelChangeItem>> findChangeAllItems(String changeId) {

        List<ConfigChannelChangeItem> list = configChannelChangeItemRepository.findConfigChannelChangeItemByChangeIdOrderByStatusDesc(changeId);
        return ResponseDataUtil.buildSuccess(list);

    }
}
