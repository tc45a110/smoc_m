package com.smoc.cloud.message.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageMoInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.message.service.MessageMoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * 上行信息
 */
@Slf4j
@Controller
@RequestMapping("/message/mo")
public class MessageMoController {

    @Autowired
    private MessageMoService messageMoService;

    /**
     *  查询上行信息
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("message/message_mo_list");

        //初始化数据
        PageParams<MessageMoInfoValidator> params = new PageParams<MessageMoInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageMoInfoValidator messageMoInfoValidator = new MessageMoInfoValidator();
        messageMoInfoValidator.setStartDate(DateTimeUtils.getDateFormat(new Date()));
        messageMoInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        messageMoInfoValidator.setMtStartDate(DateTimeUtils.getDateFormat(DateTimeUtils.dateAddDays(new Date(),-4)));
        messageMoInfoValidator.setMtEndDate(DateTimeUtils.getDateFormat(new Date()));
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
     * w上行信息分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView webAllPage(@ModelAttribute MessageMoInfoValidator messageMoInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("message/message_mo_list");

        //分页查询
        if (!StringUtils.isEmpty(messageMoInfoValidator.getStartDate())) {
            String[] date = messageMoInfoValidator.getStartDate().split(" - ");
            messageMoInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageMoInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }
        if (!StringUtils.isEmpty(messageMoInfoValidator.getMtStartDate())) {
            String[] date = messageMoInfoValidator.getMtStartDate().split(" - ");
            messageMoInfoValidator.setMtStartDate(StringUtils.trimWhitespace(date[0]));
            messageMoInfoValidator.setMtEndDate(StringUtils.trimWhitespace(date[1]));
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
