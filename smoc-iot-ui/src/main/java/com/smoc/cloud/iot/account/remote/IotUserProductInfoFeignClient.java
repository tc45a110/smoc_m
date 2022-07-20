package com.smoc.cloud.iot.account.remote;


import com.smoc.cloud.common.iot.validator.AccountProduct;
import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.iot.validator.IotUserProductInfoValidator;
import com.smoc.cloud.common.iot.validator.IotUserProductItemsValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(name = "iot", path = "/iot")
public interface IotUserProductInfoFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/iot/user/product/page", method = RequestMethod.POST)
    ResponseData<PageList<IotUserProductInfoValidator>> page(@RequestBody PageParams<IotUserProductInfoValidator> pageParams) throws Exception;


    /**
     * 查询列表
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "/iot/user/product/list/{account}", method = RequestMethod.GET)
    ResponseData<List<IotProductInfoValidator>> list(@PathVariable String account) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/user/product/findById/{id}", method = RequestMethod.GET)
    ResponseData<IotUserProductInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/iot/user/product/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountProduct accountProduct) throws Exception;

}
