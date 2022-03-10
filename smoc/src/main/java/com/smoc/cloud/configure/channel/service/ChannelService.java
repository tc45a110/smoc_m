package com.smoc.cloud.configure.channel.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.channel.entity.ConfigChannelBasicInfo;
import com.smoc.cloud.configure.channel.entity.ConfigChannelPrice;
import com.smoc.cloud.configure.channel.repository.ChannelPriceRepository;
import com.smoc.cloud.configure.channel.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

/**
 * 通道接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelService {

    @Resource
    private ChannelRepository channelRepository;

    @Resource
    private ChannelPriceRepository channelPriceRepository;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<ChannelBasicInfoQo> page(PageParams<ChannelBasicInfoQo> pageParams) {
        return channelRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<ConfigChannelBasicInfo> data = channelRepository.findById(id);
        if(!data.isPresent()){
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        ConfigChannelBasicInfo entity = data.get();
        ChannelBasicInfoValidator channelBasicInfoValidator = new ChannelBasicInfoValidator();
        BeanUtils.copyProperties(entity, channelBasicInfoValidator);

        //如果是统一计价，查询价格
        if ("UNIFIED_PRICE".equals(entity.getPriceStyle())) {
            ConfigChannelPrice configChannelPrice = channelPriceRepository.findByChannelId(entity.getChannelId());
            if(!StringUtils.isEmpty(configChannelPrice)){
                channelBasicInfoValidator.setChannelPrice(configChannelPrice.getChannelPrice());
            }
        }

        return ResponseDataUtil.buildSuccess(channelBasicInfoValidator);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findChannelById(String id) {
        Optional<ConfigChannelBasicInfo> data = channelRepository.findById(id);
        if(!data.isPresent()){
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        ConfigChannelBasicInfo entity = data.get();
        ChannelBasicInfoValidator channelBasicInfoValidator = new ChannelBasicInfoValidator();
        BeanUtils.copyProperties(entity, channelBasicInfoValidator);

        //区域范围是分省
        if ("PROVINCE".equals(entity.getBusinessAreaType())) {
            channelBasicInfoValidator.setProvince(entity.getSupportAreaCodes());
            channelBasicInfoValidator.setSupportAreaCodes("");
        }

        //如果是统一计价，查询价格
        if ("UNIFIED_PRICE".equals(entity.getPriceStyle())) {
            ConfigChannelPrice configChannelPrice = channelPriceRepository.findByChannelId(entity.getChannelId());
            if(!StringUtils.isEmpty(configChannelPrice)){
                channelBasicInfoValidator.setChannelPrice(configChannelPrice.getChannelPrice());
            }
        }

        //转换日期
        channelBasicInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(channelBasicInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param channelBasicInfoValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<ConfigChannelBasicInfo> save(ChannelBasicInfoValidator channelBasicInfoValidator, String op) {

        Iterable<ConfigChannelBasicInfo> data = channelRepository.findByChannelId(channelBasicInfoValidator.getChannelId());

        ConfigChannelBasicInfo entity = new ConfigChannelBasicInfo();
        BeanUtils.copyProperties(channelBasicInfoValidator, entity);

        String priceStyle = "";

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ConfigChannelBasicInfo info = (ConfigChannelBasicInfo) iter.next();
                priceStyle = info.getPriceStyle();
                if (!entity.getChannelId().equals(info.getChannelId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //区域范围是全国
        if ("COUNTRY".equals(channelBasicInfoValidator.getBusinessAreaType())) {
            entity.setSupportAreaCodes("ALL");
        }
        //区域范围是分省
        if ("PROVINCE".equals(channelBasicInfoValidator.getBusinessAreaType())) {
            entity.setSupportAreaCodes(channelBasicInfoValidator.getProvince());
            //如果第一次选择的是全国并且屏蔽省份有值，修改的时候改成了分省，那得把屏蔽省份设为空
            entity.setMaskProvince("");
        }

        //如果是统一计价，保存通道价格表
        saveChannelPrice(channelBasicInfoValidator, op);

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(channelBasicInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //设置通道完善进度：计价进度
        StringBuffer channelProcess = new StringBuffer(channelBasicInfoValidator.getChannelProcess());
        if ("UNIFIED_PRICE".equals(channelBasicInfoValidator.getPriceStyle())){
            channelProcess = channelProcess.replace(3, 4, "1");
        }else {
            if("edit".equals(op) && !priceStyle.equals(channelBasicInfoValidator.getPriceStyle())){
                channelProcess = channelProcess.replace(3, 4, "0");
            }
        }
        entity.setChannelProcess(channelProcess.toString());

        //记录日志
        log.info("[通道管理][通道基本信息][{}]数据:{}",op,JSON.toJSONString(entity));
        channelRepository.saveAndFlush(entity);

        //如果修改的时候，修改了区域数据
        if("AREA_PRICE".equals(channelBasicInfoValidator.getPriceStyle()) && "edit".equals(op)){
            //根据区域编号先删除库里多余的区域(比如：添加的时候选择了5个区域，修改的时候选择了3个区域，那么就得把多余的2个区域删除)
            channelPriceRepository.deleteByChannelIdAndAreaCode(channelBasicInfoValidator.getChannelId(), entity.getSupportAreaCodes());
        }

        return ResponseDataUtil.buildSuccess();
    }

    private void saveChannelPrice(ChannelBasicInfoValidator channelBasicInfoValidator, String op) {

        if ("UNIFIED_PRICE".equals(channelBasicInfoValidator.getPriceStyle())) {
            channelPriceRepository.deleteByChannelId(channelBasicInfoValidator.getChannelId());

            ConfigChannelPrice configChannelPrice = new ConfigChannelPrice();
            configChannelPrice.setId(UUID.uuid32());
            configChannelPrice.setChannelId(channelBasicInfoValidator.getChannelId());
            configChannelPrice.setPriceStyle(channelBasicInfoValidator.getPriceStyle());
            configChannelPrice.setChannelPrice(channelBasicInfoValidator.getChannelPrice());
            configChannelPrice.setAreaCode("ALL");
            configChannelPrice.setCreatedTime(new Date());
            configChannelPrice.setCreatedBy(channelBasicInfoValidator.getCreatedBy());

            //记录日志
            log.info("[通道管理][通道价格][{}]数据:{}",op,JSON.toJSONString(configChannelPrice));

            channelPriceRepository.saveAndFlush(configChannelPrice);
        }else {
            //这种情况是添加得时候选择得是统一计价并且添加到计价表里了，修改得时候改成分区计价了,这种情况就得把计价表里添加得统一计价删除
            channelPriceRepository.deleteByChannelIdAndPriceStyle(channelBasicInfoValidator.getChannelId(),"UNIFIED_PRICE");
        }
    }



}
