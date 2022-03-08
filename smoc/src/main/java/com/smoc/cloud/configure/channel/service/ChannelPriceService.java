package com.smoc.cloud.configure.channel.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendBusinessParamValueValidator;
import com.smoc.cloud.configure.channel.entity.ConfigChannelBasicInfo;
import com.smoc.cloud.configure.channel.repository.ChannelPriceRepository;
import com.smoc.cloud.configure.channel.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通道价格接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChannelPriceService {

    @Resource
    private ChannelPriceRepository channelPriceRepository;

    @Resource
    private ChannelRepository channelRepository;


    /**
     * 根据通道id和区域编号通道价格
     *
     * @param channelPriceValidator
     * @return
     */
    public ResponseData editChannelPrice(ChannelPriceValidator channelPriceValidator) {

        //根据通道id和区域查询已有的价格
        List<ChannelPriceValidator> list = channelPriceRepository.findChannelPrice(channelPriceValidator);

        //前台选择的区域
        Map<String, BigDecimal> map = new LinkedHashMap<>();
        String[] areaCode = channelPriceValidator.getAreaCode().split(",");
        for (int i = 0; i < areaCode.length; i++) {
            map.put(areaCode[i], null);
        }

        //循环已设定过价格的区域，赋值给选择的区域
        if (!StringUtils.isEmpty(list)) {
            for (ChannelPriceValidator channelPrice : list) {
                map.put(channelPrice.getAreaCode(), channelPrice.getChannelPrice());
            }
        }

        return ResponseDataUtil.buildSuccess(map);
    }

    /**
     * 批量保存通道价格
     * @param channelPriceValidator
     * @param op
     * @return
     */
    @Transactional
    public ResponseData batchSave(ChannelPriceValidator channelPriceValidator, String op) {

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        ConfigChannelBasicInfo channel = channelRepository.findById(channelPriceValidator.getChannelId()).get();

        //根据通道id和区域查询数据库已有的价格
        ChannelPriceValidator info = new ChannelPriceValidator();
        info.setChannelId(channelPriceValidator.getChannelId());
        info.setAreaCode(channel.getSupportAreaCodes());
        List<ChannelPriceValidator> list = channelPriceRepository.findChannelPrice(info);
        //转map
        Map<String, ChannelPriceValidator> map = new HashMap<>();
        if (!StringUtils.isEmpty(list) && list.size() > 0) {
            map = list.stream().collect(Collectors.toMap(ChannelPriceValidator::getAreaCode, Function.identity()));
        }

        //前台提交的区域和价格
        List<ChannelPriceValidator> priceList = channelPriceValidator.getPrices();
        Iterator<ChannelPriceValidator> it = priceList.iterator();
        while (it.hasNext()){
            ChannelPriceValidator submitPrice = (ChannelPriceValidator)it.next();
            ChannelPriceValidator priceMap = map.get(submitPrice.getAreaCode());

            //priceMap为空，代表是新添加的
            if(StringUtils.isEmpty(priceMap) ){
                submitPrice.setFlag("1");
            }

            //priceMap不为空：代表数据库存在
            if(!StringUtils.isEmpty(priceMap) ){
                //价格相等：代表没有改动，数据库不用执行
                if(submitPrice.getChannelPrice().compareTo(priceMap.getChannelPrice())==0){
                    it.remove();
                }else{
                    //价格有变动，需要修改更新日期
                    submitPrice.setFlag("2");
                    submitPrice.setId(priceMap.getId());
                }
            }
        }

        //先删除数据
        //channelPriceRepository.deleteByChannelId(channelPriceValidator.getChannelId());

        //批量保存
        channelPriceRepository.batchSave(channelPriceValidator);

        //设置通道完成进度
        Optional<ConfigChannelBasicInfo> optional = channelRepository.findById(channelPriceValidator.getChannelId());
        if(optional.isPresent()){
            ConfigChannelBasicInfo configChannelBasicInfo = optional.get();
            StringBuffer channelProcess = new StringBuffer(configChannelBasicInfo.getChannelProcess());
            channelProcess = channelProcess.replace(3, 4, "1");
            configChannelBasicInfo.setChannelProcess(channelProcess.toString());
            channelRepository.save(configChannelBasicInfo);
        }

        //记录日志
        log.info("[通道管理][区域计价配置][{}]数据:{}",op, JSON.toJSONString(channelPriceValidator));

        return ResponseDataUtil.buildSuccess();
    }

    public ResponseData findChannelPrice(ChannelPriceValidator channelPriceValidator) {

        List<ChannelPriceValidator> list = channelPriceRepository.findChannelPrice(channelPriceValidator);
        return ResponseDataUtil.buildSuccess(list);
    }
}
