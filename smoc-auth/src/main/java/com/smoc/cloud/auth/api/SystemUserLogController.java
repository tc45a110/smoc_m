package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.entity.SystemUserLog;
import com.smoc.cloud.auth.data.provider.service.SystemUserLogService;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@RestController
@RequestMapping("user/logs")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SystemUserLogController {

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 根据条件查询分页查询
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<SystemUserLogValidator>> page(@RequestBody PageParams<SystemUserLogValidator> pageParams) {

        return systemUserLogService.page(pageParams);
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return systemUserLogService.findById(id);
    }

    /**
     * 保存
     *
     * @param systemUserLogValidator
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody SystemUserLogValidator systemUserLogValidator) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(systemUserLogValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(systemUserLogValidator));
        }

        //转SystemUserLog存放对象
        SystemUserLog systemUserLog = new SystemUserLog();
        BeanUtils.copyProperties(systemUserLogValidator, systemUserLog);
        return systemUserLogService.save(systemUserLog, "add");
    }


}
