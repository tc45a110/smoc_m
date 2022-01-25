package com.smoc.cloud.identification.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.identification.remote.IdentificationRequestDataFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 认证请求数据
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IdentificationRequestDataService {

    @Autowired
    private IdentificationRequestDataFeignClient identificationRequestDataFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IdentificationRequestDataValidator>> page(PageParams<IdentificationRequestDataValidator> pageParams) {

        try {
            PageList<IdentificationRequestDataValidator> data = this.identificationRequestDataFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<IdentificationRequestDataValidator> findById(String id) {
        try {
            ResponseData<IdentificationRequestDataValidator> data = this.identificationRequestDataFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
