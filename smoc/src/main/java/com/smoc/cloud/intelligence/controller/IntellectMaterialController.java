package com.smoc.cloud.intelligence.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.intelligence.entity.IntellectMaterial;
import com.smoc.cloud.intelligence.service.IntellectMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 智能短信素材
 */
@Slf4j
@RestController
@RequestMapping("intel/material")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntellectMaterialController {

    @Autowired
    private IntellectMaterialService intellectMaterialService;

    /**
     * 查询素材
     *
     * @return
     */
    @RequestMapping(value = "/getMaterial/{materialTypeId}", method = RequestMethod.GET)
    public ResponseData<List<IntellectMaterial>> getMaterial(@PathVariable String materialTypeId) {

        return intellectMaterialService.findIntellectMaterialByMaterialTypeId(materialTypeId);

    }
}
