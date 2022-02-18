package com.smoc.cloud.filter.black.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 黑名单管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface BlackFeignClient {

    /**
     * 根据群id查询
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/filter/black/page", method = RequestMethod.POST)
    PageList<FilterBlackListValidator> page(@RequestBody PageParams<FilterBlackListValidator> pageParams);

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/filter/black/findById/{id}", method = RequestMethod.GET)
    ResponseData<FilterBlackListValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @param filterBlackListValidator
     * @return
     */
    @RequestMapping(value = "/filter/black/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody FilterBlackListValidator filterBlackListValidator, @PathVariable String op) throws Exception;

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/filter/black/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 批量保存
     * @param meipFileData
     * @param op
     * @return
     */
   /* @RequestMapping(value = "/filter/white/bathSave/{op}", method = RequestMethod.POST)
    void bathSave(@RequestBody MeipFileData meipFileData, @PathVariable String op);*/


}
