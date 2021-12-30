package com.smoc.cloud.configure.channel.group.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.configure.channel.group.remote.ChannelGroupFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;



/**
 * 通道组管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelGroupService {

    @Autowired
    private ChannelGroupFeignClient channelGroupFeignClient;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ChannelGroupInfoValidator>> page(PageParams<ChannelGroupInfoValidator> pageParams) {
        try {
            PageList<ChannelGroupInfoValidator> pageList = this.channelGroupFeignClient.page(pageParams);
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
    public ResponseData<ChannelGroupInfoValidator> findById(String id) {
        try {
            ResponseData<ChannelGroupInfoValidator> data = this.channelGroupFeignClient.findById(id);
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
    public ResponseData save(ChannelGroupInfoValidator channelGroupInfoValidator, String op) {

        try {
            ResponseData data = this.channelGroupFeignClient.save(channelGroupInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
