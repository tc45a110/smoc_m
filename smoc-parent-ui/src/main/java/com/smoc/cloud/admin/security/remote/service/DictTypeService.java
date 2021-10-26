package com.smoc.cloud.admin.security.remote.service;

import com.smoc.cloud.admin.security.remote.client.DictTypeFeignClient;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.DictTypeValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典类型管理服务
 * 2019/4/23 16:28
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DictTypeService {

    @Autowired
    private DictTypeFeignClient dictTypeFeignClient;

    /**
     * 获取列表数据
     */
    public ResponseData<List<DictTypeValidator>> list() {
        try {
            ResponseData<List<DictTypeValidator>> data = this.dictTypeFeignClient.list();
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 获取列表数据
     */
    public List<Nodes> getDictTree() {
        try {
            List<Nodes> data = this.dictTypeFeignClient.getDictTree();
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * 获取列表数据
     */
    public List<Nodes> getDictTree(String projectName) {
        try {
            List<Nodes> data = this.dictTypeFeignClient.getDictTree(projectName);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * 根据id获取数据
     */
    public ResponseData<DictTypeValidator> findById(String id) {
        try {
            ResponseData<DictTypeValidator> data = this.dictTypeFeignClient.findById(id);
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
    public ResponseData save(DictTypeValidator dictTypeValidator, String op) {
        try {
            ResponseData data = this.dictTypeFeignClient.save(dictTypeValidator, op);
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
            ResponseData data = this.dictTypeFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
