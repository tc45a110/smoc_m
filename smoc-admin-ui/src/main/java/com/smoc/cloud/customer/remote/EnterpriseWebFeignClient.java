package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.ServiceAuthInfo;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 企业WEB登录账号管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface EnterpriseWebFeignClient {


    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/enterprise/web/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseWebAccountInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/enterprise/web/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 查询列表
     * @param enterpriseWebAccountInfoValidator
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enterprise/web/page", method = RequestMethod.POST)
    ResponseData<List<EnterpriseWebAccountInfoValidator>> page(@RequestBody EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator) throws Exception;

    /**
     * 重置密码
     * @param enterpriseWebAccountInfoValidator
     * @return
     */
    @RequestMapping(value = "/enterprise/web/resetPassword", method = RequestMethod.POST)
    ResponseData resetPassword(@RequestBody EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator) throws Exception;

    /**
     * 注销、启用账号
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/enterprise/web/forbiddenWeb/{id}/{status}", method = RequestMethod.GET)
    ResponseData forbiddenWeb(@PathVariable String id, @PathVariable String status) throws Exception;

    /**
     *  查询所有web账号
     * @param params
     * @return
     */
    @RequestMapping(value = "/enterprise/web/webAll", method = RequestMethod.POST)
    ResponseData<PageList<EnterpriseWebAccountInfoValidator>> webAll(@RequestBody PageParams<EnterpriseWebAccountInfoValidator> params);

    /**
     * 查询自服务平台角色
     * @param id
     * @return
     */
    @RequestMapping(value = "/enterprise/web/webLoginAuth/{id}", method = RequestMethod.GET)
    ResponseData<List<ServiceAuthInfo>> webLoginAuth(@PathVariable String id);

    /**
     * WEB登录账号授权
     * @param serviceAuthInfo
     * @return
     */
    @RequestMapping(value = "/enterprise/web/webAuthSave", method = RequestMethod.POST)
    ResponseData webAuthSave(@RequestBody ServiceAuthInfo serviceAuthInfo);
}
