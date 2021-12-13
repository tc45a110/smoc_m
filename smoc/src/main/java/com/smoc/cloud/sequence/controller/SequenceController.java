package com.smoc.cloud.sequence.controller;


import com.smoc.cloud.sequence.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

/**
 * 序列管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/sequence")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SequenceController {

    @Autowired
    private SequenceService sequenceService;

    /**
     *  根据序列名称获取id值
     * @param seqName
     * @return
     */
    @RequestMapping(value = "/findSequence/{seqName}", method = RequestMethod.GET)
    public Integer findSequence(@PathVariable String seqName) {

        return sequenceService.findSequence(seqName);
    }

}
