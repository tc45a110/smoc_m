package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.entity.BaseRole;
import com.smoc.cloud.auth.data.provider.service.BaseRoleService;
import com.smoc.cloud.common.auth.validator.RoleValidator;
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

/**
 * 角色接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("roles")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class RolesController {

    @Autowired
    private BaseRoleService baseRoleService;

    /**
     * 查询角色列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list() {

        ResponseData data = baseRoleService.findAll();
        return data;
    }

    /**
     * 根据ID 查询角色
     *
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = baseRoleService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody RoleValidator roleValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(roleValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(roleValidator));
        }

        //数据复制
        BaseRole role = new BaseRole();
        BeanUtils.copyProperties(roleValidator, role);
        role.setRoleCode(role.getRoleCode().toUpperCase());

        //保存操作
        ResponseData data = baseRoleService.save(role, op);

        return data;
    }

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseData deleteById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return baseRoleService.deleteById(id);
    }

}
