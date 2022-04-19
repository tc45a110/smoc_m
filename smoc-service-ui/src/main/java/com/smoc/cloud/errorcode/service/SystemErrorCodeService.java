package com.smoc.cloud.errorcode.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import com.smoc.cloud.errorcode.remote.SystemErrorCodeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * 错误码管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemErrorCodeService {

    @Autowired
    private SystemErrorCodeFeignClient systemErrorCodeFeignClient;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<SystemErrorCodeValidator>> page(PageParams<SystemErrorCodeValidator> pageParams) {
        try {
            PageList<SystemErrorCodeValidator> pageList = this.systemErrorCodeFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
