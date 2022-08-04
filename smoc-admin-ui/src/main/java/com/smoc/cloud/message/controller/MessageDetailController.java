package com.smoc.cloud.message.controller;

import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.message.service.MessageDetailInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 消息明细
 **/
@Controller
@RequestMapping("/message/detail")
public class MessageDetailController {

    @Autowired
    private MessageDetailInfoService messageDetailInfoService;

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * 消息明细查询
     *
     * @return
     */
    @RequestMapping(value = "/list/{enterpriseFlag}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String enterpriseFlag, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("message/message_detail_list");

        //初始化数据
        PageParams<MessageDetailInfoValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageDetailInfoValidator messageDetailInfoValidator = new MessageDetailInfoValidator();
        Date startDate = DateTimeUtils.dateAddDays(new Date(),-2);
        messageDetailInfoValidator.setStartDate(DateTimeUtils.getDateTimeFormat(startDate));
        messageDetailInfoValidator.setEndDate(DateTimeUtils.getDateTimeFormat(new Date()));
        messageDetailInfoValidator.setEnterpriseFlag(enterpriseFlag);
        params.setParams(messageDetailInfoValidator);

        //查询
        ResponseData<PageList<MessageDetailInfoValidator>> data = messageDetailInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageDetailInfoValidator", messageDetailInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 消息明细分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageDetailInfoValidator messageDetailInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("message/message_detail_list");

        //日期格式
        if (!StringUtils.isEmpty(messageDetailInfoValidator.getStartDate())) {
            String[] date = messageDetailInfoValidator.getStartDate().split(" - ");
            messageDetailInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageDetailInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        pageParams.setParams(messageDetailInfoValidator);

        ResponseData<PageList<MessageDetailInfoValidator>> data = messageDetailInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageDetailInfoValidator", messageDetailInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * EC检索
     * @return
     */
    @RequestMapping(value = "/ec/customer/search", method = RequestMethod.GET)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("message/search/search_list");

        //初始化数据
        PageParams<EnterpriseBasicInfoValidator> params = new PageParams<EnterpriseBasicInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseBasicInfoValidator enterpriseBasicInfoValidator = new EnterpriseBasicInfoValidator();
        enterpriseBasicInfoValidator.setFlag("1");//不查询认证企业
        params.setParams(enterpriseBasicInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseBasicInfoValidator>> data = enterpriseService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * EC检索
     * @return
     */
    @RequestMapping(value = "/ec/customer/search/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute EnterpriseBasicInfoValidator enterpriseBasicInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("message/search/search_list");

        //分页查询
        enterpriseBasicInfoValidator.setFlag("1");//不查询认证企业
        pageParams.setParams(enterpriseBasicInfoValidator);

        ResponseData<PageList<EnterpriseBasicInfoValidator>> data = enterpriseService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }
}
