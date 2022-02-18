package com.smoc.cloud.filter.controller;


import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.entity.FilterGroupList;
import com.smoc.cloud.filter.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 群组管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/filter/group")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class GroupController {

    @Autowired
    private GroupService meipGroupService;

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String id) {

        return meipGroupService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param groupValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FilterGroupListValidator groupValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(groupValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(groupValidator));
        }

        return meipGroupService.save(groupValidator, op);
    }


    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseData deleteById(@PathVariable String id) {
        return meipGroupService.deleteById(id);
    }


    /**
     * 根据父ID查询
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/findByParentId/{enterprise}/{parentId}", method = RequestMethod.GET)
    public ResponseData<List<FilterGroupList>> findByParentId(@PathVariable String enterprise, @PathVariable String parentId) {

        ResponseData<List<FilterGroupList>> data = meipGroupService.findByParentId(enterprise,parentId);
        return data;
    }

    /**
     * 根据父ID查询tree
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getGroupByParentId/{enterprise}/{parentId}", method = RequestMethod.GET)
    public ResponseData<List<Nodes>> getGroupByParentId(@PathVariable String enterprise, @PathVariable String parentId) {

        ResponseData<List<Nodes>> data = meipGroupService.getGroupByParentId(enterprise,parentId);
        return data;
    }


}
