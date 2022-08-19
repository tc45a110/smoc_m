package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.CarrierCount;
import com.smoc.cloud.common.smoc.customer.qo.ExportModel;
import com.smoc.cloud.common.smoc.customer.qo.ExportRegisterModel;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "smoc", path = "/smoc")
public interface AccountSignRegisterForFileFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/sign/register/file/page", method = RequestMethod.POST)
    ResponseData<PageList<AccountSignRegisterForFileValidator>> page(@RequestBody PageParams<AccountSignRegisterForFileValidator> pageParams) throws Exception;

    /**
     * 根据运营商，统计未报备得条数
     * @return
     */
    @RequestMapping(value = "/sign/register/file/countByCarrier", method = RequestMethod.GET)
    ResponseData<List<CarrierCount>> countByCarrier() throws Exception;

    /**
     * 查询导出数据
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/sign/register/file/export", method = RequestMethod.POST)
    ResponseData<PageList<ExportModel>> export(@RequestBody PageParams<ExportModel> pageParams) throws Exception;

    /**
     * 根据报备订单号查询导出数据
     *
     * @param pageParams
     * @param registerOrderNo
     * @return
     */
    @RequestMapping(value = "/sign/register/file/query/{registerOrderNo}", method = RequestMethod.POST)
    ResponseData<PageList<ExportModel>> query(@RequestBody PageParams pageParams, @PathVariable String registerOrderNo) throws Exception;

    /**
     * 为导出数据生成报备订单号，并改变报备数据状态
     *
     * @param exportRegisterModel
     * @return
     */
    @RequestMapping(value = "/sign/register/file/register", method = RequestMethod.POST)
    ResponseData register(@RequestBody ExportRegisterModel exportRegisterModel) throws Exception;

    /**
     * 为导出数据生成报备订单号，并改变报备数据状态
     *
     * @param registerOrderNo
     * @param status
     * @return
     */
    @RequestMapping(value = "/sign/register/file/updateRegisterStatusByOrderNo/{status}/{registerOrderNo}", method = RequestMethod.GET)
    public ResponseData updateRegisterStatusByOrderNo(@PathVariable String status, @PathVariable  String registerOrderNo)  throws Exception;
}
