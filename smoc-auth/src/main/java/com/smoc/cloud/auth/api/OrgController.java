package com.smoc.cloud.auth.api;


import com.smoc.cloud.auth.data.provider.entity.BaseOrganization;
import com.smoc.cloud.auth.data.provider.service.BaseOrganizationService;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.OrgValidator;
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

import java.util.List;

/**
 * 组织管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/org")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class OrgController {

    @Autowired
    private BaseOrganizationService baseOrganizationService;

    /**
     * 根据父ID 查询组织
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getOrgByParentId/{parentId}", method = RequestMethod.GET)
    public List<Nodes> getOrgByParentId(@PathVariable String parentId) {

        ResponseData<List<Nodes>> data = baseOrganizationService.getOrgByParentId(parentId);
        return data.getData();
    }

    /**
     * 根据父ID、组织类型查询
     *
     * @param parentId
     * @param orgType  组织类型 0标示组织  1标示部门
     * @return
     */
    @RequestMapping(value = "/findByParentIdAndOrgType/{parentId}/{orgType}", method = RequestMethod.GET)
    public ResponseData<List<BaseOrganization>> findByParentIdAndOrgType(@PathVariable String parentId, @PathVariable Integer orgType) {

        ResponseData<List<BaseOrganization>> data = baseOrganizationService.findByParentIdAndOrgType(parentId, orgType);
        return data;
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<BaseOrganization> findById(@PathVariable String id) {


        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return baseOrganizationService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param orgValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody OrgValidator orgValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(orgValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(orgValidator));
        }

        //转BaseUser存放对象
        BaseOrganization baseOrganization = new BaseOrganization();
        BeanUtils.copyProperties(orgValidator, baseOrganization);

        return baseOrganizationService.save(baseOrganization, op);
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

        return baseOrganizationService.deleteById(id);
    }


}
