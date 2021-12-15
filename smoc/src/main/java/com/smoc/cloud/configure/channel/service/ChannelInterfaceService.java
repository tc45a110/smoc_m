package com.smoc.cloud.configure.channel.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelInterfaceValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.channel.entity.ConfigChannelInterface;
import com.smoc.cloud.configure.channel.repository.ChannelInterfaceRepository;
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

/**
 * 通道接口参数接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelInterfaceService {

    @Resource
    private ChannelInterfaceRepository channelInterfaceRepository;


    /**
     * 根据通道id获取接口参数
     * @param id
     * @return
     */
    public ResponseData findByChannelId(String id) {
        ConfigChannelInterface entity = channelInterfaceRepository.findByChannelId(id);

        ChannelInterfaceValidator channelInterfaceValidator = null;
        if(!StringUtils.isEmpty(entity)){
            channelInterfaceValidator = new ChannelInterfaceValidator();
            BeanUtils.copyProperties(entity, channelInterfaceValidator);
            //转换日期
            channelInterfaceValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));
        }

        return ResponseDataUtil.buildSuccess(channelInterfaceValidator);
    }

    /**
     * 保存或修改
     *
     * @param channelInterfaceValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<ConfigChannelInterface> save(ChannelInterfaceValidator channelInterfaceValidator, String op) {

        Iterable<ConfigChannelInterface> data = channelInterfaceRepository.findByIdAndChannelId(channelInterfaceValidator.getId(),channelInterfaceValidator.getChannelId());

        ConfigChannelInterface entity = new ConfigChannelInterface();
        BeanUtils.copyProperties(channelInterfaceValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ConfigChannelInterface info = (ConfigChannelInterface) iter.next();
                if (!entity.getChannelId().equals(info.getChannelId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(channelInterfaceValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[通道管理][通道接口参数][{}]数据:{}",op,JSON.toJSONString(entity));
        channelInterfaceRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }


}
