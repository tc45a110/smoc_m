package com.smoc.cloud.message.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 短信群发远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface MessageFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/message/web/task/page", method = RequestMethod.POST)
    ResponseData<PageList<MessageWebTaskInfoValidator>> page(@RequestBody PageParams<MessageWebTaskInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/message/web/task/findById/{id}", method = RequestMethod.GET)
    ResponseData<MessageWebTaskInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/message/web/task/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody MessageWebTaskInfoValidator messageWebTaskInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/message/web/task/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 短信发送
     * @param id
     * @return
     */
    @RequestMapping(value = "/message/web/task/sendMessageById/{id}", method = RequestMethod.GET)
    ResponseData sendMessageById(@PathVariable String id) throws Exception;
}
