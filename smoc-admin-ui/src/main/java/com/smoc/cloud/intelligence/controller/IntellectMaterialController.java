package com.smoc.cloud.intelligence.controller;


import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.intelligence.service.IntellectMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

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
    private EnterpriseService enterpriseService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private IntellectMaterialService intellectMaterialService;

    /**
     * 根据业务账号查询素材分类（页面）
     *
     * @return
     */
    @RequestMapping(value = "/getMaterial/{parentId}/{materialTypeId}", method = RequestMethod.GET)
    public ModelAndView getMaterial(@PathVariable String parentId, @PathVariable String materialTypeId) {

        ResponseData<AccountBasicInfoValidator> accountData = businessAccountService.findById(parentId);

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(accountData.getData().getEnterpriseId());

        ModelAndView view = new ModelAndView("intelligence/resource/resource_list");
        ResponseData<List<IntellectMaterialValidator>> resourceData = intellectMaterialService.getMaterial(materialTypeId);
        if (!ResponseCode.SUCCESS.getCode().equals(resourceData.getCode())) {
            view.addObject("error", resourceData.getCode() + ":" + resourceData.getMessage());
            return view;
        }

        view.addObject("enterpriseName", enterpriseData.getData().getEnterpriseName());
        view.addObject("enterpriseId", enterpriseData.getData().getEnterpriseId());
        view.addObject("accountName", accountData.getData().getAccountName());
        view.addObject("accountId", accountData.getData().getAccountId());
        view.addObject("list", resourceData.getData());
        return view;
    }
}
