package com.smoc.cloud.parameter.errorcode.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import com.smoc.cloud.parameter.errorcode.remote.SystemErrorCodeFeignClient;
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

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    public ResponseData<SystemErrorCodeValidator> findById(String id) {
        try {
            ResponseData data = this.systemErrorCodeFeignClient.findById(id);
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
    public ResponseData save(SystemErrorCodeValidator systemErrorCodeValidator, String op) {
        try {
            ResponseData data = this.systemErrorCodeFeignClient.save(systemErrorCodeValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.systemErrorCodeFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 批量保存
     * @param systemErrorCodeValidator
     * @return
     */
    public ResponseData batchSave(SystemErrorCodeValidator systemErrorCodeValidator) {
        try {
            this.systemErrorCodeFeignClient.bathSave(systemErrorCodeValidator);
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
