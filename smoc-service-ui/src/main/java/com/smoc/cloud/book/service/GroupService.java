package com.smoc.cloud.book.service;

import com.smoc.cloud.book.remote.GroupFeignClient;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 群组管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GroupService {

    @Autowired
    private GroupFeignClient groupFeignClient;

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    public ResponseData<FilterGroupListValidator> findById(String id) {
        try {
            ResponseData data = this.groupFeignClient.findById(id);
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
    public ResponseData save(FilterGroupListValidator filterGroupListValidator, String op) {
        try {
            ResponseData data = this.groupFeignClient.save(filterGroupListValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除菜单数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.groupFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据父id查询群组
     * @param parentId
     * @return
     */
    public ResponseData<List<FilterGroupListValidator>> findByParentId(String enterprise,String parentId) {
        try {
            ResponseData<List<FilterGroupListValidator>> data = this.groupFeignClient.findByParentId(enterprise,parentId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     *根据父ID查询tree
     * @param parentId
     * @return
     */
    public ResponseData<List<Nodes>> getGroupByParentId(String enterprise, String parentId) {
        try {
            ResponseData<List<Nodes>> data = this.groupFeignClient.getGroupByParentId(enterprise,parentId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
