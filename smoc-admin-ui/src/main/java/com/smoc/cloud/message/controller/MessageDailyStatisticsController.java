package com.smoc.cloud.message.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.gson.Gson;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.common.utils.DateTimeUtils;
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
 * 消息日统计明细
 **/
@Slf4j
@Controller
@RequestMapping("/message/daily/statistics")
public class MessageDailyStatisticsController {

    @Autowired
    private MessageDailyStatisticService messageDailyStatisticService;

    /**
     * 消息日统计明细查询
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("message/message_daily_statistics_list");

        //初始化数据
        PageParams<MessageDailyStatisticValidator> params = new PageParams<MessageDailyStatisticValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageDailyStatisticValidator messageDailyStatisticValidator = new MessageDailyStatisticValidator();

        Date startDate = DateTimeUtils.dateAddDays(new Date(),-3);
        messageDailyStatisticValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageDailyStatisticValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageDailyStatisticValidator);

        //查询
        ResponseData<PageList<MessageDailyStatisticValidator>> data = messageDailyStatisticService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object> countMap =  count.getData();
        //log.info("countMap：{}",new Gson().toJson(countMap));

        view.addObject("countMap", countMap);
        view.addObject("messageDailyStatisticValidator", messageDailyStatisticValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 消息日统计明细分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageDailyStatisticValidator messageDailyStatisticValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("message/message_daily_statistics_list");

        //日期格式
        if (!StringUtils.isEmpty(messageDailyStatisticValidator.getStartDate())) {
            String[] date = messageDailyStatisticValidator.getStartDate().split(" - ");
            messageDailyStatisticValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageDailyStatisticValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        pageParams.setParams(messageDailyStatisticValidator);

        //log.info("[messageDailyStatisticValidator]:{}",new Gson().toJson(messageDailyStatisticValidator));

        ResponseData<PageList<MessageDailyStatisticValidator>> data = messageDailyStatisticService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object>  countMap = count.getData();

        //log.info("countMap：{}",new Gson().toJson(countMap));
        view.addObject("countMap", countMap);
        view.addObject("messageDailyStatisticValidator", messageDailyStatisticValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 导出excel 结账单
     *
     * @return
     */
    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    public void excel(@ModelAttribute MessageDailyStatisticValidator messageDailyStatisticValidator, PageParams pageParams, HttpServletResponse response, HttpServletRequest request) {

        //运营商
        Map<String,String>  carrierMap = this.carrier(request);

        //日期格式
        if (!StringUtils.isEmpty(messageDailyStatisticValidator.getStartDate())) {
            String[] date = messageDailyStatisticValidator.getStartDate().split(" - ");
            messageDailyStatisticValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageDailyStatisticValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        pageParams.setPageSize(500000);
        pageParams.setCurrentPage(1);
        pageParams.setParams(messageDailyStatisticValidator);

        ResponseData<PageList<MessageDailyStatisticValidator>> data = messageDailyStatisticService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            return ;
        }

        Map<String,Object>  countMap = count.getData();

        List<Map<String, Object>> list = new ArrayList<>();
        if (null != data.getData() && data.getData().getList().size() > 0) {
            List<MessageDailyStatisticValidator> dailyList = data.getData().getList();
            for (MessageDailyStatisticValidator model : dailyList) {
                Map<String, Object> map = new HashMap<>();
                map.put("messageDate", model.getMessageDate());
                map.put("enterpriseName", model.getEnterpriseName());
                map.put("businessAccount", model.getBusinessAccount());
                map.put("carrier", carrierMap.get(model.getCarrier()));
                map.put("channelId", model.getChannelId());
                map.put("messageSign",model.getMessageSign());
                map.put("customerSubmitNum", model.getCustomerSubmitNum());
                map.put("failureSubmitNum", model.getFailureSubmitNum());
                map.put("messageSuccessNum", model.getMessageSuccessNum());
                map.put("successSubmitNum", model.getSuccessSubmitNum());
                map.put("messageFailureNum", model.getMessageFailureNum());
                map.put("messageNoReportNum", model.getMessageNoReportNum());
                list.add(map);
            }
        }

        OutputStream out = null;
        BufferedOutputStream bos = null;
        String excelName = messageDailyStatisticValidator.getStartDate()+"-"+messageDailyStatisticValidator.getEndDate();
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/files/templates/" + "message_daily.xlsx");
            InputStream fis = classPathResource.getInputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(excelName+ "发送量汇总.xlsx", "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);

            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(fis).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            Map<String, Object> map = new HashMap<>();
            map.put("date", excelName+ "发送量汇总");
            map.put("successSubmitNum", countMap.get("SUCCESS_SUBMIT_NUM"));
            map.put("messageSuccessNum", countMap.get("MESSAGE_SUCCESS_NUM"));
            map.put("messageFailureNum", countMap.get("MESSAGE_FAILURE_NUM"));
            map.put("messageNoReportNum", countMap.get("MESSAGE_NO_REPORT_NUM"));
            excelWriter.fill(list, writeSheet);
            excelWriter.fill(map, writeSheet);
            excelWriter.finish();
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
