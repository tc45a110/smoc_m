package com.smoc.cloud.customer.service;

import com.smoc.cloud.admin.security.remote.client.SystemExtendBusinessParameterFeignClient;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.validator.SystemExtendBusinessParamValidator;
import com.smoc.cloud.common.gateway.utils.AESConstUtil;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendFiltersValueValidator;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendSystemParamValueValidator;
import com.smoc.cloud.common.utils.DES;
import com.smoc.cloud.customer.remote.BusinessAccountFeignClient;
import com.smoc.cloud.identification.model.AccountExcelModel;
import com.smoc.cloud.parameter.remote.ParameterExtendSystemParamValueFeignClient;
import com.smoc.cloud.parameter.service.ParameterExtendFiltersValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 业务账号管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BusinessAccountService {

    @Autowired
    private BusinessAccountFeignClient businessAccountFeignClient;

    @Autowired
    private AccountFinanceService accountFinanceService;

    @Autowired
    private AccountInterfaceService accountInterfaceService;

    @Autowired
    private ParameterExtendFiltersValueService parameterExtendFiltersValueService;

    @Autowired
    private SystemExtendBusinessParameterFeignClient systemExtendBusinessParameterFeignClient;

    @Autowired
    private ParameterExtendSystemParamValueFeignClient parameterExtendSystemParamValueFeignClient;

    @Autowired
    private EnterpriseWebService enterpriseWebService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountBasicInfoValidator>> page(PageParams<AccountBasicInfoValidator> pageParams) {
        try {
            PageList<AccountBasicInfoValidator> pageList = this.businessAccountFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<AccountBasicInfoValidator> findById(String id) {
        try {
            ResponseData<AccountBasicInfoValidator> data = this.businessAccountFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(AccountBasicInfoValidator accountBasicInfoValidator, String op) {

        try {
            ResponseData data = this.businessAccountFeignClient.save(accountBasicInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销、启用账号
     *
     * @param id
     * @param status
     * @return
     */
    public ResponseData forbiddenAccountById(String id, String status) {
        try {
            ResponseData data = this.businessAccountFeignClient.forbiddenAccountById(id, status);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询企业所有的业务账号
     *
     * @param enterpriseId
     * @return
     */
    public ResponseData<List<AccountBasicInfoValidator>> findBusinessAccountByEnterpriseId(String enterpriseId) {
        try {
            ResponseData<List<AccountBasicInfoValidator>> data = this.businessAccountFeignClient.findBusinessAccountByEnterpriseId(enterpriseId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据业务类型查询企业所有的业务账号
     *
     * @param enterpriseId
     * @return
     */
    public ResponseData<List<AccountBasicInfoValidator>> findBusinessAccountByEnterpriseIdAndBusinessType(String enterpriseId,String businessType) {
        try {
            ResponseData<List<AccountBasicInfoValidator>> data = this.businessAccountFeignClient.findBusinessAccountByEnterpriseIdAndBusinessType(enterpriseId,businessType);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 生成业务账号
     *
     * @param enterpriseFlag
     * @return
     */
    public ResponseData<String> createAccountId(String enterpriseFlag) {
        try {
            ResponseData<String> accountId = this.businessAccountFeignClient.createAccountId(enterpriseFlag);
            return accountId;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 账号按维度统计发送量
     *
     * @param statisticSendData
     * @return
     */
    public AccountStatisticSendData statisticAccountSendNumber(AccountStatisticSendData statisticSendData) {
        ResponseData<List<AccountStatisticSendData>> responseData = this.businessAccountFeignClient.statisticAccountSendNumber(statisticSendData);
        List<AccountStatisticSendData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(AccountStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = list.stream().map(AccountStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);
        ;

        AccountStatisticSendData accountStatisticSendData = new AccountStatisticSendData();
        accountStatisticSendData.setMonthArray(month);
        accountStatisticSendData.setSendNumberArray(sendNumber);

        return accountStatisticSendData;
    }

    /**
     * EC业务账号投诉率统计
     *
     * @param statisticComplaintData
     * @return
     */
    public AccountStatisticComplaintData statisticComplaintMonth(AccountStatisticComplaintData statisticComplaintData) {
        ResponseData<List<AccountStatisticComplaintData>> responseData = this.businessAccountFeignClient.statisticComplaintMonth(statisticComplaintData);
        List<AccountStatisticComplaintData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(AccountStatisticComplaintData::getMonth).toArray(String[]::new);
        //投诉率
        String[] complaint = list.stream().map(AccountStatisticComplaintData::getComplaint).toArray(String[]::new);
        ;

        AccountStatisticComplaintData accountStatisticComplaintData = new AccountStatisticComplaintData();
        accountStatisticComplaintData.setMonthArray(month);
        accountStatisticComplaintData.setComplaintArray(complaint);

        return accountStatisticComplaintData;
    }

    /**
     * 生成excel：导出账号信息
     *
     * @param accountBasicInfoValidator
     * @return
     */
   /* public CopyOnWriteArrayList<AccountExcelModel> excelAccountInfo(AccountBasicInfoValidator accountBasicInfoValidator, HttpServletRequest request) {

        //查询财务信息
        AccountFinanceInfoValidator accountFinanceInfoValidator = new AccountFinanceInfoValidator();
        accountFinanceInfoValidator.setAccountId(accountBasicInfoValidator.getAccountId());
        ResponseData<List<AccountFinanceInfoValidator>> financeList = accountFinanceService.findByAccountId(accountFinanceInfoValidator);

        //查询接口信息
        ResponseData<AccountInterfaceInfoValidator> interfaceInfoValidator = accountInterfaceService.findById(accountBasicInfoValidator.getAccountId());

        CopyOnWriteArrayList<AccountExcelModel> list = new CopyOnWriteArrayList<>();

        //账号基本信息
        AccountExcelModel excelModel = new AccountExcelModel();
        excelModel.setKey("EC账号");
        excelModel.setValue(accountBasicInfoValidator.getAccountId());
        list.add(excelModel);

        AccountExcelModel excelModel1 = new AccountExcelModel();
        excelModel1.setKey("计费方式");
        AccountExcelModel excelModel2 = new AccountExcelModel();
        excelModel2.setKey("付费方式");
        if (!StringUtils.isEmpty(financeList.getData()) && financeList.getData().size() > 0) {
            AccountFinanceInfoValidator finance = financeList.getData().get(0);
            excelModel1.setValue(finance.getChargeType().equals("1") ? "成功接收" : "成功发送");
            excelModel2.setValue(finance.getPayType().equals("1") ? "预付费" : "后付费");
        }
        list.add(excelModel1);
        list.add(excelModel2);

        AccountExcelModel excelModel3 = new AccountExcelModel();
        excelModel3.setKey("扩展码");
        excelModel3.setValue("固定扩展码：" + accountBasicInfoValidator.getExtendCode() + "；随机扩展码位数：" + accountBasicInfoValidator.getRandomExtendCodeLength());
        list.add(excelModel3);

        //接口信息
        if (!StringUtils.isEmpty(interfaceInfoValidator.getData())) {
            AccountInterfaceInfoValidator interfaceInfo = interfaceInfoValidator.getData();

            AccountExcelModel excelModel4 = new AccountExcelModel();
            excelModel4.setKey(interfaceInfo.getProtocol() + "接口参数信息如下：");
            list.add(excelModel4);

            AccountExcelModel excelModel6 = new AccountExcelModel();
            excelModel6.setKey("账号密码");
            excelModel6.setValue(DES.decrypt(interfaceInfo.getAccountPassword()));
            list.add(excelModel6);

            AccountExcelModel excelModel5 = new AccountExcelModel();
            excelModel5.setKey("IP端口");
            excelModel5.setValue(StringUtils.isEmpty(interfaceInfo.getIdentifyIp()) ? "无限制" : interfaceInfo.getIdentifyIp());
            list.add(excelModel5);

            AccountExcelModel excelModel7 = new AccountExcelModel();
            excelModel7.setKey("发送速率(条/秒)");
            excelModel7.setValue("" + interfaceInfo.getMaxSendSecond());
            list.add(excelModel7);

            if ("CMPP".equals(interfaceInfo.getProtocol()) || "SGIP".equals(interfaceInfo.getProtocol()) || "SMGP".equals(interfaceInfo.getProtocol())) {
                AccountExcelModel excelModel8 = new AccountExcelModel();
                excelModel8.setKey("服务代码");
                excelModel8.setValue(interfaceInfo.getSrcId());
                list.add(excelModel8);

                AccountExcelModel excelModel9 = new AccountExcelModel();
                excelModel9.setKey("最大连接数");
                excelModel9.setValue("" + interfaceInfo.getMaxConnect());
                list.add(excelModel9);
            }

            if (interfaceInfo.getProtocol().contains("HTTP")) {
                AccountExcelModel excelModel10 = new AccountExcelModel();
                excelModel10.setKey("客户提交速率(次/秒)");
                excelModel10.setValue("" + interfaceInfo.getMaxSubmitSecond());
                list.add(excelModel10);
            }
        }

        //过滤参数信息
        ResponseData<List<ParameterExtendFiltersValueValidator>> data = this.parameterExtendFiltersValueService.findParameterValue(accountBasicInfoValidator.getAccountId());
        if (!StringUtils.isEmpty(data.getData()) && data.getData().size() > 0) {
            AccountExcelModel excelModel11 = new AccountExcelModel();
            excelModel11.setKey("过滤属性信息如下：");
            list.add(excelModel11);

            List<ParameterExtendFiltersValueValidator> filtersList = data.getData();
            //取字典数据
            ServletContext context = request.getServletContext();
            Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");

            for (ParameterExtendFiltersValueValidator filters : filtersList) {
                //黑词过滤、审核词过滤、黑名单层级过滤  这三列不用导出
                if("BLACK_WORD_FILTERING".equals(filters.getParamKey()) || "AUDIT_WORD_FILTERING".equals(filters.getParamKey()) || "BLACK_LIST_LEVEL_FILTERING".equals(filters.getParamKey())){
                    continue;
                }

                String values = getParamValues(filters, dictMap);
                AccountExcelModel excelModel12 = new AccountExcelModel();
                excelModel12.setKey(filters.getParamName());
                excelModel12.setValue(values);
                list.add(excelModel12);
            }
        }

        return list;
    }*/


    /**
     * 业务账号综合查询
     * @param params
     * @return
     */
    public ResponseData<PageList<AccountInfoQo>> accountAll(PageParams<AccountInfoQo> params) {
        try {
            ResponseData<PageList<AccountInfoQo>> pageList = this.businessAccountFeignClient.accountAll(params);
            return pageList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    public Map<String, Object> buildExcelMap(AccountBasicInfoValidator accountBasicInfoValidator, HttpServletRequest request) {

        //业务类型
        Map<String,String> businessTypeMap = this.businessType(request);
        //运营商
        Map<String,String>  carrierMap = this.carrier(request);

        Map<String, Object> buildMap = new HashMap<>();

        //查询财务信息
        AccountFinanceInfoValidator accountFinanceInfoValidator = new AccountFinanceInfoValidator();
        accountFinanceInfoValidator.setAccountId(accountBasicInfoValidator.getAccountId());
        ResponseData<List<AccountFinanceInfoValidator>> financeList = accountFinanceService.findByAccountId(accountFinanceInfoValidator);

        Map<String, Object> financeMap = new HashMap<>();
        financeMap.put("bussinessType",businessTypeMap.get(accountBasicInfoValidator.getBusinessType()));
        if (!StringUtils.isEmpty(financeList.getData()) && financeList.getData().size() > 0) {
            AccountFinanceInfoValidator finance = financeList.getData().get(0);
            financeMap.put("chargeType",finance.getChargeType().equals("1") ? "下发运营商计费" : "回执成功计费");
            financeMap.put("payType",finance.getPayType().equals("1") ? "预付费" : "后付费");
            String price = "";
            for(AccountFinanceInfoValidator info: financeList.getData()){
                if(StringUtils.isEmpty(price)){
                    price = carrierMap.get(info.getCarrier())+":"+info.getCarrierPrice();
                }else{
                    price += ","+ carrierMap.get(info.getCarrier())+":"+info.getCarrierPrice();
                }
            }
            financeMap.put("price",price);
        }

        //查询接口参数
        ResponseData<AccountInterfaceInfoValidator> interfaceInfoValidator = accountInterfaceService.findById(accountBasicInfoValidator.getAccountId());
        Map<String, Object> interfaceMap = new HashMap<>();
        List<Map<String, Object>> interfaceList = new ArrayList<>();
        if(!StringUtils.isEmpty(interfaceInfoValidator.getData())){
            AccountInterfaceInfoValidator interfaceInfo = interfaceInfoValidator.getData();
            interfaceMap.put("protocol",interfaceInfo.getProtocol());
            interfaceMap.put("accountPassword",DES.decrypt(interfaceInfo.getAccountPassword()));
            interfaceMap.put("maxSendSecond",interfaceInfo.getMaxSendSecond());
            //查询系统参数:系统对外IP
            ResponseData<ParameterExtendSystemParamValueValidator> systemParamValue = parameterExtendSystemParamValueFeignClient.findByBusinessTypeAndBusinessIdAndParamKey("SYSTEM_PARAM","SYSTEM","SYSTEM_PARAM_IP");
            if(!StringUtils.isEmpty(systemParamValue.getData())){
                interfaceMap.put("sysIp",systemParamValue.getData().getParamValue());
            }

            if("WEB".equals(interfaceInfo.getProtocol())){
                Map<String, Object> map = new HashMap<>();
                map.put("interKey","账号提交速率(次/秒)");
                map.put("interValue",interfaceInfo.getMaxSubmitSecond());

                Map<String, Object> map1 = new HashMap<>();
                map1.put("interKey","是否审核模板");
                map1.put("interValue",interfaceInfo.getExecuteCheck().equals("1") ? "是" : "否");

                interfaceList.add(map);
                interfaceList.add(map1);
            }

            if("CMPP".equals(interfaceInfo.getProtocol()) || "SGIP".equals(interfaceInfo.getProtocol()) || "SMGP".equals(interfaceInfo.getProtocol())){
                Map<String, Object> map = new HashMap<>();
                map.put("interKey","服务代码(接入码号)");
                map.put("interValue",interfaceInfo.getSrcId());

                Map<String, Object> map1 = new HashMap<>();
                map1.put("interKey","最大链接数");
                map1.put("interValue",interfaceInfo.getMaxConnect());

                Map<String, Object> map2 = new HashMap<>();
                map2.put("interKey","是否匹配模板");
                map2.put("interValue",interfaceInfo.getExecuteCheck().equals("1") ? "是" : "否");

                Map<String, Object> map3 = new HashMap<>();
                map3.put("interKey","是否审核内容");
                map3.put("interValue",interfaceInfo.getMatchingCheck().equals("1") ? "是" : "否");

                Map<String, Object> map4 = new HashMap<>();
                map4.put("interKey","客户鉴权IP");
                map4.put("interValue",StringUtils.isEmpty(interfaceInfo.getIdentifyIp()) ? "无限制" : interfaceInfo.getIdentifyIp());

                interfaceList.add(map);
                interfaceList.add(map1);
                interfaceList.add(map2);
                interfaceList.add(map3);
                interfaceList.add(map4);
            }

            if("HTTPS".equals(interfaceInfo.getProtocol())){
                Map<String, Object> map = new HashMap<>();
                map.put("interKey","账号提交速率(次/秒)");
                map.put("interValue",interfaceInfo.getMaxSubmitSecond());

                Map<String, Object> map1 = new HashMap<>();
                map1.put("interKey","是否审核模板");
                map1.put("interValue",interfaceInfo.getExecuteCheck().equals("1") ? "是" : "否");

                Map<String, Object> map2 = new HashMap<>();
                map2.put("interKey","客户鉴权IP");
                map2.put("interValue",StringUtils.isEmpty(interfaceInfo.getIdentifyIp()) ? "无限制" : interfaceInfo.getIdentifyIp());

                Map<String, Object> map3 = new HashMap<>();
                map3.put("interKey","上行短信推送地址");
                map3.put("interValue",interfaceInfo.getMoUrl());

                Map<String, Object> map4 = new HashMap<>();
                map4.put("interKey","状态报告推送地址");
                map4.put("interValue",interfaceInfo.getStatusReportUrl());

                interfaceList.add(map);
                interfaceList.add(map1);
                interfaceList.add(map2);
                interfaceList.add(map3);
                interfaceList.add(map4);
            }
        }

        //web登录账号
        List<Map<String, Object>> webList = new ArrayList<>();
        EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator = new EnterpriseWebAccountInfoValidator();
        enterpriseWebAccountInfoValidator.setEnterpriseId(accountBasicInfoValidator.getEnterpriseId());
        ResponseData<List<EnterpriseWebAccountInfoValidator>> webData = this.enterpriseWebService.page(enterpriseWebAccountInfoValidator);
        if(!StringUtils.isEmpty(webData.getData()) && webData.getData().size()>0){
            for(EnterpriseWebAccountInfoValidator info:webData.getData()){
                Map<String, Object> map = new HashMap<>();
                map.put("webLoginName",info.getWebLoginName());
                String webPassword = "";
                if(!StringUtils.isEmpty(info.getAesPassword())){
                    webPassword = AESConstUtil.decrypt(info.getAesPassword());
                }
                map.put("webPassword",webPassword);
                webList.add(map);
            }
        }

        //接口账号发送属性
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("transferType",accountBasicInfoValidator.getTransferType().equals("1") ? "是" : "否");
        String value = "";
        if(accountBasicInfoValidator.getRandomExtendCodeLength()>0){
            value = "；随机扩展码位数："+accountBasicInfoValidator.getRandomExtendCodeLength();
        }
        messageMap.put("extendCode",!StringUtils.isEmpty(accountBasicInfoValidator.getExtendCode())? "固定扩展码：" +accountBasicInfoValidator.getExtendCode() + value: "固定扩展码：无");
        //查询系统参数
        ResponseData<ParameterExtendSystemParamValueValidator> systemParamValue = parameterExtendSystemParamValueFeignClient.findByBusinessTypeAndBusinessIdAndParamKey("SYSTEM_PARAM","SYSTEM","MAX_SMS_TEXT_LENGTH");
        if(!StringUtils.isEmpty(systemParamValue.getData())){
            messageMap.put("messageLength",systemParamValue.getData().getParamValue());
        }

        //过滤参数信息
        List<Map<String, Object>> messageFiltersList = new ArrayList<>();
        ResponseData<List<ParameterExtendFiltersValueValidator>> data = this.parameterExtendFiltersValueService.findParameterValue(accountBasicInfoValidator.getAccountId());
        if (!StringUtils.isEmpty(data.getData()) && data.getData().size() > 0) {

            List<ParameterExtendFiltersValueValidator> filtersList = data.getData();
            //取字典数据
            ServletContext context = request.getServletContext();
            Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");

            for (ParameterExtendFiltersValueValidator filters : filtersList) {
                //黑词过滤、审核词过滤、黑名单层级过滤  这三列不用导出
                if("BLACK_WORD_FILTERING".equals(filters.getParamKey()) || "AUDIT_WORD_FILTERING".equals(filters.getParamKey()) || "BLACK_LIST_LEVEL_FILTERING".equals(filters.getParamKey())){
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                String values = getParamValues(filters, dictMap);
                map.put("filterKey",filters.getParamName());
                map.put("filterValue",values);

                messageFiltersList.add(map);
            }
        }

        buildMap.put("finance",financeMap);
        buildMap.put("interface",interfaceMap);
        buildMap.put("interfaceList",interfaceList);
        buildMap.put("webList",webList);
        buildMap.put("messageMap",messageMap);
        buildMap.put("messageFiltersList",messageFiltersList);

        return buildMap;
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

    private String getParamValues(ParameterExtendFiltersValueValidator filters, Map<String, DictType> dictMap) {

        String values = "";

        if (StringUtils.isEmpty(filters.getParamKey())) {
            return "";
        }

        //查询过滤信息，主要是取显示类型，用于查询字典FieldName
        SystemExtendBusinessParamValidator validator = new SystemExtendBusinessParamValidator();
        validator.setBusinessType("BUSINESS_ACCOUNT_FILTER");
        validator.setParamKey(filters.getParamKey());
        ResponseData<SystemExtendBusinessParamValidator> systemExtendBusinessParamValidator = systemExtendBusinessParameterFeignClient.findParamByBusinessTypeAndParamKey(validator);

        if (StringUtils.isEmpty(systemExtendBusinessParamValidator.getData())) {
            return "";
        }

        //文本
        if ("text".equals(systemExtendBusinessParamValidator.getData().getShowType())) {
            return filters.getParamValue();
        }

        //查询字典值
        DictType dictType = dictMap.get(systemExtendBusinessParamValidator.getData().getDictEnable());
        List<Dict> dictList = dictType.getDict();
        if (StringUtils.isEmpty(dictList) && dictList.size() > 0) {
            return "";
        }

        //下拉
        if ("select".equals(systemExtendBusinessParamValidator.getData().getShowType())) {
            for (Dict dict : dictList) {
                if (filters.getParamValue().equals(dict.getFieldCode())) {
                    values = dict.getFieldName();
                    return values;
                }
            }
        }

        //复选
        if ("checkbox".equals(systemExtendBusinessParamValidator.getData().getShowType())) {
            String[] params = filters.getParamValue().split(",");
            if (StringUtils.isEmpty(params) || params.length < 0) {
                return "";
            }

            for (int a = 0; a < params.length; a++) {
                for (int i = 0; i < dictList.size(); i++) {
                    Dict dict = dictList.get(i);
                    if (params[a].equals(dict.getFieldCode())) {
                        if (StringUtils.isEmpty(values)) {
                            values = dict.getFieldName();
                        } else {
                            values += "," + dict.getFieldName();
                        }
                        break;
                    }
                }
            }
        }

        return values;
    }
}
