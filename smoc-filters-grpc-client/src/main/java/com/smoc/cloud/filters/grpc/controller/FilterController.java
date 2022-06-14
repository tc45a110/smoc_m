package com.smoc.cloud.filters.grpc.controller;

import com.smoc.cloud.filters.grpc.service.FiltersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description：TODO
 * @author: CKeen
 * @date: 2022/5/6 1:35 上午
 */
@RestController
public class FilterController {

    @Resource
    private FiltersService filtersService;

    @GetMapping("filter")
    public String filter() {
        String result = filtersService.filter();
        return result;
    }
}
