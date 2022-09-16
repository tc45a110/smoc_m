package com.smoc.cloud.message.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageCodeValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.message.service.MessageDetailInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 错误码分析
 **/
@Slf4j
@Controller
@RequestMapping("/message/code")
public class MessageCodeController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private MessageDetailInfoService messageDetailInfoService;

    @Autowired
    private BusinessAccountService businessAccountService;

    /**
     * EC检索
     *
     * @return
     */
    @RequestMapping(value = "/ec/customer/search", method = RequestMethod.GET)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("message/code/search_list");

        //初始化数据
        PageParams<EnterpriseBasicInfoValidator> params = new PageParams<EnterpriseBasicInfoValidator>();
        params.setPageSize(20);
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
     *
     * @return
     */
    @RequestMapping(value = "/ec/customer/search/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute EnterpriseBasicInfoValidator enterpriseBasicInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("message/code/search_list");

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

    /**
     * 消息状态码统计查询
     *
     * @return
     */
    @RequestMapping(value = "/list/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String enterpriseId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("message/code/message_code_list");

        //查询企业
        ResponseData<EnterpriseBasicInfoValidator> enterprise = enterpriseService.findById(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(enterprise.getCode())) {
            view.addObject("error", enterprise.getCode() + ":" + enterprise.getMessage());
            return view;
        }

        //初始化数据
        PageParams<MessageCodeValidator> params = new PageParams<MessageCodeValidator>();
        params.setPageSize(30);
        params.setCurrentPage(1);
        MessageCodeValidator messageCodeValidator = new MessageCodeValidator();
        messageCodeValidator.setStartDate(DateTimeUtils.getDateFormat(new Date())+" 00:00:00");
        messageCodeValidator.setEndDate(DateTimeUtils.getDateTimeFormat(new Date()));
        messageCodeValidator.setEnterpriseFlag(enterprise.getData().getEnterpriseFlag());
        messageCodeValidator.setEnterpriseId(enterpriseId);
        params.setParams(messageCodeValidator);
        //查询
        ResponseData<PageList<MessageCodeValidator>> data = messageDetailInfoService.messageCcodeStautsList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //统计发送量、成功数、失败数、成功率、失败率
        long total = statisticEnterpriseSendMessage(view, messageCodeValidator);

        //查询账号，主要用于页面上取账号名称
        Map<String, AccountBasicInfoValidator> accountMap = queryAccount(messageCodeValidator.getEnterpriseId());

        //循环计算占比
        statisticRatio(data.getData().getList(),total);

        view.addObject("messageCodeValidator", messageCodeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("accountMap", accountMap);

        return view;
    }

    /**
     * 消息状态码统计查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageCodeValidator messageCodeValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("message/code/message_code_list");

        //查询企业
        ResponseData<EnterpriseBasicInfoValidator> enterprise = enterpriseService.findById(messageCodeValidator.getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterprise.getCode())) {
            view.addObject("error", enterprise.getCode() + ":" + enterprise.getMessage());
            return view;
        }

        //日期格式
        if (!StringUtils.isEmpty(messageCodeValidator.getStartDate())) {
            String[] date = messageCodeValidator.getStartDate().split(" - ");
            messageCodeValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageCodeValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        messageCodeValidator.setEnterpriseFlag(enterprise.getData().getEnterpriseFlag());
        pageParams.setParams(messageCodeValidator);



        ResponseData<PageList<MessageCodeValidator>> data = messageDetailInfoService.messageCcodeStautsList(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //统计发送量、成功数、失败数、成功率、失败率
        long total = statisticEnterpriseSendMessage(view, messageCodeValidator);

        //查询账号，主要用于页面上取账号名称
        Map<String, AccountBasicInfoValidator> accountMap = queryAccount(messageCodeValidator.getEnterpriseId());

        //循环计算占比
        statisticRatio(data.getData().getList(),total);

        view.addObject("messageCodeValidator", messageCodeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("accountMap", accountMap);

        return view;
    }

    //循环计算占比
    private void statisticRatio(List<MessageCodeValidator> list, long total) {
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for(MessageCodeValidator info : list){
                if(total>0){
                    info.setRatio(new BigDecimal(info.getCodeNumber()).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                }
            }
        }
    }

    //统计发送量、成功数、失败数、成功率、失败率
    private long statisticEnterpriseSendMessage(ModelAndView view, MessageCodeValidator messageCodeValidator) {
        MessageDetailInfoValidator messageDetailInfoValidator = new MessageDetailInfoValidator();
        BeanUtils.copyProperties(messageCodeValidator, messageDetailInfoValidator);
        //统计发送成功量
        ResponseData<Map<String, Object>> sendData = messageDetailInfoService.statisticEnterpriseSendMessage(messageDetailInfoValidator);
        //成功总数
        long total = Long.parseLong("" + sendData.getData().get("TOTAL_SEND_SUM"));
        //成功总数
        long successSum = Long.parseLong("" + sendData.getData().get("SUCCESS_SEND_SUM"));
        //成功率
        BigDecimal successRatio = new BigDecimal(0);
        //失败总数
        long errorSum = total - successSum;
        //失败率
        BigDecimal errorRatio = new BigDecimal(0);
        if(total>0){
            successRatio = new BigDecimal(successSum).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            errorRatio = new BigDecimal(errorSum).divide(new BigDecimal(total),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }

        view.addObject("total", total);
        view.addObject("successSum", successSum);
        view.addObject("successRatio", successRatio.stripTrailingZeros().toPlainString());
        view.addObject("errorSum", errorSum);
        view.addObject("errorRatio", errorRatio.stripTrailingZeros().toPlainString());

        return total;
    }

    //查询账号
    private Map<String, AccountBasicInfoValidator> queryAccount(String enterpriseId) {

        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        accountBasicInfoValidator.setEnterpriseId(enterpriseId);
        ResponseData<List<AccountBasicInfoValidator>> list = businessAccountService.accountList(accountBasicInfoValidator);

        Map<String, AccountBasicInfoValidator> accountMap = new HashMap<>();
        if (!StringUtils.isEmpty(list.getData()) && list.getData().size() > 0) {
            accountMap = list.getData().stream().collect(Collectors.toMap(AccountBasicInfoValidator::getAccountId, Function.identity()));
        }

        return accountMap;
    }
}
