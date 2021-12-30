package com.smoc.cloud.configure.channel.group.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



/**
 * 通道组管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ChannelGroupFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/channel/group/page", method = RequestMethod.POST)
    PageList<ChannelGroupInfoValidator> page(@RequestBody PageParams<ChannelGroupInfoValidator> pageParams)  throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/configure/channel/group/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody ChannelGroupInfoValidator channelGroupInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/channel/group/findById/{id}", method = RequestMethod.GET)
    ResponseData<ChannelGroupInfoValidator> findById(@PathVariable String id) throws Exception;

}
