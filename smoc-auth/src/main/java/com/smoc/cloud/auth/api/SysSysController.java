package com.smoc.cloud.auth.api;


import com.smoc.cloud.auth.data.provider.entity.BaseSystem;
import com.smoc.cloud.auth.data.provider.service.BaseSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * 字典管理系统接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/system")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SysSysController {

    @Autowired
    private BaseSystemService baseSystemService;

    /**
     * 查询系统字典
     * @return
     */
    @PreAuthorize("hasAuthority('SUPER-ROLE')")
    @RequestMapping(value = "/getSystemList", method = RequestMethod.GET)
    public Map<String, BaseSystem> getSystemList() {

        Map<String, BaseSystem> data = baseSystemService.getSystemList();
        return data;
    }

}
