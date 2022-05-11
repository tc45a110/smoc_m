package com.smoc.cloud.intelligence.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface IntellectTemplateInfoFeign {

    /**
     * 查询模版
     *
     * @return
     */
    @RequestMapping(value = "/intel/template/page", method = RequestMethod.GET)
    PageList<IntellectTemplateInfoValidator> page(@RequestBody PageParams<IntellectTemplateInfoValidator> pageParams) throws Exception;

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/intel/template/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody IntellectTemplateInfoValidator intellectTemplateInfoValidator) throws Exception;
}
