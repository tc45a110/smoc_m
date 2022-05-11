package com.smoc.cloud.intelligence.controller;


import com.google.gson.Gson;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.intelligence.remote.request.RequestQueryTemplates;
import com.smoc.cloud.intelligence.remote.response.ResponseDataUtil;
import com.smoc.cloud.intelligence.remote.response.ResponseTemplateInfo;
import com.smoc.cloud.intelligence.remote.service.RequestService;
import com.smoc.cloud.intelligence.service.IntellectTemplateInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智能短信模版
 */
@Slf4j
@RestController
@RequestMapping("intel/template")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntellectTemplateController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private IntellectTemplateInfoService intellectTemplateInfoService;

    /**
     * 智能短信模版列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("intelligence/template/template_list");

        //初始化数据
        PageParams<IntellectTemplateInfoValidator> params = new PageParams<IntellectTemplateInfoValidator>();

        //总页数
        int pages = 0;
        //查询总行数
        int rows = 0;

        //开始行
        int startRow = 0;

        //结束行
        int endRow = 0;

        int currentPage = 1;

        int pageSize = 10;


        IntellectTemplateInfoValidator intellectTemplateInfoValidator = new IntellectTemplateInfoValidator();
        params.setParams(intellectTemplateInfoValidator);

//        //查询
//        ResponseData<PageList<IntellectTemplateInfoValidator>> data = intellectTemplateInfoService.page(params);
//        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
//            view.addObject("error", data.getCode() + ":" + data.getMessage());
//            return view;
//        }

        RequestQueryTemplates queryTemplates = new RequestQueryTemplates();
        queryTemplates.setPage(currentPage);
        queryTemplates.setSize(pageSize);
        //queryTemplates.setTplId("600057337");
        //queryTemplates.setBizId("699687d778924514b3f3636b193b5b3b");
        //queryTemplates.setBizFlag("XYK112");
        ResponseDataUtil<List<ResponseTemplateInfo>>  templates = requestService.queryIframeTemplates(queryTemplates);
        log.info("[iframeTemplates]:{}", new Gson().toJson(templates));

        rows = templates.getPageInfo().getTotal();

        //判断页数,如果是页大小的整数倍就为rows/pageRow如果不是整数倍就为rows/pageRow+1
        if (rows % pageSize == 0) {
            pages = rows / pageSize;
        } else {
            pages = rows / pageSize + 1;
        }

        if (currentPage > pages) {
            currentPage = pages;
        }

        //查询第page页的数据
        if (currentPage <= 1) {
            endRow = pageSize < rows ? pageSize : rows;
        } else {
            startRow = ((currentPage - 1) * pageSize);
            endRow = (((currentPage - 1) * pageSize) + pageSize) < rows ? (((currentPage - 1) * pageSize) + pageSize) : rows;
        }

        //设置总页数
        params.setPages(pages);
        //设置当前页
        params.setCurrentPage(currentPage);
        //设置每页显示条数
        params.setPageSize(pageSize);
        //设置总记录数
        params.setTotalRows(rows);
        //设置开始行
        params.setStartRow(startRow + 1);
        //设置结束行
        params.setEndRow(endRow);

        view.addObject("intellectTemplateInfoValidator", intellectTemplateInfoValidator);
        view.addObject("list", templates.getData());
        view.addObject("pageParams", params);
        return view;

    }

    /**
     * 智能短信模版列表
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute IntellectTemplateInfoValidator intellectTemplateInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("intelligence/template/template_list");
//        pageParams.setParams(intellectTemplateInfoValidator);
//
//        //查询
//        ResponseData<PageList<IntellectTemplateInfoValidator>> data = intellectTemplateInfoService.page(pageParams);
//        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
//            view.addObject("error", data.getCode() + ":" + data.getMessage());
//            return view;
//        }

        RequestQueryTemplates queryTemplates = new RequestQueryTemplates();
        queryTemplates.setPage(pageParams.getCurrentPage());
        queryTemplates.setSize(pageParams.getPageSize());
        //queryTemplates.setTplId("600057337");
        //queryTemplates.setBizId("699687d778924514b3f3636b193b5b3b");
        //queryTemplates.setBizFlag("XYK112");
        ResponseDataUtil<List<ResponseTemplateInfo>>  templates = requestService.queryIframeTemplates(queryTemplates);
        pageParams.setTotalRows(templates.getPageInfo().getTotal());


        view.addObject("intellectTemplateInfoValidator", intellectTemplateInfoValidator);
        view.addObject("list", templates.getData());
        view.addObject("pageParams", pageParams);
        return view;

    }



    /**
     * 远程模版查询列表
     *
     * @return
     */
    @RequestMapping(value = "/remote/list", method = RequestMethod.GET)
    public ModelAndView remoteList() {
        ModelAndView view = new ModelAndView("intelligence/template/template_remote_list");

        //初始化数据
        PageParams<IntellectTemplateInfoValidator> params = new PageParams<IntellectTemplateInfoValidator>();
        params.setPageSize(20);
        params.setCurrentPage(1);

        RequestQueryTemplates queryTemplates = new RequestQueryTemplates();
        queryTemplates.setPage(1);
        queryTemplates.setSize(20);
        //queryTemplates.setTplId("600057337");
        //queryTemplates.setBizId("699687d778924514b3f3636b193b5b3b");
        //queryTemplates.setBizFlag("XYK112");
        ResponseDataUtil<List<ResponseTemplateInfo>>  templates = requestService.queryTemplates(queryTemplates);
        params.setTotalRows(templates.getPageInfo().getTotal());
        log.info("[templates]:{}", new Gson().toJson(templates));

        Map<String,String> account = requestService.buildHeader();
        String url ="&Ab="+account.get("account")+"&Sm="+account.get("pwd")+"&Tt="+account.get("timestamp");

        view.addObject("url", url);
        view.addObject("list", templates.getData());
        view.addObject("pageParams", params);
        return view;

    }

    /**
     * 远程模版分页列表
     *
     * @return
     */
    @RequestMapping(value = "/remote/page", method = RequestMethod.POST)
    public ModelAndView remotePage(@ModelAttribute PageParams pageParams) {
        ModelAndView view = new ModelAndView("intelligence/template/template_remote_list");


        RequestQueryTemplates queryTemplates = new RequestQueryTemplates();
        queryTemplates.setPage(pageParams.getPages());
        queryTemplates.setSize(pageParams.getPageSize());
        //queryTemplates.setTplId("600057337");
        //queryTemplates.setBizId("699687d778924514b3f3636b193b5b3b");
        //queryTemplates.setBizFlag("XYK112");
        ResponseDataUtil<List<ResponseTemplateInfo>>  templates = requestService.queryTemplates(queryTemplates);
        pageParams.setTotalRows(templates.getPageInfo().getTotal());
        //log.info("[templates]:{}", new Gson().toJson(templates));

        Map<String,String> account = requestService.buildHeader();
        String url ="&Ab="+account.get("account")+"&Sm="+account.get("pwd")+"&Tt="+account.get("timestamp");

        view.addObject("url", url);
        view.addObject("list", templates.getData());
        view.addObject("pageParams", pageParams);
        return view;
    }

    /**
     * 远程模版查询列表
     *
     * @return
     */
    @RequestMapping(value = "/view/json/{templateId}", method = RequestMethod.GET)
    public ModelAndView view_json(@PathVariable String templateId) {
        ModelAndView view = new ModelAndView("intelligence/template/template_json_view");


        //queryTemplates.setBizId("699687d778924514b3f3636b193b5b3b");
        //queryTemplates.setBizFlag("XYK112");
        String templates = requestService.queryIframeTemplatesByTemplateId(new Long(templateId));

        view.addObject("jsonData",templates.replace("\\","").replace("\"[","[").replace("]\"","]"));
        return view;

    }

    /**
     * 远程模版查询列表
     *
     * @return
     */
    @RequestMapping(value = "/view/remote/json/{templateId}", method = RequestMethod.GET)
    public ModelAndView json(@PathVariable String templateId) {
        ModelAndView view = new ModelAndView("intelligence/template/template_remote_json_view");


        //queryTemplates.setBizId("699687d778924514b3f3636b193b5b3b");
        //queryTemplates.setBizFlag("XYK112");
        String templates = requestService.queryIframeTemplatesByTemplateId(new Long(templateId));

        view.addObject("jsonData",templates.replace("\\","").replace("\"[","[").replace("]\"","]"));
        return view;

    }

    /**
     * 添加模版
     *
     * @return
     */
    @RequestMapping(value = "/template_main/{accountId}/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView template_main(@PathVariable String accountId, @PathVariable String enterpriseId) {

        ModelAndView view = new ModelAndView("intelligence/template/template_main");
        ResponseDataUtil<String> responseDataUtil = requestService.getCompanyToken();
        log.info("[token]:{}", new Gson().toJson(responseDataUtil));
        view.addObject("accountId", accountId);
        view.addObject("enterpriseId", enterpriseId);
        view.addObject("token", responseDataUtil.getData());
        return view;
    }



    /**
     * 保存模版
     */
    @RequestMapping(value = "/save/{enterpriseId}/{accountId}", method = RequestMethod.POST)
    public Map<String, String> save(@RequestBody IntellectTemplateInfoValidator intellectTemplateInfoValidator, @PathVariable String enterpriseId, @PathVariable String accountId, HttpServletRequest request) {

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[intellectTemplateInfoValidator]:{}", new Gson().toJson(intellectTemplateInfoValidator));
        intellectTemplateInfoValidator.setId(UUID.uuid32());
        intellectTemplateInfoValidator.setEnterpriseId(enterpriseId);
        intellectTemplateInfoValidator.setAccountId(accountId);
        intellectTemplateInfoValidator.setSmsExample(intellectTemplateInfoValidator.getDescription());
        intellectTemplateInfoValidator.setTemplateCheckStatus(1);
        intellectTemplateInfoValidator.setTemplateStatus(1);
        intellectTemplateInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
        intellectTemplateInfoValidator.setCreatedBy(user.getRealName());
        //保存操作
        intellectTemplateInfoService.save(intellectTemplateInfoValidator);
        Map<String, String> result = new HashMap<>();
        result.put("result", "ok");
        return result;
    }


}
