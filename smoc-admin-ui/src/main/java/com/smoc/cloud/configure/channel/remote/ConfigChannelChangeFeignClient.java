package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 通道切换
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ConfigChannelChangeFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/channel/change/page", method = RequestMethod.POST)
    ResponseData<PageList<ConfigChannelChangeValidator>> page(@RequestBody PageParams<ConfigChannelChangeValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/channel/change/findById/{id}", method = RequestMethod.GET)
    ResponseData<ConfigChannelChangeValidator> findById(@PathVariable String id)  throws Exception;

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/configure/channel/change/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody ConfigChannelChangeValidator configChannelChangeValidator, @PathVariable String op) throws Exception;

    /**
     * 取消变更,所有的业务账号都恢复到正常状态
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/channel/change/cancel/{id}", method = RequestMethod.GET)
    ResponseData cancelChannelChange(@PathVariable String id)throws Exception;
}
