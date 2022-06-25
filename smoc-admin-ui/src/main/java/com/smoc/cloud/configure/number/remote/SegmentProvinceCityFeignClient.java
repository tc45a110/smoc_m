package com.smoc.cloud.configure.number.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.SystemSegmentProvinceCityValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 省号码管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface SegmentProvinceCityFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/segment/page", method = RequestMethod.POST)
    PageList<SystemSegmentProvinceCityValidator> page(@RequestBody PageParams<SystemSegmentProvinceCityValidator> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/segment/findById/{id}", method = RequestMethod.GET)
    ResponseData<SystemSegmentProvinceCityValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/configure/segment/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/configure/segment/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id);

    /**
     * 批量导出
     * @param systemSegmentProvinceCityValidator
     * @return
     */
    @RequestMapping(value = "/configure/segment/batchSave", method = RequestMethod.POST)
    void batchSave(@RequestBody SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator);
}
