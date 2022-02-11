package com.smoc.cloud.configure.channel.group.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupConfigValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroup;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroupInfo;
import com.smoc.cloud.configure.channel.group.repository.ChannelGroupRepository;
import com.smoc.cloud.configure.channel.group.repository.ConfigChannelGroupRepository;
import com.smoc.cloud.customer.repository.AccountChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Resource
    private ChannelGroupRepository channelGroupRepository;

    @Resource
    private AccountChannelRepository accountChannelRepository;

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
    @Transactional
    public ResponseData saveChannelGroupConfig(ChannelGroupConfigValidator channelGroupConfigValidator, String op) {

        ConfigChannelGroup entity = new ConfigChannelGroup();
        BeanUtils.copyProperties(channelGroupConfigValidator, entity);

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //设置通道组进度
        channelGroupRepository.updateChannelGroupProcessByChannelGroupId(entity.getChannelGroupId(),"11");

        if("add".equals(op)){
            //查询哪些业务账号在使用该通道组，需要把新的通道添加到业务账号里的通道组里
            ConfigChannelGroupInfo configChannelGroupInfo = channelGroupRepository.findById(entity.getChannelGroupId()).get();
            List<AccountChannelInfoQo> list = accountChannelRepository.findAccountChannelGroupByChannelGroupIdAndCarrierAndAccountId(configChannelGroupInfo.getChannelGroupId(),configChannelGroupInfo.getCarrier());
            if(!StringUtils.isEmpty(list) && list.size()>0){
                //批量添加通道到业务账号里
                accountChannelRepository.batchSaveAccountChannel(list,entity);
                log.info("[通道组管理][通道组配置][{}][给业务账号里的通道组添加通道]数据:{}",op,JSON.toJSONString(entity));
            }
        }

        if("edit".equals(op)){
            //如果修改了权重，需要把业务账号通道组里的通道权重修改
            Optional<ConfigChannelGroup> data = channelGroupConfigRepository.findById(channelGroupConfigValidator.getId());
            if(channelGroupConfigValidator.getChannelWeight()!=data.get().getChannelWeight()){
                accountChannelRepository.updateAccountChannelWeight(entity.getChannelGroupId(),entity.getChannelId(),entity.getChannelWeight());
                log.info("[通道组管理][通道组配置][{}][给业务账号里的通道组里的通道修改权重]数据:{}",op,JSON.toJSONString(entity));
            }

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
    @Transactional
    public ResponseData deleteById(String id) {
        ConfigChannelGroup data = channelGroupConfigRepository.findById(id).get();
        //记录日志
        log.info("[通道组管理][通道组配置][delete]数据:{}",JSON.toJSONString(data));
        channelGroupConfigRepository.deleteById(id);

        //如果已配置的通道组为空，设置通道组的进度
        ConfigChannelGroupQo configChannelGroupQo = new ConfigChannelGroupQo();
        configChannelGroupQo.setChannelGroupId(data.getChannelGroupId());
        List<ConfigChannelGroupQo> list = channelGroupConfigRepository.findConfigChannelGroupList(configChannelGroupQo);
        if(StringUtils.isEmpty(list) || list.size()<=0){
            channelGroupRepository.updateChannelGroupProcessByChannelGroupId(data.getChannelGroupId(),"10");
        }

        //移除通道的时候，需要把通道从业务账号通道组里也移除
        accountChannelRepository.deleteByChannelGroupIdAndChannelId(data.getChannelGroupId(),data.getChannelId());
        log.info("[通道组管理][通道组配置][delete][删除业务账号里的通道组里的通道]数据:{}",JSON.toJSONString(data));

        return ResponseDataUtil.buildSuccess();
    }
}
