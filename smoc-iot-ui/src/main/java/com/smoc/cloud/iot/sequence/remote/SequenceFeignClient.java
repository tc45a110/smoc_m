package com.smoc.cloud.iot.sequence.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 序列管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface SequenceFeignClient {

    /**
     * 根据序列名称获取id值
     * @param seqName
     * @return
     */
    @RequestMapping(value = "/sequence/findSequence/{seqName}", method = RequestMethod.GET)
    Integer findSequence(@PathVariable String seqName);
}
