package com.smoc.cloud.auth.api;


import com.smoc.cloud.auth.data.provider.service.BaseCommDictService;
import com.smoc.cloud.common.auth.qo.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * 字典管理系统接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/sysDict")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SysDictController {

    @Autowired
    private BaseCommDictService baseCommDictService;

    /**
     * 查询系统字典
     *
     * @param system 系统标识
     * @return
     */
    @PreAuthorize("hasAuthority('SUPER-ROLE')")
    @RequestMapping(value = "/getDict/{system}", method = RequestMethod.GET)
    public Map<String, List<Dict>> baseCommDictService(@PathVariable String system) {

        Map<String, List<Dict>> data = baseCommDictService.getDict(system);
        return data;
    }

}
