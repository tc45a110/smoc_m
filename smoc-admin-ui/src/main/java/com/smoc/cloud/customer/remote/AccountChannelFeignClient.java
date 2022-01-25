package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 保存通道
     * @param accountChannelInfoValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/account/channel/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountChannelInfoValidator accountChannelInfoValidator, @PathVariable String op);

    /**
     * 查询账号下运营商是否配置过通道
     * @param accountId
     * @param carrier
     * @return
     */
    @RequestMapping(value = "/account/channel/findByAccountIdAndCarrier/{accountId}/{carrier}", method = RequestMethod.GET)
    ResponseData findByAccountIdAndCarrier(@PathVariable String accountId, @PathVariable String carrier);
}
