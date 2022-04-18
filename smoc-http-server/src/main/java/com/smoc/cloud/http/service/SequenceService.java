package com.smoc.cloud.http.service;


import com.smoc.cloud.http.repository.SequenceRepository;
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
    private SequenceRepository sequenceRepository;

    /**
     * 获取索引值
     * @param seqName
     * @return
     */
    public Integer findSequence(String seqName)  {
        //获取索引值
        return sequenceRepository.findSequence(seqName);
    }
}
