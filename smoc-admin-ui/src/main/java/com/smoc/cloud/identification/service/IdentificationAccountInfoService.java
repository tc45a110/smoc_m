package com.smoc.cloud.identification.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.identification.remote.IdentificationAccountInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * 认账账号管理
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IdentificationAccountInfoService {


    @Autowired
    private IdentificationAccountInfoFeignClient identificationAccountInfoFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IdentificationAccountInfoValidator>> page(PageParams<IdentificationAccountInfoValidator> pageParams) {

        try {
            PageList<IdentificationAccountInfoValidator> data = this.identificationAccountInfoFeignClient.page(pageParams);
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
    public ResponseData<IdentificationAccountInfoValidator> findById(String id) {
        try {
            ResponseData<IdentificationAccountInfoValidator> data = this.identificationAccountInfoFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(IdentificationAccountInfoValidator identificationAccountInfoValidator, String op) {

        try {
            ResponseData data = this.identificationAccountInfoFeignClient.save(identificationAccountInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销认证账户
     *
     * @param id
     * @return
     */
    public ResponseData logout(String id) {
        try {
            ResponseData data = this.identificationAccountInfoFeignClient.logout(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
