package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelAccountInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelInterfaceInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


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

    /**
     * 查看通道发送量统计
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/configure/channel/statisticChannelSendNumber", method = RequestMethod.POST)
    ResponseData<List<AccountStatisticSendData>> statisticChannelSendNumber(@RequestBody AccountStatisticSendData statisticSendData);

    /**
     * 通道投诉率统计
     * @param statisticComplaintData
     * @return
     */
    @RequestMapping(value = "/configure/channel/statisticComplaintMonth", method = RequestMethod.POST)
    ResponseData<List<AccountStatisticComplaintData>> statisticComplaintMonth(@RequestBody AccountStatisticComplaintData statisticComplaintData);

    /**
     * 通道账号使用明细
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/channel/channelAccountList", method = RequestMethod.POST)
    ResponseData<PageList<ChannelAccountInfoQo>> channelAccountList(@RequestBody PageParams<ChannelAccountInfoQo> pageParams);

    /**
     * 通道接口参数查询
     * @param params
     * @return
     */
    @RequestMapping(value = "/configure/channel/channelInterfacePage", method = RequestMethod.POST)
    ResponseData<PageList<ChannelInterfaceInfoQo>> channelInterfacePage(@RequestBody PageParams<ChannelInterfaceInfoQo> params);

    /**
     * 查询所有通道
     * @return
     */
    @RequestMapping(value = "/configure/channel/queryChannelAll", method = RequestMethod.POST)
    ResponseData<List<ChannelBasicInfoQo>> queryChannelAll(@RequestBody ChannelBasicInfoValidator channelBasicInfoValidator);
}
