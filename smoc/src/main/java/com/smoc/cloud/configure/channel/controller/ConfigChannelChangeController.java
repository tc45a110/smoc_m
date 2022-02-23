package com.smoc.cloud.configure.channel.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ConfigChannelChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通道切换
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel/change")
public class ConfigChannelChangeController {


    @Autowired
    private ConfigChannelChangeService configChannelChangeService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<ConfigChannelChangeValidator>> page(@RequestBody PageParams<ConfigChannelChangeValidator> pageParams) {

        return configChannelChangeService.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<ConfigChannelChangeValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = configChannelChangeService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ConfigChannelChangeValidator configChannelChangeValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(configChannelChangeValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(configChannelChangeValidator));
        }

        //保存操作
        ResponseData data = configChannelChangeService.save(configChannelChangeValidator, op);

        return data;
    }

    /**
     * 取消变更,所有的业务账号都恢复到正常状态
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.GET)
    public ResponseData cancelChannelChange(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = configChannelChangeService.cancelChannelChange(id);
        return data;
    }
}
