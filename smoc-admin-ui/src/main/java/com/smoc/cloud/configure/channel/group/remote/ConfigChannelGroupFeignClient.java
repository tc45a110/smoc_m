package com.smoc.cloud.configure.channel.group.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupConfigValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 配置通道组管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ConfigChannelGroupFeignClient {

    /**
     * 查询通道列表
     * @param channelBasicInfoQo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/configure/channel/group/findChannelList", method = RequestMethod.POST)
    ResponseData<List<ChannelBasicInfoQo>> findChannelList(@RequestBody ChannelBasicInfoQo channelBasicInfoQo) throws Exception;

    /**
     * 保存通道组配置
     * @param channelGroupConfigValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/configure/channel/group/saveChannelGroupConfig/{op}", method = RequestMethod.POST)
    ResponseData saveChannelGroupConfig(@RequestBody ChannelGroupConfigValidator channelGroupConfigValidator, @PathVariable String op) throws Exception;

    /**
     * 查询已配置的通道
     * @param configChannelGroupQo
     * @return
     */
    @RequestMapping(value = "/configure/channel/group/findConfigChannelGroupList", method = RequestMethod.POST)
    ResponseData<List<ConfigChannelGroupQo>> findConfigChannelGroupList(@RequestBody ConfigChannelGroupQo configChannelGroupQo) throws Exception;

    /**
     * 移除已配置通道
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/channel/group/deleteConfigChannelById/{id}", method = RequestMethod.GET)
    ResponseData deleteConfigChannelById(@PathVariable String id) throws Exception;

    /**
     *  根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/channel/group/findConfigChannelById/{id}", method = RequestMethod.GET)
    ResponseData<ConfigChannelGroupQo> findConfigChannelById(@PathVariable String id) throws Exception;
}
