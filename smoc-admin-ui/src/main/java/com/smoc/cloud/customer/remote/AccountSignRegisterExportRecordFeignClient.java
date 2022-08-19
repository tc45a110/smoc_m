package com.smoc.cloud.customer.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterExportRecordValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface AccountSignRegisterExportRecordFeignClient {


    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/sign/register/file/record/page", method = RequestMethod.POST)
    ResponseData<PageList<AccountSignRegisterExportRecordValidator>> page(@RequestBody PageParams pageParams) throws Exception;


    @RequestMapping(value = "/sign/register/file/record/findByRegisterOrderNo/{registerOrderNo}", method = RequestMethod.GET)
    ResponseData<AccountSignRegisterExportRecordValidator>  findByRegisterOrderNo(@PathVariable String registerOrderNo) throws Exception;
}
