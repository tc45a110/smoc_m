package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;


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

    /**
     * 根据运营商、业务类型、信息分类查询符合要求的备用通道
     * @param channelBasicInfoValidator
     * @return
     */
    @RequestMapping(value = "/configure/channel/repair/findSpareChannel", method = RequestMethod.POST)
    ResponseData<List<ConfigChannelRepairValidator>> findSpareChannel(@RequestBody ChannelBasicInfoValidator channelBasicInfoValidator);

    @RequestMapping(value = "/configure/channel/repair/editRepairRule/{channelId}", method = RequestMethod.GET)
    ResponseData<Map<String, ConfigChannelRepairRuleValidator>> editRepairRule(@PathVariable String channelId);

    /**
     * 保存补发通道
     * @param configChannelRepairValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/configure/channel/repair/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody ConfigChannelRepairValidator configChannelRepairValidator, @PathVariable String op);
}
