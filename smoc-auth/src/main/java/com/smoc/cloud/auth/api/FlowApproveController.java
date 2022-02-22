package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.entity.BaseFlowApprove;
import com.smoc.cloud.auth.data.provider.service.BaseFlowApproveService;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
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
 * 流程审核接口
 * 2019/5/21 17:16
 **/
@Slf4j
@RestController
@RequestMapping("flow")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class FlowApproveController {

    @Autowired
    private BaseFlowApproveService baseFlowApproveService;

    /**
     * 审核记录
     */
    @RequestMapping(value = "/checkRecord", method = RequestMethod.POST)
    public List<FlowApproveValidator> checkRecord(@RequestBody FlowApproveValidator flowApproveValidator) {
        return baseFlowApproveService.checkRecord(flowApproveValidator);
    }

    /**
     * 根据业务id查询审核记录
     *
     * @param approveId
     * @return
     */
    @RequestMapping(value = "/checkRecord/{approveId}", method = RequestMethod.GET)
    public List<FlowApproveValidator> checkRecord(@PathVariable String approveId) {
        return baseFlowApproveService.checkRecord(approveId);
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<BaseFlowApprove> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return baseFlowApproveService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param flowApproveValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FlowApproveValidator flowApproveValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(flowApproveValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(flowApproveValidator));
        }

        //转BaseFlowApprove存放对象
        BaseFlowApprove baseFlowApprove = new BaseFlowApprove();
        BeanUtils.copyProperties(flowApproveValidator, baseFlowApprove);

        return baseFlowApproveService.save(baseFlowApprove, op);
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

        return baseFlowApproveService.deleteById(id);
    }

}
