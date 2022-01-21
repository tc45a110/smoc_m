package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;


/**
 * 账号通道配置管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface AccountChannelFeignClient {


    /**
     * 查询配置的业务账号通道
     * @param accountChannelInfoQo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/account/channel/findAccountChannelConfig", method = RequestMethod.POST)
    ResponseData<Map<String, AccountChannelInfoQo>> findAccountChannelConfig(@RequestBody AccountChannelInfoQo accountChannelInfoQo) throws Exception;

    /**
     *  检索通道列表
     * @param channelBasicInfoQo
     * @return
     */
    @RequestMapping(value = "/account/channel/findChannelList", method = RequestMethod.POST)
    ResponseData<List<ChannelBasicInfoQo>> findChannelList(@RequestBody ChannelBasicInfoQo channelBasicInfoQo) throws Exception;
}
