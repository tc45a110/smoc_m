package com.smoc.cloud.intellect.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface IntellectShortUrlFeign {

    /**
     * 查询短链
     *
     * @return
     */
    @RequestMapping(value = "/intel/short/url/page", method = RequestMethod.POST)
    PageList<IntellectShortUrlValidator> page(@RequestBody PageParams<IntellectShortUrlValidator> pageParams) throws Exception;

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/intel/short/url/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody IntellectShortUrlValidator intellectShortUrlValidator) throws Exception;
}
