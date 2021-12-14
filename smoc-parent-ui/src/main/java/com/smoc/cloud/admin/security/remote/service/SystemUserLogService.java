package com.smoc.cloud.admin.security.remote.service;


import com.smoc.cloud.admin.security.remote.client.SystemUserLogFeignClient;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 *     if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
 *         systemUserLogService.logsAsync("CODE_NUMBER",codeNumberInfoValidator.getId(),"add".equals(op) ? codeNumberInfoValidator.getCreatedBy() : codeNumberInfoValidator.getUpdatedBy(),  op, "add".equals(op) ? "添加码号" : "修改码号", JSON.toJSONString(codeNumberInfoValidator));
 *     }
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemUserLogService {



    @Autowired
    private SystemUserLogFeignClient systemUserLogFeignClient;

    /**
     * 分页查询日志信息
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<SystemUserLogValidator>> page(PageParams<SystemUserLogValidator> pageParams) {

        try {
            ResponseData<PageList<SystemUserLogValidator>> data = systemUserLogFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }

    }

    /**
     * 异步保存用户操作日志
     *
     * @param module          操作模块标识
     * @param moduleId        操作模块数据ID
     * @param userId          用户ID
     * @param operationType   操作类型
     * @param simpleIntroduce 操作简介
     * @param data            操作数据（json）
     */
    public void logsAsync(String module, String moduleId,String userId, String operationType, String simpleIntroduce, String data) {

        SystemUserLogValidator systemUserLogValidator = new SystemUserLogValidator();
        systemUserLogValidator.setId(UUID.uuid32());
        systemUserLogValidator.setUserId(userId);
        systemUserLogValidator.setModule(module);
        systemUserLogValidator.setModuleId(moduleId);
        systemUserLogValidator.setOperationType(operationType);
        systemUserLogValidator.setSimpleIntroduce(simpleIntroduce);
        systemUserLogValidator.setLogData(data);
        systemUserLogValidator.setCreatedTime(DateTimeUtils.getNowDateTime());
        this.save(systemUserLogValidator);

    }

    /**
     * 异步保存日志信息
     */
    @Async
    public ResponseData save(SystemUserLogValidator systemUserLogValidator) {
        try {
            ResponseData data = this.systemUserLogFeignClient.save(systemUserLogValidator);
            log.info("[保存操作日志] 状态：{}", data.getMessage());
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


}
