package com.smoc.cloud.route.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.route.RouteAuditMessageMtInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 待审批下发信息
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface RouteAuditMessageMtInfoFeignClient {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/route/message/mt/audit/page", method = RequestMethod.POST)
    ResponseData<PageList<RouteAuditMessageMtInfoValidator>> page(@RequestBody PageParams<RouteAuditMessageMtInfoValidator> pageParams) throws Exception;

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/route/message/mt/audit/findById/{id}", method = RequestMethod.GET)
    ResponseData<RouteAuditMessageMtInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     *  操作审核
     * @param routeAuditMessageMtInfoValidator
     * @param type
     * @return
     */
    @RequestMapping(value = "/route/message/mt/audit/check/{type}", method = RequestMethod.POST)
    ResponseData check(@RequestBody RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator,@PathVariable String type);

    /**
     * 统计条数
     * @param routeAuditMessageMtInfoValidator
     * @return
     */
    @RequestMapping(value = "/route/message/mt/audit/count", method = RequestMethod.POST)
    ResponseData<Map<String, Object>> count(@RequestBody RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator);
}
