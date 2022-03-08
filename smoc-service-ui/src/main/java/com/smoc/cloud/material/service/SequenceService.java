package com.smoc.cloud.material.service;


import com.smoc.cloud.material.remote.SequenceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SequenceService {

    @Resource
    private SequenceFeignClient sequenceFeignClient;

    /**
     * 根据序列名称获取id值
     *
     * @param seqName
     * @return
     */
    public Integer findSequence(String seqName) {

        return sequenceFeignClient.findSequence(seqName);
    }

}
