package com.smoc.cloud.route.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.route.RouteAuditMessageMtInfoValidator;
import com.smoc.cloud.common.smoc.route.qo.RouteAuditMessageAccountQo;
import com.smoc.cloud.common.smoc.template.AccountResourceInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.route.service.RouteAuditMessageMtInfoService;
import com.smoc.cloud.template.service.AccountResourceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * 待审批下发信息
 */
@Slf4j
@RestController
@RequestMapping("route/message/mt/audit")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class RouteAuditMessageInfoController {

    @Autowired
    private RouteAuditMessageMtInfoService routeAuditMessageMtInfoService;


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<RouteAuditMessageMtInfoValidator>> page(@RequestBody PageParams<RouteAuditMessageMtInfoValidator> pageParams) {

        return routeAuditMessageMtInfoService.page(pageParams);
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<RouteAuditMessageMtInfoValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return routeAuditMessageMtInfoService.findById(id);
    }

    /**
     *  操作审核
     * @param routeAuditMessageMtInfoValidator
     * @param type single:单条 many:批量
     * @return
     */
    @RequestMapping(value = "/check/{type}", method = RequestMethod.POST)
    public ResponseData check(@RequestBody RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator,@PathVariable String type) {

        return routeAuditMessageMtInfoService.check(routeAuditMessageMtInfoValidator,type);
    }

    /**
     * 统计条数
     * @param routeAuditMessageMtInfoValidator
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> count(@RequestBody RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator) {

        return routeAuditMessageMtInfoService.count(routeAuditMessageMtInfoValidator);
    }

    /**
     * 账号条数查询
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/accountPage", method = RequestMethod.POST)
    public ResponseData<PageList<RouteAuditMessageAccountQo>> accountPage(@RequestBody PageParams<RouteAuditMessageAccountQo> pageParams) {

        return routeAuditMessageMtInfoService.accountPage(pageParams);
    }
}
