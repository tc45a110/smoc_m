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
import com.smoc.cloud.common.smoc.configuate.qo.ChannelInterfaceInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticItemsModel;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticModel;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.channel.service.ChannelService;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseWebService;
import com.smoc.cloud.message.service.MessageDailyStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
 * 业务账号信息查询
 */
@Slf4j
@Controller
@RequestMapping("/query/account")
public class QueryAccountInfoController {

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private MessageDailyStatisticService messageDailyStatisticService;

    /**
     *  查询业务账号发送量
     * @return
     */
    @RequestMapping(value = "/sendStatistics/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("query/account/account_message_send_list");

        //初始化数据
        PageParams<AccountSendStatisticModel> params = new PageParams<AccountSendStatisticModel>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountSendStatisticModel accountSendStatisticModel = new AccountSendStatisticModel();
        Date startDate = DateTimeUtils.dateAddDays(new Date(),-1);
        accountSendStatisticModel.setStartDate(DateTimeUtils.getDateFormat(startDate));
        accountSendStatisticModel.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(accountSendStatisticModel);

        //查询
        ResponseData<PageList<AccountSendStatisticModel>> data = businessAccountService.queryAccountSendStatistics(params);
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
        view.addObject("accountSendStatisticModel", accountSendStatisticModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 查询业务账号发送量分页
     *
     * @return
     */
    @RequestMapping(value = "/sendStatistics/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountSendStatisticModel accountSendStatisticModel, PageParams pageParams) {
        ModelAndView view = new ModelAndView("query/account/account_message_send_list");

        //日期格式
        if (!StringUtils.isEmpty(accountSendStatisticModel.getStartDate())) {
            String[] date = accountSendStatisticModel.getStartDate().split(" - ");
            accountSendStatisticModel.setStartDate(StringUtils.trimWhitespace(date[0]));
            accountSendStatisticModel.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        pageParams.setParams(accountSendStatisticModel);

        ResponseData<PageList<AccountSendStatisticModel>> data = businessAccountService.queryAccountSendStatistics(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        MessageDailyStatisticValidator messageDailyStatisticValidator = new MessageDailyStatisticValidator();
        messageDailyStatisticValidator.setStartDate(accountSendStatisticModel.getStartDate());
        messageDailyStatisticValidator.setEndDate(accountSendStatisticModel.getEndDate());
        messageDailyStatisticValidator.setBusinessAccount(accountSendStatisticModel.getBusinessAccount());
        messageDailyStatisticValidator.setAccountName(accountSendStatisticModel.getAccountName());
        messageDailyStatisticValidator.setEnterpriseName(accountSendStatisticModel.getEnterpriseName());
        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object> countMap =  count.getData();

        view.addObject("countMap", countMap);
        view.addObject("accountSendStatisticModel", accountSendStatisticModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 导出excel
     *
     * @return
     */
    @RequestMapping(value = "/excel/{startDate}/{account}", method = RequestMethod.GET)
    public void excel(@PathVariable String account, @PathVariable String startDate, HttpServletResponse response, HttpServletRequest request) {

        //运营商
        Map<String,String>  carrierMap = this.carrier(request);
        //业务类型
        Map<String,String>  bussinessTypeMap = this.businessType(request);

        //初始化数据
        PageParams<AccountSendStatisticModel> params = new PageParams<AccountSendStatisticModel>();
        params.setPageSize(1);
        params.setCurrentPage(1);
        AccountSendStatisticModel accountSendStatisticModel = new AccountSendStatisticModel();
        accountSendStatisticModel.setStartDate(startDate);
        accountSendStatisticModel.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(accountSendStatisticModel);

        //查询
        ResponseData<PageList<AccountSendStatisticModel>> data = businessAccountService.queryAccountSendStatistics(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        AccountSendStatisticModel info = new AccountSendStatisticModel();
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
            String fileName = URLEncoder.encode(info.getMessageDate()+"-"+info.getBusinessAccount()+ "发送量汇总.xlsx", "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);

            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(fis).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            Map<String, Object> map = new HashMap<>();
            map.put("title", info.getMessageDate()+"-"+info.getBusinessAccount()+" "+info.getEnterpriseName()+ "发送量汇总");
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
