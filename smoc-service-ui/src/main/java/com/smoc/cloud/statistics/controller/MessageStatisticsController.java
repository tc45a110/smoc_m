package com.smoc.cloud.statistics.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.message.service.EnterpriseService;
import com.smoc.cloud.statistics.service.StatisticsMessageService;
import com.smoc.cloud.user.service.WebEnterpriseService;
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
 * 发送统计
 */
@Controller
@RequestMapping("/statistics/message")
public class MessageStatisticsController {

    @Autowired
    private StatisticsMessageService statisticsMessageService;

    @Autowired
    private WebEnterpriseService webEnterpriseService;

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * 统计发送量
     * @param request
     * @return
     */
    @RequestMapping(value = "/number/list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_send_number_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询企业，获取标识
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(user.getOrganization());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        //初始化数据
        PageParams<StatisticMessageSendData> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        StatisticMessageSendData statisticMessageSendData = new StatisticMessageSendData();
        statisticMessageSendData.setEnterpriseFlag(enterpriseData.getData().getEnterpriseFlag().toLowerCase());
        statisticMessageSendData.setBusinessType(businessType);
        Date startDate = DateTimeUtils.getFirstMonth(1);
        statisticMessageSendData.setStartDate(DateTimeUtils.getDateFormat(startDate));
        statisticMessageSendData.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(statisticMessageSendData);

        //查询
        ResponseData<PageList<StatisticMessageSendData>> data = statisticsMessageService.messageSendNumberList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //统计web端发送量
        ResponseData<Map<String, Object>> count = statisticsMessageService.webStatisticMessageCount(statisticMessageSendData);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        view.addObject("statisticMessageSendData", statisticMessageSendData);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("businessType", businessType);
        view.addObject("countMap", count.getData());
        return view;
    }

    /**
     * 短信提交记录分页
     * @param request
     * @return
     */
    @RequestMapping(value = "/number/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute StatisticMessageSendData statisticMessageSendData, PageParams params, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_send_number_list");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        if("1".equals(statisticMessageSendData.getFlag())){
            view = new ModelAndView("statistics/message_send_number_list_account");
        }else if("2".equals(statisticMessageSendData.getFlag())){
            view = new ModelAndView("statistics/message_send_number_list_sign");
        }else if("3".equals(statisticMessageSendData.getFlag())){
            view = new ModelAndView("statistics/message_send_number_list_carrier");
        }

        //查询企业，获取标识
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(user.getOrganization());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        //分页查询
        statisticMessageSendData.setEnterpriseFlag(enterpriseData.getData().getEnterpriseFlag().toLowerCase());
        if (!StringUtils.isEmpty(statisticMessageSendData.getStartDate())) {
            String[] date = statisticMessageSendData.getStartDate().split(" - ");
            statisticMessageSendData.setStartDate(StringUtils.trimWhitespace(date[0]));
            statisticMessageSendData.setEndDate(StringUtils.trimWhitespace(date[1]));
        }
        params.setParams(statisticMessageSendData);

        //查询
        ResponseData<PageList<StatisticMessageSendData>> data = statisticsMessageService.messageSendNumberList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //统计web端发送量
        ResponseData<Map<String, Object>> count = statisticsMessageService.webStatisticMessageCount(statisticMessageSendData);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        view.addObject("statisticMessageSendData", statisticMessageSendData);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams",data.getData().getPageParams());
        view.addObject("businessType", statisticMessageSendData.getBusinessType());
        view.addObject("countMap", count.getData());
        return view;
    }

    /**
     * 导出excel 结账单
     *
     * @return
     */
    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    public void excel(@ModelAttribute StatisticMessageSendData statisticMessageSendData, PageParams pageParams, HttpServletResponse response, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询企业标识
        ResponseData<EnterpriseBasicInfoValidator> ent = webEnterpriseService.findById(user.getOrganization());
        if (!ResponseCode.SUCCESS.getCode().equals(ent.getCode())) {
            return ;
        }

        //日期格式
        if (!StringUtils.isEmpty(statisticMessageSendData.getStartDate())) {
            String[] date = statisticMessageSendData.getStartDate().split(" - ");
            statisticMessageSendData.setStartDate(StringUtils.trimWhitespace(date[0]));
            statisticMessageSendData.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        statisticMessageSendData.setEnterpriseFlag(ent.getData().getEnterpriseFlag());
        pageParams.setPageSize(500000);
        pageParams.setCurrentPage(1);
        pageParams.setParams(statisticMessageSendData);

        //查询
        ResponseData<PageList<StatisticMessageSendData>> data = statisticsMessageService.messageSendNumberList(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        //统计web端发送量
        ResponseData<Map<String, Object>> count = statisticsMessageService.webStatisticMessageCount(statisticMessageSendData);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            return ;
        }

        Map<String,Object>  countMap = count.getData();

        List<Map<String, Object>> list = new ArrayList<>();
        if (null != data.getData() && data.getData().getList().size() > 0) {
            List<StatisticMessageSendData> dailyList = data.getData().getList();
            for (StatisticMessageSendData model : dailyList) {
                Map<String, Object> map = new HashMap<>();
                map.put("messageDate", model.getCreatedTime());
                if("1".equals(statisticMessageSendData.getFlag())){
                    map.put("type", model.getBusinessAccount());
                }
                if("2".equals(statisticMessageSendData.getFlag())){
                    map.put("type",model.getSign());
                }
                map.put("successSubmitNum", model.getSendNumber());
                map.put("messageSuccessNum", model.getFailureNumber());
                map.put("messageFailureNum", model.getSuccessNumber());
                map.put("messageNoReportNum", model.getNoReportNumber());
                map.put("successRate", model.getSuccessRate());
                map.put("failureRate", model.getFailureRate());
                map.put("noReportRate", model.getNoReportRate());
                list.add(map);
            }
        }

        OutputStream out = null;
        BufferedOutputStream bos = null;
        String excelName = statisticMessageSendData.getStartDate()+"-"+statisticMessageSendData.getEndDate();
        try {
            String files = "message_daily.xlsx";
            if(!StringUtils.isEmpty(statisticMessageSendData.getFlag())){
                files = "message_daily_type.xlsx";
            }
            ClassPathResource classPathResource = new ClassPathResource("static/files/templates/" + files);
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

            if(!StringUtils.isEmpty(statisticMessageSendData.getFlag())){
                if("1".equals(statisticMessageSendData.getFlag())){
                    map.put("type", "账号");
                }
                if("2".equals(statisticMessageSendData.getFlag())){
                    map.put("type", "签名");
                }
            }

            excelWriter.fill(list, writeSheet);
            excelWriter.fill(map, writeSheet);
            excelWriter.finish();
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
