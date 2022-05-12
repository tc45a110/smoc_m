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
import com.smoc.cloud.intelligence.remote.request.RequestGetTemplateStatus;
import com.smoc.cloud.intelligence.remote.request.RequestQueryTemplates;
import com.smoc.cloud.intelligence.remote.response.ResponseDataUtil;
import com.smoc.cloud.intelligence.remote.response.ResponseGetTemplateStatus;
import com.smoc.cloud.intelligence.remote.response.ResponseTemplateInfo;
import com.smoc.cloud.intelligence.remote.service.RequestService;
import com.smoc.cloud.intelligence.service.IntellectTemplateInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
        params.setPageSize(10);
        params.setCurrentPage(1);
        IntellectTemplateInfoValidator intellectTemplateInfoValidator = new IntellectTemplateInfoValidator();
        params.setParams(intellectTemplateInfoValidator);

        //查询
        ResponseData<PageList<IntellectTemplateInfoValidator>> data = intellectTemplateInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseDataUtil<String> responseDataUtil = requestService.getCompanyToken();
        //log.info("[token]:{}", new Gson().toJson(responseDataUtil));

        view.addObject("token", responseDataUtil.getData());
        view.addObject("intellectTemplateInfoValidator", intellectTemplateInfoValidator);
        view.addObject("list", data.getData().getList());
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
        pageParams.setParams(intellectTemplateInfoValidator);

        //查询
        ResponseData<PageList<IntellectTemplateInfoValidator>> data = intellectTemplateInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseDataUtil<String> responseDataUtil = requestService.getCompanyToken();
        //log.info("[token]:{}", new Gson().toJson(responseDataUtil));

        view.addObject("token", responseDataUtil.getData());
        view.addObject("intellectTemplateInfoValidator", intellectTemplateInfoValidator);
        view.addObject("list", data.getData().getList());
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
        ResponseDataUtil<List<ResponseTemplateInfo>> templates = requestService.queryTemplates(queryTemplates);
        if (!(0 == templates.getSubCode())) {
            view.addObject("error", templates.getMessage());
            return view;
        }
        params.setTotalRows(templates.getPageInfo().getTotal());
        //log.info("[templates]:{}", new Gson().toJson(templates));


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
        ResponseDataUtil<List<ResponseTemplateInfo>> templates = requestService.queryTemplates(queryTemplates);
        if (!(0 == templates.getSubCode())) {
            view.addObject("error", templates.getMessage());
            return view;
        }
        pageParams.setTotalRows(templates.getPageInfo().getTotal());
        //log.info("[templates]:{}", new Gson().toJson(templates));

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

        view.addObject("jsonData", templates.replace("\\", "").replace("\"[", "[").replace("]\"", "]"));
        return view;

    }

    /**
     * 提交审核（iframe）
     *
     * @return
     */
    @RequestMapping(value = "/submit/template/{templateId}", method = RequestMethod.GET)
    public ModelAndView submit(@PathVariable String templateId) {
        ModelAndView view = new ModelAndView("intelligence/template/template_list");

        ResponseDataUtil<Map<String, Object>> submitTemplate = requestService.submitTemplate(new Long(templateId));
        if (!(0 == submitTemplate.getSubCode())) {
            view.addObject("error", submitTemplate.getMessage());
            return view;
        }
        log.info("[submitTemplate]:{}", new Gson().toJson(submitTemplate));
        ResponseData updateTplIdData = intellectTemplateInfoService.updateTplIdAndStatus(templateId, submitTemplate.getData().get("tplId").toString(), 1);
        if (!ResponseCode.SUCCESS.getCode().equals(updateTplIdData.getCode())) {
            view.addObject("error", updateTplIdData.getCode() + ":" + updateTplIdData.getMessage());
            return view;
        }

        view.setView(new RedirectView("/intel/template/list", true, false));
        return view;

    }

    /**
     * 删除模版（iframe）
     *
     * @return
     */
    @RequestMapping(value = "/delete/template/{templateId}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable String templateId) {
        ModelAndView view = new ModelAndView("intelligence/template/template_list");
        ResponseDataUtil<Map<String, Object>> deleteTemplate = requestService.deleteTemplate(new Long(templateId));
        log.info("[deleteTemplate]:{}", new Gson().toJson(deleteTemplate));
        if (!(0 == deleteTemplate.getSubCode())) {
            view.addObject("error", deleteTemplate.getMessage());
            return view;
        }

        ResponseData deleteResponseData = intellectTemplateInfoService.updateStatusByTemplateId(templateId, 0);
        if (!ResponseCode.SUCCESS.getCode().equals(deleteResponseData.getCode())) {
            view.addObject("error", deleteResponseData.getCode() + ":" + deleteResponseData.getMessage());
            return view;
        }
        view.setView(new RedirectView("/intel/template/list", true, false));
        return view;

    }


    /**
     * 进入模版编辑器
     *
     * @param accountId
     * @param enterpriseId
     * @param action       add、update、copy
     * @param templateId
     * @return
     */
    @RequestMapping(value = "/template_main/{accountId}/{enterpriseId}/{action}/{templateId}", method = RequestMethod.GET)
    public ModelAndView template_main(@PathVariable String accountId, @PathVariable String enterpriseId, @PathVariable String action, @PathVariable String templateId) {

        ModelAndView view = new ModelAndView("intelligence/template/template_main");
        ResponseDataUtil<String> responseDataUtil = requestService.getCompanyToken();
        log.info("[token]:{}", new Gson().toJson(responseDataUtil));

        view.addObject("templateId", templateId);
        view.addObject("action", action);
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
        intellectTemplateInfoValidator.setTemplateCheckStatus(0);
        intellectTemplateInfoValidator.setTemplateStatus(1);
        intellectTemplateInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
        intellectTemplateInfoValidator.setCreatedBy(user.getRealName());
        //保存操作
        intellectTemplateInfoService.save(intellectTemplateInfoValidator);
        Map<String, String> result = new HashMap<>();
        result.put("result", "ok");
        return result;
    }

    /**
     * 查询模版状态，并同步修改模版状态
     */
    @RequestMapping(value = "/getTemplateStatus/{tplId}", method = RequestMethod.GET)
    public ResponseGetTemplateStatus getTemplateStatus(@PathVariable String tplId) {
        RequestGetTemplateStatus templateStatus = new RequestGetTemplateStatus();
        List<String> tplIds = new ArrayList<>();
        tplIds.add(tplId);
        templateStatus.setTplIds(tplIds);
        ResponseDataUtil<List<ResponseGetTemplateStatus>> templateStatusResponseDataUtil = requestService.getTemplateStatus(templateStatus);
        log.info("[templateStatus]:{}", new Gson().toJson(templateStatusResponseDataUtil));
        if (!(0 == templateStatusResponseDataUtil.getSubCode())) {
            return null;
        }
        ResponseGetTemplateStatus  status =  templateStatusResponseDataUtil.getData().get(0);
        intellectTemplateInfoService.updateCheckStatusByTplId(status.getTplId(),status.getAuditState());
        return status;
    }

    /**
     * 模版预览
     *
     * @return
     */
    @RequestMapping(value = "/preview/{tplId}", method = RequestMethod.GET)
    public ModelAndView preview(@PathVariable String tplId) {
        ModelAndView view = new ModelAndView("intelligence/short_link/short_link_edit");

        RequestQueryTemplates queryTemplates = new RequestQueryTemplates();
        queryTemplates.setPage(1);
        queryTemplates.setSize(10);
        queryTemplates.setTplId(tplId);
        ResponseDataUtil<List<ResponseTemplateInfo>> templates = requestService.queryTemplates(queryTemplates);
        //log.info("[templates]:{}", new Gson().toJson(templates));
        if (!(0 == templates.getSubCode())) {
            view.addObject("error", templates.getMessage());
            return view;
        }
        if (null == templates.getData() || templates.getData().size() < 1) {
            view.addObject("error", "未查询到模版数据");
            return view;
        }
        ResponseTemplateInfo templateInfo = templates.getData().get(0);
        Map<String, String> account = requestService.buildHeader();
        String url = "&Ab=" + account.get("account") + "&Sm=" + account.get("pwd") + "&Tt=" + account.get("timestamp");
        view.setViewName("redirect:"+templateInfo.getPreviewUrl()+url);
        return view;

    }


}
