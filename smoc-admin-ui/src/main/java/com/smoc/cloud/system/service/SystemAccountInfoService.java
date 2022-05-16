package com.smoc.cloud.system.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import com.smoc.cloud.system.remote.SystemAccountInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 共用账号管理
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemAccountInfoService {

    @Autowired
    private SystemAccountInfoFeignClient systemAccountInfoFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<SystemAccountInfoValidator>> page(PageParams<SystemAccountInfoValidator> pageParams) {

        try {
            PageList<SystemAccountInfoValidator> data = this.systemAccountInfoFeignClient.page(pageParams);
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
    public ResponseData<SystemAccountInfoValidator> findById(String id) {
        try {
            ResponseData<SystemAccountInfoValidator> data = this.systemAccountInfoFeignClient.findById(id);
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
    public ResponseData save(SystemAccountInfoValidator systemAccountInfoValidator, String op) {

        try {
            ResponseData data = this.systemAccountInfoFeignClient.save(systemAccountInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销账户
     *
     * @param id
     * @return
     */
    public ResponseData logout(String id) {
        try {
            ResponseData data = this.systemAccountInfoFeignClient.logout(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
