package com.smoc.cloud.intelligence.controller;


import com.google.gson.Gson;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.common.smoc.intelligence.ShortLinkModel;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.intelligence.remote.request.RequestApplyShortUrl;
import com.smoc.cloud.intelligence.remote.request.RequestParamList;
import com.smoc.cloud.intelligence.remote.request.RequestQueryTemplates;
import com.smoc.cloud.intelligence.remote.response.*;
import com.smoc.cloud.intelligence.remote.service.RequestService;
import com.smoc.cloud.intelligence.service.IntellectShortUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 生成短链
 */
@Slf4j
@RestController
@RequestMapping("intel/short/url")
public class IntellectShortUrlController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private IntellectShortUrlService intellectShortUrlService;

    /**
     * 智能短链列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("intelligence/short_link/short_link_list");

        //初始化数据
        PageParams<IntellectShortUrlValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IntellectShortUrlValidator intellectShortUrlValidator = new IntellectShortUrlValidator();
        params.setParams(intellectShortUrlValidator);

        //查询
        ResponseData<PageList<IntellectShortUrlValidator>> data = intellectShortUrlService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("intellectShortUrlValidator", intellectShortUrlValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", params);
        return view;

    }

    /**
     * 智能短链分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute IntellectShortUrlValidator intellectShortUrlValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("intelligence/short_link/short_link_list");
        pageParams.setParams(intellectShortUrlValidator);

        //查询
        ResponseData<PageList<IntellectShortUrlValidator>> data = intellectShortUrlService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("intellectShortUrlValidator", intellectShortUrlValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", pageParams);
        return view;

    }

    /**
     * 进入生成短链页
     *
     * @return
     */
    @RequestMapping(value = "/add/{enterpriseId}/{accountId}/{tplId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String enterpriseId, @PathVariable String accountId, @PathVariable String tplId) {
        ModelAndView view = new ModelAndView("intelligence/short_link/short_link_edit");

        RequestQueryTemplates queryTemplates = new RequestQueryTemplates();
        queryTemplates.setPage(1);
        queryTemplates.setSize(10);
        queryTemplates.setTplId(tplId);
        ResponseDataUtil<List<ResponseTemplateInfo>> templates = requestService.queryTemplates(queryTemplates);
        if (!(0 == templates.getSubCode())) {
            view.addObject("error", templates.getMessage());
            return view;
        }
        if (null == templates.getData() || templates.getData().size() < 1) {
            view.addObject("error", "未查询到模版数据");
            return view;
        }
        ResponseTemplateInfo templateInfo = templates.getData().get(0);
        templateInfo.setPages("");
        log.info("[templateInfo]:{}", new Gson().toJson(templateInfo));

        ShortLinkModel applyShortUrl = new ShortLinkModel();
        applyShortUrl.setTplId(templateInfo.getTplId());
        applyShortUrl.setAimCodeType(1);

        String params = "";
        //组织参数
        if (0 < templateInfo.getParamcnt()) {
            for (ResponseParamArr paramArr : templateInfo.getParamArr()) {
                if (StringUtils.isEmpty(params)) {
                    params = paramArr.getName();
                } else {
                    params += "," + paramArr.getName();
                }
            }
        }
        applyShortUrl.setParams(params);

        view.addObject("accountId", accountId);
        view.addObject("enterpriseId", enterpriseId);
        view.addObject("applyShortUrl", applyShortUrl);
        view.addObject("templateInfo", templateInfo);
        return view;

    }

    /**
     * 进入生成短链页
     *
     * @return
     */
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ModelAndView generate(@ModelAttribute ShortLinkModel shortLinkModel, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("intelligence/short_link/short_link_edit");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        //完成参数规则验证
        if (!MpmValidatorUtil.validate(shortLinkModel)) {
            view.addObject("error", MpmValidatorUtil.validateMessage(shortLinkModel));
            return view;
        }

        RequestApplyShortUrl applyShortUrl = new RequestApplyShortUrl();
        applyShortUrl.setTplId(shortLinkModel.getTplId());
        applyShortUrl.setShowTimes(shortLinkModel.getShowTimes());
        applyShortUrl.setAimCodeType(shortLinkModel.getAimCodeType());
        applyShortUrl.setExpireTimes(shortLinkModel.getExpireTimes());

        //组织签名数据
        String[] signs = shortLinkModel.getSmsSigns().split(";");
        applyShortUrl.setSmsSigns(signs);

        //参数对象集合
        List<RequestParamList> paramList = new ArrayList<>();

        RequestParamList requestParamList = new RequestParamList();
        requestParamList.setCustFlag(shortLinkModel.getEnterpriseId());
        requestParamList.setCustId(shortLinkModel.getAccountId());

        //组织动态参数
        Map<String, String> dyncParams = new HashMap<>();
        if (!StringUtils.isEmpty(shortLinkModel.getParams())) {
            String[] params = shortLinkModel.getParams().split(",");
            for (int j = 0; j < params.length; j++) {
                dyncParams.put(params[j], request.getParameter(params[j]));
            }
        }
        requestParamList.setDyncParams(dyncParams);
        requestParamList.setCustomUrl(shortLinkModel.getCustomUrl());
        requestParamList.setExtData("");

        paramList.add(requestParamList);
        applyShortUrl.setParamList(paramList);

        ResponseDataUtil<ResponseShortUrl> shortUrlResponseDataUtil = requestService.applyShortUrl(applyShortUrl);
        log.info("[短链请求响应数据]:{}", new Gson().toJson(shortUrlResponseDataUtil));
        if (!(0 == shortUrlResponseDataUtil.getSubCode())) {
            view.addObject("error", shortUrlResponseDataUtil.getMessage());
            return view;
        }

        ResponseParamList resultParamList = shortUrlResponseDataUtil.getData().getParamList().get(0);

        //组织保存数据
        IntellectShortUrlValidator shortUrlValidator = new IntellectShortUrlValidator();
        BeanUtils.copyProperties(resultParamList, shortUrlValidator);
        shortUrlValidator.setId(UUID.uuid32());
        shortUrlValidator.setTplId(shortUrlResponseDataUtil.getData().getTplId());
        shortUrlValidator.setShowTimes(shortLinkModel.getShowTimes());
        shortUrlValidator.setExpireTimes(shortLinkModel.getExpireTimes());
        shortUrlValidator.setDyncParams(new Gson().toJson(resultParamList.getDyncParams()));
        shortUrlValidator.setResultCode(resultParamList.getResultCode()+"");
        shortUrlValidator.setCreatedBy(user.getRealName());
        shortUrlValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
        intellectShortUrlService.save(shortUrlValidator);
        view.setView(new RedirectView("/intel/short/url/list", true, false));
        return view;

    }


}
