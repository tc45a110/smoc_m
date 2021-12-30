package com.smoc.cloud.configure.channel.group.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupConfigValidator;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroup;
import com.smoc.cloud.configure.channel.group.repository.ConfigChannelGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 配置通道组接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigChannelGroupService {

    @Resource
    private ConfigChannelGroupRepository channelGroupConfigRepository;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {

        Optional<ConfigChannelGroup> data = channelGroupConfigRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 查询通道列表
     * @param channelBasicInfoQo
     * @return
     */
    public ResponseData<List<ChannelBasicInfoQo>> findChannelList(ChannelBasicInfoQo channelBasicInfoQo) {
        List<ChannelBasicInfoQo> list = channelGroupConfigRepository.findChannelList(channelBasicInfoQo);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 查询已配置的通道
     * @param configChannelGroupQo
     * @return
     */
    public ResponseData<List<ConfigChannelGroupQo>> findConfigChannelGroupList(ConfigChannelGroupQo configChannelGroupQo) {
        List<ConfigChannelGroupQo> list = channelGroupConfigRepository.findConfigChannelGroupList(configChannelGroupQo);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 保存通道组配置
     * @param channelGroupConfigValidator
     * @param op
     * @return
     */
    public ResponseData saveChannelGroupConfig(ChannelGroupConfigValidator channelGroupConfigValidator, String op) {

        ConfigChannelGroup entity = new ConfigChannelGroup();
        BeanUtils.copyProperties(channelGroupConfigValidator, entity);

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[通道组管理][通道组配置][{}]数据:{}",op,JSON.toJSONString(entity));
        channelGroupConfigRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 移除已配置通道
     * @param id
     * @return
     */
    public ResponseData deleteById(String id) {
        ConfigChannelGroup data = channelGroupConfigRepository.findById(id).get();
        //记录日志
        log.info("[通道组管理][通道组配置][delete]数据:{}",JSON.toJSONString(data));
        channelGroupConfigRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }
}
