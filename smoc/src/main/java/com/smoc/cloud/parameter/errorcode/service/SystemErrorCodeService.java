package com.smoc.cloud.parameter.errorcode.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import com.smoc.cloud.parameter.errorcode.entity.SystemErrorCode;
import com.smoc.cloud.parameter.errorcode.repository.SystemErrorCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemErrorCodeService {

    @Resource
    private SystemErrorCodeRepository systemErrorCodeRepository;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public PageList<SystemErrorCodeValidator> page(PageParams<SystemErrorCodeValidator> pageParams) {
        return systemErrorCodeRepository.page(pageParams);
    }

    /**
     * 根据id 查询
     * @param id
     * @return
     */
    public ResponseData findById(String id) {

        Optional<SystemErrorCode> data = systemErrorCodeRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param systemErrorCodeValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(SystemErrorCodeValidator systemErrorCodeValidator, String op) {

        //转BaseUser存放对象
        SystemErrorCode entity = new SystemErrorCode();
        BeanUtils.copyProperties(systemErrorCodeValidator, entity);

        List<SystemErrorCode> data = systemErrorCodeRepository.findByCodeTypeAndErrorCode(systemErrorCodeValidator.getCodeType(),systemErrorCodeValidator.getErrorCode());

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError("错误码已存在，请修改");
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                SystemErrorCode organization = (SystemErrorCode) iter.next();
                if (!entity.getId().equals(organization.getId()) ) {
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
        log.info("[错误码管理][{}]数据:{}",op,JSON.toJSONString(entity));

        systemErrorCodeRepository.saveAndFlush(entity);
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

        SystemErrorCode data = systemErrorCodeRepository.findById(id).get();

        //记录日志
        log.info("[错误码管理][delete]数据:{}",JSON.toJSONString(data));
        systemErrorCodeRepository.deleteById(id);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 批量保存
     * @param systemErrorCodeValidator
     * @return
     */
    @Async
    public void bathSave(SystemErrorCodeValidator systemErrorCodeValidator) {
        systemErrorCodeRepository.bathSave(systemErrorCodeValidator);
    }

}
