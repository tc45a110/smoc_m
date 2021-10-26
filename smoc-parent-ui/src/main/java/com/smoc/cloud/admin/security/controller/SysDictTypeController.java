package com.smoc.cloud.admin.security.controller;

import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.admin.security.remote.service.DictTypeService;
import com.smoc.cloud.common.auth.qo.Nodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 公用字典接口
 * 2019/5/21 17:16
 **/
@Slf4j
@RestController
@RequestMapping("sysDictType")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SysDictTypeController {


    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    private SystemProperties systemProperties;


    /**
     * 根据dictType查询
     *
     * @param
     */
    @RequestMapping(value = "/getDictTree", method = RequestMethod.GET)
    public List<Nodes> getDictTree() {
        List<Nodes> data = dictTypeService.getDictTree(systemProperties.getSystemMarking());
        return data;
    }

}
