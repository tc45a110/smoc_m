package com.smoc.cloud.scheduler.service.channel;

import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.initialize.model.AccountChannelBusinessModel;
import com.smoc.cloud.scheduler.initialize.model.ContentRouteBusinessModel;
import com.smoc.cloud.scheduler.initialize.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    /**
     * 内容通道路由
     *
     * @param accountId
     * @param phoneNumber
     * @param carrier
     * @param areaCode
     * @param content
     * @return
     */
    public String contentRouteChannel(String accountId, String phoneNumber, String carrier, String areaCode, String content) {

        ContentRouteBusinessModel contentRouteBusinessModel = Reference.contentRouteBusinessModels.get(accountId);
        if (null == contentRouteBusinessModel) {
            return null;
        }

        String contentRouteChannelId = contentRouteBusinessModel.route(carrier, areaCode, phoneNumber, content);
        return contentRouteChannelId;
    }

    /**
     * 业务账号通道路由
     *
     * @param accountId
     * @param carrier
     * @param areaCode
     * @return
     */
    public String accountRouteChannel(String accountId, String carrier, String areaCode) {
        String channelId = null;
        AccountChannelBusinessModel accountChannelBusinessModel = Reference.accountChannelBusinessModels.get(accountId);
        if (null == accountChannelBusinessModel) {
            return null;
        }
        channelId = accountChannelBusinessModel.getChannelId(carrier, areaCode);
        return channelId;
    }

    /**
     * 构建业务账号-通道业务模型
     *
     * @return
     */
    public Map<String, AccountChannelBusinessModel> getAccountChannelBusinessModels() {
        Map<String, AccountChannelBusinessModel> resultMap = new HashMap<>();
        Map<String, AccountChannelBusinessModel> accountChannels = channelRepository.getAccountChannels();
        for (String key : accountChannels.keySet()) {
            AccountChannelBusinessModel accountChannelBusinessModel = accountChannels.get(key);
            accountChannelBusinessModel.builder();
            resultMap.put(key, accountChannelBusinessModel);
        }
        return resultMap;
    }
}
