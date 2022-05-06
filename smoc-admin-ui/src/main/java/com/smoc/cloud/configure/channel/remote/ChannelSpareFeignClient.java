package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelInterfaceValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelSpareChannelValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 补发通道管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ChannelSpareFeignClient {

    /**
     * 根据通道id获取通道接口参数
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/channel/spare/findByChannelId/{id}", method = RequestMethod.GET)
    ResponseData<ConfigChannelSpareChannelValidator> findByChannelId(@PathVariable String id) throws Exception;

    /**
     * 保存、修改通道接口参数
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/configure/channel/spare/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody ConfigChannelSpareChannelValidator configChannelSpareChannelValidator, @PathVariable String op) throws Exception;

    /**
     * 根据原通道属性查询符合要求的备用通道
     * @param channelBasicInfoValidator
     * @return
     */
    @RequestMapping(value = "/configure/channel/spare/findSpareChannel", method = RequestMethod.POST)
    ResponseData<List<ConfigChannelSpareChannelValidator>> findSpareChannel(@RequestBody ChannelBasicInfoValidator channelBasicInfoValidator);

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/configure/channel/spare/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    @RequestMapping(value = "/configure/channel/spare/findById/{id}", method = RequestMethod.GET)
    ResponseData<ConfigChannelSpareChannelValidator> findById(@PathVariable String id);
}
