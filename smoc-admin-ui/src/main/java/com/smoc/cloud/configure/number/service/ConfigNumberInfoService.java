package com.smoc.cloud.configure.number.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import com.smoc.cloud.configure.number.remote.ConfigNumberInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * 号段管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigNumberInfoService {

    @Autowired
    private ConfigNumberInfoFeignClient configNumberInfoFeignClient;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ConfigNumberInfoValidator>> page(PageParams<ConfigNumberInfoValidator> pageParams) {
        try {
            PageList<ConfigNumberInfoValidator> pageList = this.configNumberInfoFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
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
    public ResponseData<ConfigNumberInfoValidator> findById(String id) {
        try {
            ResponseData<ConfigNumberInfoValidator> data = this.configNumberInfoFeignClient.findById(id);
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
    public ResponseData save(ConfigNumberInfoValidator configNumberInfoValidator, String op) {

        try {
            ResponseData data = this.configNumberInfoFeignClient.save(configNumberInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 删除数据
     * @param id
     * @return
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.configNumberInfoFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 批量导出
     * @param configNumberInfoValidator
     * @return
     */
    public ResponseData batchSave(ConfigNumberInfoValidator configNumberInfoValidator) {
        try {
            this.configNumberInfoFeignClient.batchSave(configNumberInfoValidator);
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询携号转网数据是否在redis库
     * @param numberCode
     * @return
     */
    public ResponseData<ConfigNumberInfoValidator> findRedis(String numberCode) {
        try {
            ResponseData<ConfigNumberInfoValidator> data = this.configNumberInfoFeignClient.findRedis(numberCode);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    public ResponseData deleteRedis(String numberCode) {
        try {
            ResponseData data = this.configNumberInfoFeignClient.deleteRedis(numberCode);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
