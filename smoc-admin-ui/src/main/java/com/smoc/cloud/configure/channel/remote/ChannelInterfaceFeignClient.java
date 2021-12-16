package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelInterfaceValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 通道接口参数管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ChannelInterfaceFeignClient {

    /**
     * 根据通道id获取通道接口参数
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/channel/findChannelInterfaceByChannelId/{id}", method = RequestMethod.GET)
    ResponseData<ChannelInterfaceValidator> findChannelInterfaceByChannelId(@PathVariable String id) throws Exception;

    /**
     * 保存、修改通道接口参数
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/configure/channel/interfaceSave/{op}", method = RequestMethod.POST)
    ResponseData interfaceSave(@RequestBody ChannelInterfaceValidator channelInterfaceValidator, @PathVariable String op) throws Exception;


}
