package com.smoc.cloud.admin.security.remote.client;

import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 流程审核远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth", path = "/auth")
public interface FlowApproveFeignClient {


    /**
     * 根据id获取信息
     */
    @RequestMapping(value = "/flow/findById/{id}", method = RequestMethod.GET)
    ResponseData<FlowApproveValidator> findById(@PathVariable(value = "id") String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/flow/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody FlowApproveValidator flowApproveValidator, @PathVariable(value = "op") String op) throws Exception;

    /**
     * 根据id删除数据
     */
    @RequestMapping(value = "/flow/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable(value = "id") String id) throws Exception;

    /**
     * 审核记录
     * @param flowApproveValidator
     * @return
     */
    @RequestMapping(value = "/flow/checkRecord", method = RequestMethod.POST)
    List<FlowApproveValidator> checkRecord(@RequestBody FlowApproveValidator flowApproveValidator);
}
