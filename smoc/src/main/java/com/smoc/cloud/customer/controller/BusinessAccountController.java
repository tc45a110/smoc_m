package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticModel;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.utils.RandomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 业务账号接口
 **/
@Slf4j
@RestController
@RequestMapping("account")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class BusinessAccountController {

    @Autowired
    private BusinessAccountService businessAccountService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<AccountBasicInfoValidator> page(@RequestBody PageParams<AccountBasicInfoValidator> pageParams) {

        return businessAccountService.page(pageParams);
    }

    /**
     * 根据id获取信息
     * @param id
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

        ResponseData data = businessAccountService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody AccountBasicInfoValidator accountBasicInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(accountBasicInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(accountBasicInfoValidator));
        }

        //保存操作
        ResponseData data = businessAccountService.save(accountBasicInfoValidator, op);

        return data;
    }

    /**
     * 注销、启用业务账号
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/forbiddenAccountById/{id}/{status}", method = RequestMethod.GET)
    public ResponseData forbiddenAccountById(@PathVariable String id, @PathVariable String status)  {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = businessAccountService.forbiddenAccountById(id,status);
        return data;
    }

    /**
     *  查询企业所有的业务账号
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/forbiddenAccountById/{enterpriseId}", method = RequestMethod.GET)
    public ResponseData<List<AccountBasicInfoValidator>> findBusinessAccountByEnterpriseId(@PathVariable String enterpriseId)  {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(enterpriseId);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<List<AccountBasicInfoValidator>> data = businessAccountService.findBusinessAccountByEnterpriseId(enterpriseId);
        return data;
    }

    /**
     * 根据业务类型查询企业所有的业务账号
     * @param enterpriseId
     * @param businessType
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findBusinessAccountByEnterpriseIdAndBusinessType/{enterpriseId}/{businessType}", method = RequestMethod.GET)
    public ResponseData<List<AccountBasicInfo>> findBusinessAccountByEnterpriseIdAndBusinessType(@PathVariable String enterpriseId,@PathVariable String businessType){
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(enterpriseId);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<List<AccountBasicInfo>> data = businessAccountService.findBusinessAccountByEnterpriseIdAndBusinessType(enterpriseId,businessType);
        return data;
    }

    /**
     * 生成业务账号
     * @param enterpriseFlag
     * @return
     */
    @RequestMapping(value = "/createAccountId/{enterpriseFlag}", method = RequestMethod.GET)
    public ResponseData<String> createAccountId(@PathVariable String enterpriseFlag){

        return businessAccountService.createAccountId(enterpriseFlag);

    }

    /**
     *  查询企业所有的业务账号
     * @param accountBasicInfoValidator
     * @return
     */
    @RequestMapping(value = "/findBusinessAccount", method = RequestMethod.POST)
    public ResponseData<List<AccountBasicInfoValidator>> findBusinessAccount(@RequestBody AccountBasicInfoValidator accountBasicInfoValidator)  {

        ResponseData<List<AccountBasicInfoValidator>> data = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
        return data;
    }

    /**
     *  查询企业下的账户和余额
     * @param messageAccountValidator
     * @return
     */
    @RequestMapping(value = "/messageAccountList", method = RequestMethod.POST)
    public ResponseData<List<MessageAccountValidator>> messageAccountList(@RequestBody MessageAccountValidator messageAccountValidator)  {

        ResponseData<List<MessageAccountValidator>> data = businessAccountService.messageAccountList(messageAccountValidator);
        return data;
    }

    /**
     *  查询自服务平台发送账号列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/messageAccountInfoList", method = RequestMethod.POST)
    public ResponseData<PageList<MessageAccountValidator>> messageAccountInfoList(@RequestBody PageParams<MessageAccountValidator> params) {

        ResponseData<PageList<MessageAccountValidator>> data = businessAccountService.messageAccountInfoList(params);
        return data;
    }

    /**
     * 账号按维度统计发送量
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/statisticAccountSendNumber", method = RequestMethod.POST)
    public ResponseData<List<AccountStatisticSendData>> statisticAccountSendNumber(@RequestBody AccountStatisticSendData statisticSendData) {

        ResponseData<List<AccountStatisticSendData>> data = businessAccountService.statisticAccountSendNumber(statisticSendData);
        return data;
    }

    /**
     *  EC业务账号投诉率统计
     * @param statisticComplaintData
     * @return
     */
    @RequestMapping(value = "/statisticComplaintMonth", method = RequestMethod.POST)
    public ResponseData<List<AccountStatisticComplaintData>> statisticComplaintMonth(@RequestBody AccountStatisticComplaintData statisticComplaintData) {

        ResponseData<List<AccountStatisticComplaintData>> data = businessAccountService.statisticComplaintMonth(statisticComplaintData);
        return data;
    }

    /**
     * 业务账号综合查询
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/accountAll", method = RequestMethod.POST)
    public ResponseData<PageList<AccountInfoQo>> accountAll(@RequestBody PageParams<AccountInfoQo> pageParams){

        return businessAccountService.accountAll(pageParams);
    }

    /**
     * 查询账户列表根据接口类型
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/accountByProtocol", method = RequestMethod.POST)
    public PageList<AccountBasicInfoValidator> accountByProtocol(@RequestBody PageParams<AccountBasicInfoValidator> pageParams) {

        return businessAccountService.accountByProtocol(pageParams);
    }

    /**
     * 查询业务账号发送量
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/queryAccountSendStatistics", method = RequestMethod.POST)
    public ResponseData<PageList<AccountSendStatisticModel>> queryAccountSendStatistics(@RequestBody PageParams<AccountSendStatisticModel> pageParams){

        return businessAccountService.queryAccountSendStatistics(pageParams);
    }

    /**
     *  查询所有账号
     * @param accountBasicInfoValidator
     * @return
     */
    @RequestMapping(value = "/accountList", method = RequestMethod.POST)
    public ResponseData<List<AccountBasicInfoValidator>> accountList(@RequestBody AccountBasicInfoValidator accountBasicInfoValidator)  {

        ResponseData<List<AccountBasicInfoValidator>> data = businessAccountService.accountList(accountBasicInfoValidator);
        return data;
    }
}
