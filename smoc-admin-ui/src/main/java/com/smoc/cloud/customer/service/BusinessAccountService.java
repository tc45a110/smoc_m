package com.smoc.cloud.customer.service;

import com.smoc.cloud.admin.security.remote.client.SystemExtendBusinessParameterFeignClient;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.validator.SystemExtendBusinessParamValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendFiltersValueValidator;
import com.smoc.cloud.common.utils.DES;
import com.smoc.cloud.customer.remote.BusinessAccountFeignClient;
import com.smoc.cloud.identification.model.AccountExcelModel;
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
        String[] sendNumber = list.stream().map(AccountStatisticSendData::getSendNumber).toArray(String[]::new);
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
    public CopyOnWriteArrayList<AccountExcelModel> excelAccountInfo(AccountBasicInfoValidator accountBasicInfoValidator, HttpServletRequest request) {

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
            excelModel4.setKey(interfaceInfo.getProtocol() + "接口参数");
            list.add(excelModel4);

            AccountExcelModel excelModel5 = new AccountExcelModel();
            excelModel5.setKey("IP端口");
            excelModel5.setValue(StringUtils.isEmpty(interfaceInfo.getIdentifyIp()) ? "无限制" : interfaceInfo.getIdentifyIp());
            list.add(excelModel5);

            AccountExcelModel excelModel6 = new AccountExcelModel();
            excelModel6.setKey("账号密码");
            excelModel6.setValue(DES.decrypt(interfaceInfo.getAccountPassword()));
            list.add(excelModel6);

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
            excelModel11.setKey("过滤属性");
            list.add(excelModel11);

            List<ParameterExtendFiltersValueValidator> filtersList = data.getData();
            //取字典数据
            ServletContext context = request.getServletContext();
            Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");

            for (ParameterExtendFiltersValueValidator filters : filtersList) {
                String values = getParamValues(filters,dictMap);

                AccountExcelModel excelModel12 = new AccountExcelModel();
                excelModel12.setKey(filters.getParamName());
                excelModel12.setValue(values);
                list.add(excelModel12);
            }
        }

        return list;
    }

    private String getParamValues(ParameterExtendFiltersValueValidator filters, Map<String, DictType> dictMap) {

        String values = "";

        if(StringUtils.isEmpty(filters.getParamKey())){
            return "";
        }

        SystemExtendBusinessParamValidator validator = new SystemExtendBusinessParamValidator();
        validator.setBusinessType("BUSINESS_ACCOUNT_FILTER");
        validator.setParamKey(filters.getParamKey());
        ResponseData<SystemExtendBusinessParamValidator> systemExtendBusinessParamValidator = systemExtendBusinessParameterFeignClient.findParamByBusinessTypeAndParamKey(validator);

        if(StringUtils.isEmpty(systemExtendBusinessParamValidator.getData())){
            return "";
        }

        //文本
        if("text".equals(systemExtendBusinessParamValidator.getData().getShowType())){
            return filters.getParamValue();
        }

        //查询字典值
        DictType dictType  = dictMap.get(systemExtendBusinessParamValidator.getData().getDictEnable());
        List<Dict> dictList = dictType.getDict();
        if(StringUtils.isEmpty(dictList) && dictList.size()>0){
            return "";
        }

        //下拉
        if("select".equals(systemExtendBusinessParamValidator.getData().getShowType())){
            for(Dict dict:dictList){
                if(filters.getParamValue().equals(dict.getFieldCode())){
                    values = dict.getFieldName();
                    return values;
                }
            }
        }

        //复选
        if("checkbox".equals(systemExtendBusinessParamValidator.getData().getShowType())){
            String[] params = filters.getParamValue().split(",");
            if(StringUtils.isEmpty(params) || params.length<0){
                return "";
            }

            for(int a=0;a<params.length;a++){
                for (int i =0;i<dictList.size();i++) {
                    Dict dict = dictList.get(i);
                    if (params[a].equals(dict.getFieldCode())) {
                        if(StringUtils.isEmpty(values)){
                            values = dict.getFieldName();
                        }else{
                            values += ","+dict.getFieldName();
                        }
                        break;
                    }
                }
            }
        }
        return values;
    }
}
