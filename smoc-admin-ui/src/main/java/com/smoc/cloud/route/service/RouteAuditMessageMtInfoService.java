package com.smoc.cloud.route.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.route.RouteAuditMessageMtInfoValidator;
import com.smoc.cloud.common.smoc.route.qo.RouteAuditMessageAccountQo;
import com.smoc.cloud.route.remote.RouteAuditMessageMtInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 待审批下发信息
 */
@Slf4j
@Service
public class RouteAuditMessageMtInfoService {

    @Autowired
    private RouteAuditMessageMtInfoFeignClient routeAuditMessageMtInfoFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<RouteAuditMessageMtInfoValidator>> page(PageParams<RouteAuditMessageMtInfoValidator> pageParams) {

        try {
            ResponseData<PageList<RouteAuditMessageMtInfoValidator>> page = routeAuditMessageMtInfoFeignClient.page(pageParams);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    public ResponseData<RouteAuditMessageMtInfoValidator> findById(String id) {
        try {
            ResponseData<RouteAuditMessageMtInfoValidator> data = this.routeAuditMessageMtInfoFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 操作审核
     * @param routeAuditMessageMtInfoValidator
     * @param type
     * @return
     */
    public ResponseData check(RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator, String type) {
        try {
            ResponseData data = this.routeAuditMessageMtInfoFeignClient.check(routeAuditMessageMtInfoValidator,type);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 统计条数
     * @param routeAuditMessageMtInfoValidator
     * @return
     */
    public ResponseData<Map<String, Object>> count(RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator) {
        try {
            ResponseData<Map<String, Object>> data = this.routeAuditMessageMtInfoFeignClient.count(routeAuditMessageMtInfoValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 账号条数统计
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<RouteAuditMessageAccountQo>> accountPage(PageParams<RouteAuditMessageAccountQo> pageParams) {
        try {
            ResponseData<PageList<RouteAuditMessageAccountQo>> page = routeAuditMessageMtInfoFeignClient.accountPage(pageParams);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
