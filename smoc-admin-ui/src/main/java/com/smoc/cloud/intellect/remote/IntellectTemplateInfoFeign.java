package com.smoc.cloud.intellect.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 根据templateId 修改 tplId
     * @param templateId
     * @param tplId
     * @param status
     * @return
     */
    @RequestMapping(value = "/intel/template/updateTplIdAndStatus/{templateId}/{tplId}/{status}", method = RequestMethod.GET)
     ResponseData updateTplIdAndStatus(@PathVariable String templateId, @PathVariable String tplId, @PathVariable Integer status) throws Exception;

    /**
     * templateId 修改 check status
     * @param status
     * @param templateId
     * @return
     */
    @RequestMapping(value = "/intel/template/updateCheckStatus/{templateId}/{status}", method = RequestMethod.GET)
     ResponseData updateCheckStatus(@PathVariable String templateId,@PathVariable Integer status) throws Exception;

    /**
     * tplId 修改 check status
     * @param status
     * @param tplId
     * @return
     */
    @RequestMapping(value = "/intel/template/updateCheckStatusByTplId/{tplId}/{status}", method = RequestMethod.GET)
    ResponseData updateCheckStatusByTplId(@PathVariable String tplId,@PathVariable Integer status) throws Exception;

    /**
     * templateId 修改  status
     * @param status
     * @param templateId
     * @return
     */
    @RequestMapping(value = "/intel/template/updateStatusByTemplateId/{templateId}/{status}", method = RequestMethod.GET)
     ResponseData updateStatusByTemplateId(@PathVariable String templateId,@PathVariable Integer status) throws Exception;
}
