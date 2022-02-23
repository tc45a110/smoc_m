package com.smoc.cloud.filter.keywords.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 关键词管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface KeywordsFeignClient {

    /**
     * 查询
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/filter/keywords/system/page", method = RequestMethod.POST)
    PageList<FilterKeyWordsInfoValidator> page(@RequestBody PageParams<FilterKeyWordsInfoValidator> pageParams);

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/filter/keywords/system/findById/{id}", method = RequestMethod.GET)
    ResponseData<FilterKeyWordsInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @param filterKeyWordsInfoValidator
     * @return
     */
    @RequestMapping(value = "/filter/keywords/system/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/filter/keywords/system/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 批量保存
     * @param filterKeyWordsInfoValidator
     * @param op
     */
    @RequestMapping(value = "/filter/keywords/system/bathSave/{op}", method = RequestMethod.POST)
    void bathSave(@RequestBody FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, @PathVariable String op);

    /**
     * 关键字导入
     * @param validator
     */
    @RequestMapping(value = "/filter/keywords/system/expBatchSave", method = RequestMethod.POST)
    void expBatchSave(@RequestBody FilterKeyWordsInfoValidator validator);
}
