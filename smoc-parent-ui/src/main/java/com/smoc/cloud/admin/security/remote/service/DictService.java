package com.smoc.cloud.admin.security.remote.service;

import com.smoc.cloud.admin.security.remote.client.DictFeignClient;
import com.smoc.cloud.common.auth.validator.DictValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典管理服务
 * 2019/4/23 16:28
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DictService {

    @Autowired
    private DictFeignClient dictFeignClient;

    /**
     * 获取列表数据
     */
    public ResponseData<List<DictValidator>> listByDictType(String typeId,String dictType) {
        try {
            ResponseData<List<DictValidator>> data = this.dictFeignClient.listByDictType(typeId,dictType);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取数据
     */
    public ResponseData<DictValidator> findById(String id) {
        try {
            ResponseData<DictValidator> data = this.dictFeignClient.findById(id);
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
    public ResponseData save(DictValidator dictValidator, String op) {
        try {
            ResponseData data = this.dictFeignClient.save(dictValidator, op);
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
            ResponseData data = this.dictFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
