package com.smoc.cloud.identification.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import com.smoc.cloud.identification.remote.IdentificationOrdersInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 认证订单管理
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IdentificationOrdersInfoService {

    @Autowired
    private IdentificationOrdersInfoFeignClient identificationOrdersInfoFeignClient;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IdentificationOrdersInfoValidator>> page(PageParams<IdentificationOrdersInfoValidator> pageParams) {

        try {
            PageList<IdentificationOrdersInfoValidator> data = this.identificationOrdersInfoFeignClient.page(pageParams);
            return ResponseDataUtil.buildError(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData<IdentificationOrdersInfoValidator> findById(String id) {
        try {
            ResponseData<IdentificationOrdersInfoValidator> data = this.identificationOrdersInfoFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
