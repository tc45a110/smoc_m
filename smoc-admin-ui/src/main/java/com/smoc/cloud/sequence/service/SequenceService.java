package com.smoc.cloud.sequence.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendSystemParamValueValidator;
import com.smoc.cloud.parameter.remote.ParameterExtendSystemParamValueFeignClient;
import com.smoc.cloud.sequence.remote.SequenceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SequenceService {

    @Resource
    private SequenceFeignClient sequenceFeignClient;

    @Resource
    private ParameterExtendSystemParamValueFeignClient parameterExtendSystemParamValueFeignClient;

    /**
     * 根据序列名称获取id值
     *
     * @param seqName
     * @return
     */
    public Integer findSequence(String seqName) {

        return sequenceFeignClient.findSequence(seqName);
    }

    /**
     * 封装ID前缀
     * @param channel
     * @param businessType
     * @return
     */
    public String getPrefixId(String channel, String businessType) {

        StringBuilder prefixId = new StringBuilder();

        //查询系统前缀
        ResponseData<ParameterExtendSystemParamValueValidator> data = parameterExtendSystemParamValueFeignClient.findByBusinessTypeAndBusinessIdAndParamKey("SYSTEM_PARAM", "SYSTEM", "ID_PREFIX");
        ParameterExtendSystemParamValueValidator systemParam = data.getData();
        if (!StringUtils.isEmpty(systemParam)) {
            prefixId.append(systemParam.getParamValue());
        }

        Integer seq = null;

        //通道
        if ("CHANNEL".equals(channel)) {
            prefixId.append("CH");
            //查询索引
            seq = sequenceFeignClient.findSequence(businessType);
        }

        //通道组
        if ("CHANNEL_GROUP".equals(channel)) {
            prefixId.append("PR");
            //查询索引
            seq = sequenceFeignClient.findSequence(channel);
        }

        //业务账号
        if ("BUSINESS_ACCOUNT".equals(channel)) {
            prefixId.append("BA");
            //查询索引
            seq = sequenceFeignClient.findSequence(channel);
        }

        /*//运营商第一个字母
        String[] carriers = carrier.split(",");
        for (int i = 0; i < carriers.length; i++) {
            String type = carriers[i];
            prefixId.append(type.substring(0, 1));
        }*/

        if(!StringUtils.isEmpty(seq)){
            prefixId.append(seq);
        }

        return prefixId.toString();
    }
}
