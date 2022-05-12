package com.smoc.cloud.intelligence.controller;


import com.google.gson.Gson;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.intelligence.remote.request.RequestQueryTemplates;
import com.smoc.cloud.intelligence.remote.response.ResponseDataUtil;
import com.smoc.cloud.intelligence.remote.response.ResponseTemplateInfo;
import com.smoc.cloud.intelligence.remote.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

/**
 * 生成短链
 */
@Slf4j
@RestController
@RequestMapping("intel/short/url")
public class IntellectShortUrlController {

    @Autowired
    private RequestService requestService;

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
//        queryTemplates.setBizId(enterpriseId);
//        queryTemplates.setBizFlag(accountId);
        ResponseDataUtil<List<ResponseTemplateInfo>> templates = requestService.queryTemplates(queryTemplates);
        log.info("[templates]:{}", new Gson().toJson(templates));
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
        view.addObject("template", templateInfo);
        return view;

    }


}
