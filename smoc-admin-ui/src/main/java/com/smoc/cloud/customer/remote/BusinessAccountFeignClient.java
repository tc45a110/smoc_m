package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 业务账号管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface BusinessAccountFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/account/page", method = RequestMethod.POST)
    PageList<AccountBasicInfoValidator> page(@RequestBody PageParams<AccountBasicInfoValidator> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/findById/{id}", method = RequestMethod.GET)
    ResponseData<AccountBasicInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/account/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountBasicInfoValidator accountBasicInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 注销、启用账号
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/account/forbiddenAccountById/{id}/{status}", method = RequestMethod.GET)
    ResponseData forbiddenAccountById(@PathVariable String id, @PathVariable String status) throws Exception;

    /**
     * 查询企业所有的业务账号
     * @param enterpriseId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/account/forbiddenAccountById/{enterpriseId}", method = RequestMethod.GET)
    ResponseData<List<AccountBasicInfoValidator>> findBusinessAccountByEnterpriseId(@PathVariable String enterpriseId) throws Exception;

    /**
     * 生成业务账号
     * @param enterpriseFlag
     * @return
     */
    @RequestMapping(value = "/account/createAccountId/{enterpriseFlag}", method = RequestMethod.GET)
    ResponseData<String> createAccountId(@PathVariable String enterpriseFlag)throws Exception;

    /**
     * 账号按维度统计发送量
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/account/statisticAccountSendNumber", method = RequestMethod.POST)
    ResponseData<List<AccountStatisticSendData>> statisticAccountSendNumber(@RequestBody AccountStatisticSendData statisticSendData);

    /**
     * EC业务账号投诉率统计
     * @param statisticComplaintData
     * @return
     */
    @RequestMapping(value = "/account/statisticComplaintMonth", method = RequestMethod.POST)
    ResponseData<List<AccountStatisticComplaintData>> statisticComplaintMonth(@RequestBody AccountStatisticComplaintData statisticComplaintData);

    /**
     * 业务账号综合查询
     * @param params
     * @return
     */
    @RequestMapping(value = "/account/accountAll", method = RequestMethod.POST)
    ResponseData<PageList<AccountInfoQo>> accountAll(@RequestBody PageParams<AccountInfoQo> params);
}
