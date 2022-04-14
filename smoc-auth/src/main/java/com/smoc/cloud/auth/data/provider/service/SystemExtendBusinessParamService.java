package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.SystemExtendBusinessParam;
import com.smoc.cloud.auth.data.provider.repository.SystemExtendBusinessParamRepository;
import com.smoc.cloud.common.auth.validator.SystemExtendBusinessParamValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    public ResponseData<SystemExtendBusinessParamValidator> findById(String id) {

        Optional<SystemExtendBusinessParam> data = systemExtendBusinessParamRepository.findById(id);

        SystemExtendBusinessParamValidator systemExtendBusinessParamValidator = new SystemExtendBusinessParamValidator();
        BeanUtils.copyProperties(data.get(),systemExtendBusinessParamValidator);
        //日期转换
        systemExtendBusinessParamValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(data.get().getCreatedTime()));


        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(systemExtendBusinessParamValidator);
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @Transactional
    public ResponseData save(SystemExtendBusinessParamValidator systemExtendBusinessParamValidator, String op){

        /**
         * 查重操作
         */
        List<SystemExtendBusinessParam> data = systemExtendBusinessParamRepository.findSystemExtendBusinessParamByBusinessTypeAndParamKeyAndParamStatus(systemExtendBusinessParamValidator.getBusinessType(), systemExtendBusinessParamValidator.getParamKey(),"1");
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
                if (!systemExtendBusinessParamValidator.getId().equals(record.getId())) {
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

        //转SystemExtendBusinessParam存放对象
        SystemExtendBusinessParam entity = new SystemExtendBusinessParam();
        BeanUtils.copyProperties(systemExtendBusinessParamValidator, entity);
        //日期转换
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(systemExtendBusinessParamValidator.getCreatedTime()));

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
        systemExtendBusinessParamRepository.updateSystemExtendBusinessParamStatus(id,"0");
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

    /**
     * 根据业务类型和KEY查询
     * @param systemExtendBusinessParamValidator
     * @return
     */
    public ResponseData findParamByBusinessTypeAndParamKey(SystemExtendBusinessParamValidator systemExtendBusinessParamValidator) {

        List<SystemExtendBusinessParam> list = systemExtendBusinessParamRepository.findSystemExtendBusinessParamByBusinessTypeAndParamKeyAndParamStatus(systemExtendBusinessParamValidator.getBusinessType(),systemExtendBusinessParamValidator.getParamKey(),"1");
        if(!StringUtils.isEmpty(list) && list.size()>0){
            SystemExtendBusinessParam systemExtendBusinessParam = list.get(0);
            return ResponseDataUtil.buildSuccess(systemExtendBusinessParam);
        }

        return ResponseDataUtil.buildSuccess();
    }
}
