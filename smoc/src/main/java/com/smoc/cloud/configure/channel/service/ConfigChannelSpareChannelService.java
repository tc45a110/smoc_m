package com.smoc.cloud.configure.channel.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelSpareChannelValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.channel.entity.ConfigChannelSpareChannel;
import com.smoc.cloud.configure.channel.repository.ConfigChannelSpareChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 备用通道参数接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigChannelSpareChannelService {

    @Resource
    private ConfigChannelSpareChannelRepository configChannelSpareChannelRepository;


    /**
     *  根据通道id查询
     * @param id
     * @return
     */
    public ResponseData findByChannelId(String id) {
        ConfigChannelSpareChannel entity = configChannelSpareChannelRepository.findByChannelId(id);

        ConfigChannelSpareChannelValidator configChannelSpareChannel = null;
        if(!StringUtils.isEmpty(entity)){
            configChannelSpareChannel = new ConfigChannelSpareChannelValidator();
            BeanUtils.copyProperties(entity, configChannelSpareChannel);
            //转换日期
            configChannelSpareChannel.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));
        }

        return ResponseDataUtil.buildSuccess(configChannelSpareChannel);
    }

    /**
     * 保存或修改
     *
     * @param configChannelSpareChannelValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<ConfigChannelSpareChannel> save(ConfigChannelSpareChannelValidator configChannelSpareChannelValidator, String op) {

        Iterable<ConfigChannelSpareChannel> data = configChannelSpareChannelRepository.findByChannelIdAndSpareChannelId(configChannelSpareChannelValidator.getChannelId(),configChannelSpareChannelValidator.getSpareChannelId());

        ConfigChannelSpareChannel entity = new ConfigChannelSpareChannel();
        BeanUtils.copyProperties(configChannelSpareChannelValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ConfigChannelSpareChannel info = (ConfigChannelSpareChannel) iter.next();
                if (!entity.getId().equals(info.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(configChannelSpareChannelValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[通道管理][备用通道配置][{}]数据:{}",op,JSON.toJSONString(entity));
        configChannelSpareChannelRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据原通道属性查询符合要求的备用通道
     * @param channelBasicInfoValidator
     * @return
     */
    public ResponseData<List<ConfigChannelSpareChannelValidator>> findSpareChannel(ChannelBasicInfoValidator channelBasicInfoValidator) {

        List<ConfigChannelSpareChannelValidator> list = configChannelSpareChannelRepository.findSpareChannel(channelBasicInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Transactional
    public ResponseData deleteById(String id) {

        ConfigChannelSpareChannel data = configChannelSpareChannelRepository.findById(id).get();
        //记录日志
        log.info("[通道管理][备用通道配置][delete]数据:{}",JSON.toJSONString(data));
        configChannelSpareChannelRepository.deleteById(id);


        return ResponseDataUtil.buildSuccess();
    }

    public ResponseData findById(String id) {
        Optional<ConfigChannelSpareChannel> entity = configChannelSpareChannelRepository.findById(id);

        ConfigChannelSpareChannelValidator configChannelSpareChannel = null;
        if(entity.isPresent()){
            configChannelSpareChannel = new ConfigChannelSpareChannelValidator();
            BeanUtils.copyProperties(entity.get(), configChannelSpareChannel);
            //转换日期
            configChannelSpareChannel.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.get().getCreatedTime()));
        }

        return ResponseDataUtil.buildSuccess(configChannelSpareChannel);
    }

}
