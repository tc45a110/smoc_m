package com.smoc.cloud.message.controller;


import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageMoInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.message.service.MessageMoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 短信上行
 */
@Slf4j
@RestController
@RequestMapping("/message/mo")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class MessageMoController {

    @Autowired
    private MessageMoService messageMoService;

    /**
     * 短信发送列表
     * @param request
     * @return
     */
    @RequestMapping(value = "list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/mo/message_mo_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<MessageMoInfoValidator> params = new PageParams<MessageMoInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageMoInfoValidator messageMoInfoValidator = new MessageMoInfoValidator();
        messageMoInfoValidator.setEnterpriseId(user.getOrganization());
        messageMoInfoValidator.setBusinessType(businessType);
        Date startDate = DateTimeUtils.getFirstMonth(12);
        messageMoInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageMoInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageMoInfoValidator);

        //查询
        ResponseData<PageList<MessageMoInfoValidator>> data = messageMoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageMoInfoValidator", messageMoInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 短信发送分页
     * @param request
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageMoInfoValidator messageMoInfoValidator,PageParams pageParams,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/mo/message_mo_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        messageMoInfoValidator.setEnterpriseId(user.getOrganization());
        if (!StringUtils.isEmpty(messageMoInfoValidator.getStartDate())) {
            String[] date = messageMoInfoValidator.getStartDate().split(" - ");
            messageMoInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageMoInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }
        pageParams.setParams(messageMoInfoValidator);

        //查询
        ResponseData<PageList<MessageMoInfoValidator>> data = messageMoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageMoInfoValidator", messageMoInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

}
