package com.smoc.cloud.admin.service;

import com.smoc.cloud.admin.remote.client.ClientsFeignClient;
import com.smoc.cloud.common.auth.validator.ClientDetailsValidator;
import com.smoc.cloud.common.auth.validator.ResetClientSecretValidator;
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
 * 客户端管理服务
 * 2019/4/23 16:28
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ClientsService {

    @Autowired
    private ClientsFeignClient clientsFeignClient;

    /**
     * 查询列表
     */
    public ResponseData<List<ClientDetailsValidator>> list() {

        try {
            ResponseData<List<ClientDetailsValidator>> data = this.clientsFeignClient.list();
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

    /**
     * 根据id获取信息
     */
    public ResponseData<ClientDetailsValidator> findById(String id) {

        try {
            ResponseData<ClientDetailsValidator> data = this.clientsFeignClient.findById(id);
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
    public ResponseData save(ClientDetailsValidator clientDetailsValidator, String op) {

        try {
            ResponseData data = this.clientsFeignClient.save(clientDetailsValidator, op);
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
            ResponseData data = this.clientsFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 修改客户端密码
     *
     * @param resetClientSecretValidator
     * @return
     */
    public ResponseData resetClientSecret(ResetClientSecretValidator resetClientSecretValidator) {

        try {
            ResponseData data = this.clientsFeignClient.resetClientSecret(resetClientSecretValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
