package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelRepairQo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 业务账号失败补发远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface AccountChannelRepairFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/channel/repair/accountChannelRepairPage", method = RequestMethod.POST)
    PageList<AccountChannelRepairQo> page(@RequestBody PageParams<AccountChannelRepairQo> pageParams)  throws Exception;


}
