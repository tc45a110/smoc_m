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

import java.util.Map;

/**
 * web任务单
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface MessageWebTaskInfoFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/message/web/task/page", method = RequestMethod.POST)
    ResponseData<PageList<MessageWebTaskInfoValidator>> page(@RequestBody PageParams<MessageWebTaskInfoValidator> pageParams) throws Exception;

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    @RequestMapping(value = "/message/web/task/count", method = RequestMethod.POST)
    ResponseData<Map<String, Object>> count(@RequestBody MessageWebTaskInfoValidator qo) throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/message/web/task/findById/{id}", method = RequestMethod.GET)
    ResponseData<MessageWebTaskInfoValidator> findById(@PathVariable String id) throws Exception;
}
