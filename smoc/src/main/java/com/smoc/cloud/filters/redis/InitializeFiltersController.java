package com.smoc.cloud.filters.redis;


import com.smoc.cloud.common.filters.utils.InitializeFiltersData;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


/**
 * 初始化过滤器数据
 */
@Slf4j
@RestController
@RequestMapping("initialize/filters")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class InitializeFiltersController {

    @Autowired
    private InitializeFiltersDataService initializeFiltersDataService;

    /**
     * @return
     */
    @RequestMapping(value = "/initialize", method = RequestMethod.POST)
    public ResponseData initialize(@RequestBody InitializeFiltersData initializeFiltersData) {

        initializeFiltersDataService.initialize(initializeFiltersData);
        return ResponseDataUtil.buildSuccess();
    }
}
