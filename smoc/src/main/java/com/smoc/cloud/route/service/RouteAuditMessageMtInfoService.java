package com.smoc.cloud.route.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.route.RouteAuditMessageMtInfoValidator;
import com.smoc.cloud.route.repository.RouteAuditMessageMtInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 待审批下发信息
 */
@Slf4j
@Service
public class RouteAuditMessageMtInfoService {

    @Resource
    private RouteAuditMessageMtInfoRepository routeAuditMessageMtInfoRepository;


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<RouteAuditMessageMtInfoValidator>> page(PageParams<RouteAuditMessageMtInfoValidator> pageParams) {
        PageList<RouteAuditMessageMtInfoValidator> data = routeAuditMessageMtInfoRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 根据ID 查询
     * @param id
     * @return
     */
    public ResponseData<RouteAuditMessageMtInfoValidator> findById(String id) {
        RouteAuditMessageMtInfoValidator data = routeAuditMessageMtInfoRepository.findById(id);

        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 操作审核
     * @param routeAuditMessageMtInfoValidator
     * @param type single:单条 many:批量
     * @return
     */
    public ResponseData check(RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator, String type) {

        //单条审批
        if("single".equals(type)){
            routeAuditMessageMtInfoRepository.updateAuditFlagById(routeAuditMessageMtInfoValidator.getAuditFlag(),routeAuditMessageMtInfoValidator.getId());
        }

        //批量审批
        if("many".equals(type)){
            routeAuditMessageMtInfoRepository.updateAuditFlagByMessageMd5(routeAuditMessageMtInfoValidator.getAuditFlag(),routeAuditMessageMtInfoValidator.getMessageMd5());
        }

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 统计条数
     * @param routeAuditMessageMtInfoValidator
     * @return
     */
    public ResponseData<Map<String, Object>> count(RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator) {
        Map<String, Object> map = routeAuditMessageMtInfoRepository.count(routeAuditMessageMtInfoValidator);
        return ResponseDataUtil.buildSuccess(map);
    }
}
