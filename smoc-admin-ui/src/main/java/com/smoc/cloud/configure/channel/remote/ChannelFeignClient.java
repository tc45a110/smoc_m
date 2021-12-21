package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 通道管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ChannelFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/channel/page", method = RequestMethod.POST)
    PageList<ChannelBasicInfoQo> page(@RequestBody PageParams<ChannelBasicInfoQo> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/channel/findChannelById/{id}", method = RequestMethod.GET)
    ResponseData<ChannelBasicInfoValidator> findChannelById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/configure/channel/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody ChannelBasicInfoValidator channelBasicInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/channel/findById/{id}", method = RequestMethod.GET)
    ResponseData<ChannelBasicInfoValidator> findById(@PathVariable String id) throws Exception;
}
