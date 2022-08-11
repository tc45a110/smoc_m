package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseSignCertifyValidator;
import com.smoc.cloud.customer.remote.EnterpriseSignCertifyFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EnterpriseSignCertifyService {

    @Autowired
    private EnterpriseSignCertifyFeignClient enterpriseSignCertifyFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<EnterpriseSignCertifyValidator>> page(PageParams<EnterpriseSignCertifyValidator> pageParams) {
        try {
            ResponseData<PageList<EnterpriseSignCertifyValidator>> responseData = this.enterpriseSignCertifyFeignClient.page(pageParams);
            return responseData;
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
    public ResponseData<EnterpriseSignCertifyValidator> findById(String id) {
        try {
            ResponseData<EnterpriseSignCertifyValidator> data = this.enterpriseSignCertifyFeignClient.findById(id);
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
    public ResponseData save(EnterpriseSignCertifyValidator enterpriseSignCertifyValidator, String op) {

        try {
            ResponseData data = this.enterpriseSignCertifyFeignClient.save(enterpriseSignCertifyValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销
     *
     * @param id
     * @return
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.enterpriseSignCertifyFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
