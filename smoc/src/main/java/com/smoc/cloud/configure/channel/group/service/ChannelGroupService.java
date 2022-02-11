package com.smoc.cloud.configure.channel.group.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroup;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroupInfo;
import com.smoc.cloud.configure.channel.group.repository.ChannelGroupRepository;
import com.smoc.cloud.configure.channel.group.repository.ConfigChannelGroupRepository;
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
 * 通道组接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelGroupService {

    @Resource
    private ChannelGroupRepository channelGroupRepository;

    @Resource
    private ConfigChannelGroupRepository channelGroupConfigRepository;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<ChannelGroupInfoValidator> page(PageParams<ChannelGroupInfoValidator> pageParams) {
        return channelGroupRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<ConfigChannelGroupInfo> data = channelGroupRepository.findById(id);
        if(!data.isPresent()){
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        ConfigChannelGroupInfo entity = data.get();
        ChannelGroupInfoValidator channelGroupInfoValidator = new ChannelGroupInfoValidator();
        BeanUtils.copyProperties(entity, channelGroupInfoValidator);

        //转换日期
        channelGroupInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(channelGroupInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param channelGroupInfoValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<ConfigChannelGroupInfo> save(ChannelGroupInfoValidator channelGroupInfoValidator, String op) {

        Iterable<ConfigChannelGroupInfo> data = channelGroupRepository.findByChannelGroupId(channelGroupInfoValidator.getChannelGroupId());

        ConfigChannelGroupInfo entity = new ConfigChannelGroupInfo();
        BeanUtils.copyProperties(channelGroupInfoValidator, entity);

        String carrier = "";
        String businessType = "";
        String infoType = "";

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ConfigChannelGroupInfo info = (ConfigChannelGroupInfo) iter.next();
                carrier = info.getCarrier();
                businessType = info.getBusinessType();
                infoType = info.getInfoType();
                if (!entity.getChannelGroupId().equals(info.getChannelGroupId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        if("edit".equals(op)){
            //如果该通道组已经配置了通道，不允许修改运营商、业务类型、信息分类
            List<ConfigChannelGroup> list = channelGroupConfigRepository.findByChannelGroupId(channelGroupInfoValidator.getChannelGroupId());
            if(!StringUtils.isEmpty(list) && list.size()>0){
                if(!carrier.equals(channelGroupInfoValidator.getCarrier()) || !businessType.equals(channelGroupInfoValidator.getBusinessType()) || !infoType.equals(channelGroupInfoValidator.getInfoType())){
                    return ResponseDataUtil.buildError("1000","通道组已配置了通道，不能修改运营商或业务类型或信息分类");
                }
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(channelGroupInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[通道组管理][通道组基本信息][{}]数据:{}",op,JSON.toJSONString(entity));
        channelGroupRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 通道组详情里已配置通道列表
     * @param channelGroupInfoValidator
     * @return
     */
    public ResponseData<List<ChannelBasicInfoQo>> centerConfigChannelList(ChannelGroupInfoValidator channelGroupInfoValidator) {
        List<ChannelBasicInfoQo> list = channelGroupRepository.centerConfigChannelList(channelGroupInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }
}
