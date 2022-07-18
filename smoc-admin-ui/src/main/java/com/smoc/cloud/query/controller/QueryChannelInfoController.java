package com.smoc.cloud.query.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticItemsModel;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticModel;
import com.smoc.cloud.common.smoc.query.model.ChannelSendStatisticModel;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.message.service.MessageDailyStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 通道发送量查询
 */
@Slf4j
@Controller
@RequestMapping("/query/channel")
public class QueryChannelInfoController {

    @Autowired
    private MessageDailyStatisticService messageDailyStatisticService;

    /**
     *  查询通道发送量
     * @return
     */
    @RequestMapping(value = "/sendStatistics/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("query/channel/channel_message_send_list");

        //初始化数据
        PageParams<ChannelSendStatisticModel> params = new PageParams<ChannelSendStatisticModel>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        ChannelSendStatisticModel channelSendStatisticModel = new ChannelSendStatisticModel();
        Date startDate = DateTimeUtils.dateAddDays(new Date(),-1);
        channelSendStatisticModel.setStartDate(DateTimeUtils.getDateFormat(startDate));
        channelSendStatisticModel.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(channelSendStatisticModel);

        //查询
        ResponseData<PageList<ChannelSendStatisticModel>> data = messageDailyStatisticService.queryChannelSendStatistics(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        MessageDailyStatisticValidator messageDailyStatisticValidator = new MessageDailyStatisticValidator();
        messageDailyStatisticValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageDailyStatisticValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object> countMap =  count.getData();

        view.addObject("countMap", countMap);
        view.addObject("channelSendStatisticModel", channelSendStatisticModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 查询通道发送量分页
     *
     * @return
     */
    @RequestMapping(value = "/sendStatistics/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ChannelSendStatisticModel channelSendStatisticModel, PageParams pageParams) {
        ModelAndView view = new ModelAndView("query/channel/channel_message_send_list");

        //日期格式
        if (!StringUtils.isEmpty(channelSendStatisticModel.getStartDate())) {
            String[] date = channelSendStatisticModel.getStartDate().split(" - ");
            channelSendStatisticModel.setStartDate(StringUtils.trimWhitespace(date[0]));
            channelSendStatisticModel.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        pageParams.setParams(channelSendStatisticModel);

        ResponseData<PageList<ChannelSendStatisticModel>> data = messageDailyStatisticService.queryChannelSendStatistics(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        MessageDailyStatisticValidator messageDailyStatisticValidator = new MessageDailyStatisticValidator();
        messageDailyStatisticValidator.setStartDate(channelSendStatisticModel.getStartDate());
        messageDailyStatisticValidator.setEndDate(channelSendStatisticModel.getEndDate());
        messageDailyStatisticValidator.setChannelId(channelSendStatisticModel.getChannelId());
        messageDailyStatisticValidator.setChannelName(channelSendStatisticModel.getChannelName());
        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object> countMap =  count.getData();

        view.addObject("countMap", countMap);
        view.addObject("channelSendStatisticModel", channelSendStatisticModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     *  查询通道下面账号发送量
     * @return
     */
    @RequestMapping(value = "/accountMessageSend/list/{channelId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String channelId, HttpServletResponse response, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("query/channel/channel_account_message_send_list");

        //初始化数据
        PageParams<AccountSendStatisticItemsModel> params = new PageParams<AccountSendStatisticItemsModel>();
        params.setPageSize(20);
        params.setCurrentPage(1);
        AccountSendStatisticItemsModel accountSendStatisticItemsModel = new AccountSendStatisticItemsModel();
        Date startDate = DateTimeUtils.dateAddDays(new Date(),-1);
        accountSendStatisticItemsModel.setStartDate(DateTimeUtils.getDateFormat(startDate));
        accountSendStatisticItemsModel.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        accountSendStatisticItemsModel.setChannelId(channelId);
        params.setParams(accountSendStatisticItemsModel);

        //查询
        ResponseData<PageList<AccountSendStatisticItemsModel>> data = messageDailyStatisticService.accountMessageSendListByChannel(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        MessageDailyStatisticValidator messageDailyStatisticValidator = new MessageDailyStatisticValidator();
        messageDailyStatisticValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageDailyStatisticValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object> countMap =  count.getData();

        view.addObject("countMap", countMap);
        view.addObject("accountSendStatisticItemsModel", accountSendStatisticItemsModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 查询通道下面账号发送量分页
     *
     * @return
     */
    @RequestMapping(value = "/accountMessageSend/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountSendStatisticItemsModel accountSendStatisticItemsModel, PageParams pageParams) {
        ModelAndView view = new ModelAndView("query/channel/channel_account_message_send_list");

        //日期格式
        if (!StringUtils.isEmpty(accountSendStatisticItemsModel.getStartDate())) {
            String[] date = accountSendStatisticItemsModel.getStartDate().split(" - ");
            accountSendStatisticItemsModel.setStartDate(StringUtils.trimWhitespace(date[0]));
            accountSendStatisticItemsModel.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        pageParams.setParams(accountSendStatisticItemsModel);

        ResponseData<PageList<AccountSendStatisticItemsModel>> data = messageDailyStatisticService.accountMessageSendListByChannel(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        MessageDailyStatisticValidator messageDailyStatisticValidator = new MessageDailyStatisticValidator();
        messageDailyStatisticValidator.setStartDate(accountSendStatisticItemsModel.getStartDate());
        messageDailyStatisticValidator.setEndDate(accountSendStatisticItemsModel.getEndDate());
        messageDailyStatisticValidator.setChannelId(accountSendStatisticItemsModel.getChannelId());
        messageDailyStatisticValidator.setBusinessAccount(accountSendStatisticItemsModel.getBusinessAccount());
        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object> countMap =  count.getData();

        view.addObject("countMap", countMap);
        view.addObject("accountSendStatisticItemsModel", accountSendStatisticItemsModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 导出excel
     *
     * @return
     */
   /* @RequestMapping(value = "/excel/{startDate}/{channelId}", method = RequestMethod.GET)
    public void excel(@PathVariable String channelId, @PathVariable String startDate, HttpServletResponse response, HttpServletRequest request) {

        //运营商
        Map<String,String>  carrierMap = this.carrier(request);
        //业务类型
        Map<String,String>  bussinessTypeMap = this.businessType(request);

        //初始化数据
        PageParams<ChannelSendStatisticModel> params = new PageParams<ChannelSendStatisticModel>();
        params.setPageSize(1);
        params.setCurrentPage(1);
        ChannelSendStatisticModel accountSendStatisticModel = new ChannelSendStatisticModel();
        accountSendStatisticModel.setStartDate(startDate);
        accountSendStatisticModel.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        accountSendStatisticModel.setChannelId(channelId);
        params.setParams(accountSendStatisticModel);

        //查询
        ResponseData<PageList<ChannelSendStatisticModel>> data = messageDailyStatisticService.queryChannelSendStatistics(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        ChannelSendStatisticModel info = new ChannelSendStatisticModel();
        if (null != data.getData() && data.getData().getList().size() > 0) {
            info = data.getData().getList().get(0);
            List<AccountSendStatisticItemsModel> dailyList = info.getItems();
            if(!StringUtils.isEmpty(dailyList) && dailyList.size()>0){
                for (AccountSendStatisticItemsModel model : dailyList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("messageDate", model.getMessageDate());
                    map.put("businessAccount", model.getBusinessAccount());
                    map.put("carrier", carrierMap.get(model.getCarrier()));
                    map.put("businessType", bussinessTypeMap.get(model.getBusinessType()));
                    map.put("channelId", model.getChannelId());
                    map.put("price", model.getPrice());
                    map.put("customerSubmitNum", model.getCustomerSubmitNum());
                    map.put("failureSubmitNum", model.getFailureSubmitNum());
                    map.put("messageSuccessNum", model.getMessageSuccessNum());
                    map.put("successSubmitNum", model.getSuccessSubmitNum());
                    map.put("messageFailureNum", model.getMessageFailureNum());
                    map.put("messageNoReportNum", model.getMessageNoReportNum());
                    list.add(map);
                }
            }
        }

        OutputStream out = null;
        BufferedOutputStream bos = null;

        try {
            ClassPathResource classPathResource = new ClassPathResource("static/files/templates/" + "message_account_send.xlsx");
            InputStream fis = classPathResource.getInputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(info.getMessageDate()+"-"+info.getChannelId()+ "发送量汇总.xlsx", "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);

            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(fis).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            Map<String, Object> map = new HashMap<>();
            map.put("title", info.getMessageDate()+"-"+info.getChannelId()+" "+info.getEnterpriseName()+ "发送量汇总");
            map.put("totalSuccessSubmitNum", info.getTotalSuccessSubmitNum());
            map.put("totalMessageSuccessNum", info.getTotalMessageSuccessNum());
            map.put("totalMessageFailureNum", info.getTotalMessageFailureNum());
            map.put("totalMessageNoReportNum", info.getTotalMessageNoReportNum());

            excelWriter.fill(list, writeSheet);
            excelWriter.fill(map, writeSheet);
            excelWriter.finish();
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 运营商和 国际区域合并
     */
    private Map<String,String> carrier(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //运营商
        DictType carrier = dictMap.get("carrier");
        //国际区域
        DictType internationalArea = dictMap.get("internationalArea");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : carrier.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : internationalArea.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        return dictValueMap;
    }

    /**
     * 业务类型
     */
    private Map<String,String> businessType(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //对接主体公司
        DictType businessType = dictMap.get("businessType");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : businessType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        return dictValueMap;
    }
}
