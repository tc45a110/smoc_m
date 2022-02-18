package com.smoc.cloud.filter.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.service.WhiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 白名单管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/filter/white")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class WhiteController {

    @Autowired
    private WhiteService whiteService;

    /**
     * 根据群id查询通讯录
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<FilterWhiteListValidator> page(@RequestBody PageParams<FilterWhiteListValidator> pageParams) {

        return whiteService.page(pageParams);
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

        return whiteService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param filterWhiteListValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FilterWhiteListValidator filterWhiteListValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(filterWhiteListValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(filterWhiteListValidator));
        }

        return whiteService.save(filterWhiteListValidator, op);
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

        return whiteService.deleteById(id);
    }

    /**
     * 批量保存
     * @param meipFileData
     * @param op
     * @return
     */
    /*@RequestMapping(value = "/bathSave/{op}", method = RequestMethod.POST)
    public void bathSave(@RequestBody MeipFileData meipFileData, @PathVariable String op) {
        whiteService.bathSave(meipFileData, op);
    }
*/
}
