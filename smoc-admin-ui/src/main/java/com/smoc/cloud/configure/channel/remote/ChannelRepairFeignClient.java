package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 通道补发管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ChannelRepairFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/channel/repair/page", method = RequestMethod.POST)
    PageList<ConfigChannelRepairValidator> page(@RequestBody PageParams<ConfigChannelRepairValidator> pageParams)  throws Exception;

}
