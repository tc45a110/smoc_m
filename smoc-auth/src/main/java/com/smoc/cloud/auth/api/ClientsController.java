package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.entity.OauthClientDetails;
import com.smoc.cloud.auth.data.provider.service.OauthClientDetailsService;
import com.smoc.cloud.common.auth.validator.ClientDetailsValidator;
import com.smoc.cloud.common.auth.validator.ResetClientSecretValidator;
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
 * 客户端接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("client")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ClientsController {

    @Autowired
    private OauthClientDetailsService oauthClientDetailsService;

    /**
     * 查询列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData<Iterable<OauthClientDetails>> list() {

        ResponseData data = oauthClientDetailsService.findAll();
        return data;
    }

    /**
     * 根据ID 查询
     *
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<OauthClientDetails> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = oauthClientDetailsService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ClientDetailsValidator clientDetailsValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(clientDetailsValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(clientDetailsValidator));
        }

        //数据复制
        OauthClientDetails clientDetails = new OauthClientDetails();
        BeanUtils.copyProperties(clientDetailsValidator, clientDetails);

        //保存操作
        ResponseData data = oauthClientDetailsService.save(clientDetails, op);

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

        return oauthClientDetailsService.deleteById(id);
    }

    /**
     * 修改客户端密码
     *
     * @param resetClientSecretValidator
     * @return
     */
    @RequestMapping(value = "/resetClientSecret", method = RequestMethod.POST)
    public ResponseData resetClientSecret(@RequestBody ResetClientSecretValidator resetClientSecretValidator) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(resetClientSecretValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(resetClientSecretValidator));
        }

        return oauthClientDetailsService.resetClientSecret(resetClientSecretValidator.getClientId(), resetClientSecretValidator.getClientSecret());

    }

}
