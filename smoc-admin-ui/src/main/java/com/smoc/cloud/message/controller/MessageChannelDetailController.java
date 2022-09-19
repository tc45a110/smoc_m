package com.smoc.cloud.message.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.channel.service.ChannelService;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.message.service.MessageDetailInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
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
 * 通道消息明细
 **/
@Slf4j
@Controller
@RequestMapping("/message/detail")
public class MessageChannelDetailController {

    @Autowired
    private MessageDetailInfoService messageDetailInfoService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private ChannelService channelService;

    /**
     * 通道消息明细查询
     *
     * @return
     */
    @RequestMapping(value = "/channel/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("message/message_channel_detail_list");

        //初始化数据
        PageParams<MessageDetailInfoValidator> params = new PageParams<>();
        params.setPageSize(20);
        params.setCurrentPage(1);
        MessageDetailInfoValidator messageDetailInfoValidator = new MessageDetailInfoValidator();
        messageDetailInfoValidator.setStartDate(DateTimeUtils.getDateFormat(new Date())+" 00:00:00");
        messageDetailInfoValidator.setEndDate(DateTimeUtils.getDateTimeFormat(new Date()));
        messageDetailInfoValidator.setTableName(DateTimeUtils.currentDate(new Date()));
        params.setParams(messageDetailInfoValidator);

        //查询
        ResponseData<PageList<MessageDetailInfoValidator>> data = messageDetailInfoService.messageChannelPage(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询账号，主要用于页面上取账号名称
        Map<String, AccountBasicInfoValidator> accountMap = queryAccount("");

        //查询所有通道，用于页面取通道名称
        Map<String, ChannelBasicInfoQo> channelMap = queryChannelAll();

        view.addObject("messageDetailInfoValidator", messageDetailInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("accountMap", accountMap);
        view.addObject("channelMap", channelMap);

        //统计发送量、成功数、失败数、成功率、失败率
        statisticEnterpriseSendMessage(view, messageDetailInfoValidator,data.getData().getPageParams().getTotalRows());

        return view;
    }

    /**
     * 通道消息明细分页查询
     *
     * @return
     */
    @RequestMapping(value = "/channel/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageDetailInfoValidator messageDetailInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("message/message_channel_detail_list");

        //日期格式
        if (!StringUtils.isEmpty(messageDetailInfoValidator.getStartDate())) {
            String[] date = messageDetailInfoValidator.getStartDate().split(" - ");
            messageDetailInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageDetailInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        String tableName = messageDetailInfoValidator.getStartDate();
        messageDetailInfoValidator.setTableName(tableName.substring(0,10).replaceAll("-",""));
        pageParams.setParams(messageDetailInfoValidator);

        ResponseData<PageList<MessageDetailInfoValidator>> data = messageDetailInfoService.messageChannelPage(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询账号，主要用于页面上取账号名称
        Map<String, AccountBasicInfoValidator> accountMap = queryAccount("");

        //查询所有通道，用于页面取通道名称
        Map<String, ChannelBasicInfoQo> channelMap = queryChannelAll();

        view.addObject("messageDetailInfoValidator", messageDetailInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("accountMap", accountMap);
        view.addObject("channelMap", channelMap);

        //统计发送量、成功数、失败数、成功率、失败率
        statisticEnterpriseSendMessage(view, messageDetailInfoValidator,data.getData().getPageParams().getTotalRows());

        return view;
    }

    //统计发送量、成功数、失败数、成功率、失败率
    private void statisticEnterpriseSendMessage(ModelAndView view, MessageDetailInfoValidator messageDetailInfoValidator, long total ) {
        //统计发送成功量
        ResponseData<Map<String, Object>> sendData = messageDetailInfoService.statisticChannelSendMessage(messageDetailInfoValidator);
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

    //查询通道
    private Map<String, ChannelBasicInfoQo> queryChannelAll() {
        ChannelBasicInfoValidator channelBasicInfoValidator = new ChannelBasicInfoValidator();
        ResponseData<List<ChannelBasicInfoQo>> list = channelService.queryChannelAll(channelBasicInfoValidator);

        Map<String, ChannelBasicInfoQo> accountMap = new HashMap<>();
        if (!StringUtils.isEmpty(list.getData()) && list.getData().size() > 0) {
            accountMap = list.getData().stream().collect(Collectors.toMap(ChannelBasicInfoQo::getChannelId, Function.identity()));
        }

        return accountMap;
    }
}
