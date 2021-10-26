package com.smoc.cloud.admin.service;

import com.smoc.cloud.admin.remote.client.SystemFeignClient;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 系统管理服务
 * 2019/4/23 16:28
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemService {

    @Autowired
    private SystemFeignClient systemFeignClient;

    /**
     * 获取系统数据
     */
    public ResponseData<Iterable<SystemValidator>> list(){
        try {
            ResponseData<Iterable<SystemValidator>> data = this.systemFeignClient.list();
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取系统数据
     */
    public ResponseData<SystemValidator> findById(String id){
        try {
            ResponseData<SystemValidator> data = this.systemFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改系统数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(SystemValidator systemValidator, String op){
        try {
            ResponseData data = this.systemFeignClient.save(systemValidator,op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


    /**
     * 根据id删除系统数据
     */
    public ResponseData deleteById(String id){
        try {
            ResponseData data = this.systemFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
