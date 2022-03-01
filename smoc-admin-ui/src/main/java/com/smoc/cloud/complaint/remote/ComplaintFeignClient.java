package com.smoc.cloud.complaint.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 投诉管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ComplaintFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/complaint/page", method = RequestMethod.POST)
    PageList<MessageComplaintInfoValidator> page(@RequestBody PageParams<MessageComplaintInfoValidator> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/complaint/findById/{id}", method = RequestMethod.GET)
    ResponseData<MessageComplaintInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/complaint/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody MessageComplaintInfoValidator messageComplaintInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/complaint/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 批量导入投诉
     * @param messageComplaintInfoValidator
     * @return
     */
    @RequestMapping(value = "/complaint/batchSave", method = RequestMethod.POST)
    ResponseData batchSave(@RequestBody MessageComplaintInfoValidator messageComplaintInfoValidator) throws Exception;
}
