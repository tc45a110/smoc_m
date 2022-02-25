package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 附件管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface SystemAttachmentFeignClient {

    /**
     * 根据业务id查询附件
     * @param id
     * @return
     */
    @RequestMapping(value = "/attachment/findByMoudleId/{id}", method = RequestMethod.GET)
    ResponseData<List<SystemAttachmentValidator>> findByMoudleId(@PathVariable String id);

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/attachment/findById/{id}", method = RequestMethod.GET)
    ResponseData<SystemAttachmentValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 根据id删除
     */
    @RequestMapping(value = "/attachment/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;
}
