package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.SystemExtendBusinessParam;
import com.smoc.cloud.auth.data.provider.repository.SystemExtendBusinessParamRepository;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 系统共用业务扩展参数 管理
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemExtendBusinessParamService {

    @Resource
    private SystemExtendBusinessParamRepository systemExtendBusinessParamRepository;

    /**
     * 按业务类型查询系统扩展参数
     *
     * @param businessType
     * @param paramStatus
     * @return
     */
    public ResponseData findSystemExtendBusinessParamByBusinessTypeAndParamStatus(String businessType, String paramStatus) {

        List<SystemExtendBusinessParam> data = systemExtendBusinessParamRepository.findSystemExtendBusinessParamByBusinessTypeAndParamStatusOrderByDisplaySortAsc(businessType, paramStatus);
        return ResponseDataUtil.buildSuccess(data);

    }

    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData<SystemExtendBusinessParam> findById(String id) {

        Optional<SystemExtendBusinessParam> data = systemExtendBusinessParamRepository.findById(id);

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
    public ResponseData save(SystemExtendBusinessParam entity,String op){

        /**
         * 查重操作
         */
        List<SystemExtendBusinessParam> data = systemExtendBusinessParamRepository.findSystemExtendBusinessParamByBusinessTypeAndParamKey(entity.getBusinessType(), entity.getParamKey());
        //add查重  businessType、paramKey
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重 businessType、paramKey
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iterator = data.iterator();
            while (iterator.hasNext()) {
                SystemExtendBusinessParam record = (SystemExtendBusinessParam) iterator.next();
                if (!entity.getId().equals(record.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[业务扩展参数][{}]数据:{}",op, JSON.toJSONString(entity));

        //操作数据
        systemExtendBusinessParamRepository.saveAndFlush(entity);

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

        SystemExtendBusinessParam data = systemExtendBusinessParamRepository.findById(id).get();
        //记录日志
        log.info("[业务扩展参数][delete]数据:{}", JSON.toJSONString(data));
        systemExtendBusinessParamRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id判断对象是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(String id) {
        return systemExtendBusinessParamRepository.existsById(id);
    }


}
