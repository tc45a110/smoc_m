package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.SystemUserLog;
import com.smoc.cloud.auth.data.provider.repository.SystemUserLogRepository;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemUserLogService {

    @Resource
    private SystemUserLogRepository systemUserLogRepository;


    /**
     * 分页查询
     *
     * @param pageParams 分页参数
     * @return
     */
    public ResponseData<PageList<SystemUserLogValidator>> page(PageParams<SystemUserLogValidator> pageParams) {

        PageList data = systemUserLogRepository.page(pageParams);

        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData<SystemUserLog> findById(String id) {

        Optional<SystemUserLog> data = systemUserLogRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @Transactional
    public ResponseData save(SystemUserLog entity, String op) {

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //操作数据
        systemUserLogRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id 删除
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData deleteById(String id) {

        SystemUserLog data = systemUserLogRepository.findById(id).get();
        //记录日志
        log.info("[日志管理][delete]数据:{}", JSON.toJSONString(data));
        systemUserLogRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id判断对象是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(String id) {
        return systemUserLogRepository.existsById(id);
    }

}
