package com.smoc.cloud.configure.codenumber.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.validator.CodeNumberInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 码号管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface CodeNumberFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/code/number/page", method = RequestMethod.POST)
    PageList<CodeNumberInfoValidator> page(@RequestBody PageParams<CodeNumberInfoValidator> pageParams)  throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/configure/code/number/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody CodeNumberInfoValidator codeNumberInfoValidator, @PathVariable String op) throws Exception;


}
