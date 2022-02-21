package com.smoc.cloud.filter.white.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 白名单管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface WhiteFeignClient {

    /**
     * 根据群id查询
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/filter/white/page", method = RequestMethod.POST)
    PageList<FilterWhiteListValidator> page(@RequestBody PageParams<FilterWhiteListValidator> pageParams);

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/filter/white/findById/{id}", method = RequestMethod.GET)
    ResponseData<FilterWhiteListValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @param filterWhiteListValidator
     * @return
     */
    @RequestMapping(value = "/filter/white/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody FilterWhiteListValidator filterWhiteListValidator, @PathVariable String op) throws Exception;

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/filter/white/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 批量保存
     * @param filterWhiteListValidator
     */
    @RequestMapping(value = "/filter/white/bathSave", method = RequestMethod.POST)
    void bathSave(@RequestBody FilterWhiteListValidator filterWhiteListValidator);

    /**
     * 查询导出数据
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/filter/white/excelModel", method = RequestMethod.POST)
    ResponseData<List<ExcelModel>> excelModel(@RequestBody PageParams<FilterWhiteListValidator> pageParams);
}
