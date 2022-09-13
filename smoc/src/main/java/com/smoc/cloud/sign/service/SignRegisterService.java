package com.smoc.cloud.sign.service;

import com.smoc.cloud.common.smoc.customer.qo.ExcelRegisterImportData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.entity.AccountSignRegister;
import com.smoc.cloud.customer.repository.AccountSignRegisterRepository;
import com.smoc.cloud.customer.repository.BusinessAccountRepository;
import com.smoc.cloud.sequence.repository.SequenceRepository;
import com.smoc.cloud.sign.mode.SignChannel;
import com.smoc.cloud.sign.mode.SignRegister;
import com.smoc.cloud.template.entity.AccountTemplateInfo;
import com.smoc.cloud.template.repository.AccountTemplateInfoRepository;
import com.smoc.cloud.template.service.AccountTemplateInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 签名报备
 */
@Slf4j
@Service
public class SignRegisterService {

    @Autowired
    public DataSource dataSource;

    @Resource
    private SignRegisterRepository signRegisterRepository;

    @Resource
    private BusinessAccountRepository businessAccountRepository;

    @Resource
    private AccountSignRegisterRepository accountSignRegisterRepository;

    @Resource
    private SequenceRepository sequenceRepository;

    @Resource
    private AccountTemplateInfoService accountTemplateInfoService;

    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;

    @Async
    public void generateSignRegisterByRegisterId(String signRegisterId) {

        //获取签名报备信息
        Optional<AccountSignRegister> accountSignRegisterOptional = accountSignRegisterRepository.findById(signRegisterId);
        if (!accountSignRegisterOptional.isPresent()) {
            return;
        }
        AccountSignRegister accountSignRegister = accountSignRegisterOptional.get();

        //获取业务账号信息
        Optional<AccountBasicInfo> accountBasicInfoOptional = businessAccountRepository.findById(accountSignRegister.getAccount());
        if (!accountBasicInfoOptional.isPresent()) {
            return;
        }
        AccountBasicInfo accountBasicInfo = accountBasicInfoOptional.get();

        //获取账号配置通道信息
        List<SignChannel> channels = this.signRegisterRepository.findChannelByAccount(accountSignRegister.getAccount());
        if (null == channels || channels.size() < 1) {
            return;
        }

        //获取账号配置的路由通道信息
        List<SignChannel> routeChannels = this.signRegisterRepository.findRouteChannelByAccount(accountSignRegister.getAccount());
//        log.info("routeChannels:{}",new Gson().toJson(routeChannels));
        //获取账号配置的补发通道信息
        List<SignChannel> repairChannels = this.signRegisterRepository.findRepairChannelByAccount(accountSignRegister.getAccount());
//        log.info("repairChannels:{}",new Gson().toJson(repairChannels));
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            String deleteSql = "delete from account_sign_register_for_file where REGISTER_SIGN_ID='" + accountSignRegister.getId() + "' and REGISTER_STATUS='1'";
            stmt = conn.prepareStatement(deleteSql);
            stmt.execute();
            StringBuffer sql = new StringBuffer();
            sql.append("insert IGNORE into account_sign_register_for_file ");
            sql.append("(ID,REGISTER_SIGN_ID,ACCOUNT,CHANNEL_ID,CHANNEL_NAME,ACCESS_PROVINCE,ACCESS_CITY,REGISTER_ENTERPRISE,REGISTER_CARRIER,REGISTER_CODE_NUMBER,REGISTER_EXTEND_NUMBER,REGISTER_SIGN,NUMBER_SEGMENT,REGISTER_STATUS,CREATED_TIME,REGISTER_TYPE) ");
            sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?)");
            stmt = conn.prepareStatement(sql.toString());

            String signExtendNumber = accountSignRegister.getSignExtendNumber();
            String signExtendData = accountSignRegister.getExtendData();
            String[] extendNumbers = signExtendData.split(",");
            for (String extend : extendNumbers) {
                //主通道
                for (SignChannel signChannel : channels) {
                    if (StringUtils.isEmpty(signChannel.getSrcId())) {
                        continue;
                    }
                    stmt.setString(1, UUID.uuid32());
                    stmt.setString(2, accountSignRegister.getId());
                    stmt.setString(3, accountSignRegister.getAccount());
                    stmt.setString(4, signChannel.getChannelId());
                    stmt.setString(5, signChannel.getChannelName());
                    stmt.setString(6, signChannel.getAccessProvince());
                    stmt.setString(7, signChannel.getAccessCity());
                    stmt.setString(8, signChannel.getRegisterEnterprise());
                    stmt.setString(9, signChannel.getCarrier());
                    stmt.setString(10, signChannel.getSrcId());
                    stmt.setString(11, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                    stmt.setString(12, accountSignRegister.getSign());
                    stmt.setString(13, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                    stmt.setString(14, "1");
                    stmt.setString(15, "1");
                    stmt.addBatch();
                }

                //路由通道
                if (null != routeChannels && routeChannels.size() > 0) {
                    for (SignChannel signChannel : routeChannels) {
                        if (StringUtils.isEmpty(signChannel.getSrcId())) {
                            continue;
                        }
                        stmt.setString(1, UUID.uuid32());
                        stmt.setString(2, accountSignRegister.getId());
                        stmt.setString(3, accountSignRegister.getAccount());
                        stmt.setString(4, signChannel.getChannelId());
                        stmt.setString(5, signChannel.getChannelName());
                        stmt.setString(6, signChannel.getAccessProvince());
                        stmt.setString(7, signChannel.getAccessCity());
                        stmt.setString(8, signChannel.getRegisterEnterprise());
                        stmt.setString(9, signChannel.getCarrier());
                        stmt.setString(10, signChannel.getSrcId());
                        stmt.setString(11, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(12, accountSignRegister.getSign());
                        stmt.setString(13, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(14, "1");
                        stmt.setString(15, "1");
                        stmt.addBatch();
                    }
                }

                //补发通道
                if (null != repairChannels && repairChannels.size() > 0) {
                    for (SignChannel signChannel : repairChannels) {
                        if (StringUtils.isEmpty(signChannel.getSrcId())) {
                            continue;
                        }
//                        log.info("[channelId]:{}",signChannel.getChannelId());
                        stmt.setString(1, UUID.uuid32());
                        stmt.setString(2, accountSignRegister.getId());
                        stmt.setString(3, accountSignRegister.getAccount());
                        stmt.setString(4, signChannel.getChannelId());
                        stmt.setString(5, signChannel.getChannelName());
                        stmt.setString(6, signChannel.getAccessProvince());
                        stmt.setString(7, signChannel.getAccessCity());
                        stmt.setString(8, signChannel.getRegisterEnterprise());
                        stmt.setString(9, signChannel.getCarrier());
                        stmt.setString(10, signChannel.getSrcId());
                        stmt.setString(11, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(12, accountSignRegister.getSign());
                        stmt.setString(13, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(14, "1");
                        stmt.setString(15, "1");
                        stmt.addBatch();
                    }
                }
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (null != stmt) {
                    stmt.clearBatch();
                    stmt.clearParameters();
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Async
    public void generateSignRegisterByAccount(String account) {


        //获取业务账号信息
        Optional<AccountBasicInfo> accountBasicInfoOptional = businessAccountRepository.findById(account);
        if (!accountBasicInfoOptional.isPresent()) {
            return;
        }
        AccountBasicInfo accountBasicInfo = accountBasicInfoOptional.get();

        //获取签名报备信息
        List<SignRegister> signRegisters = this.signRegisterRepository.findSignRegisterByAccount(account);
        if (null == signRegisters || signRegisters.size() < 1) {
            return;
        }

        //获取账号配置通道信息
        List<SignChannel> channels = this.signRegisterRepository.findChannelByAccount(account);
        if (null == channels || channels.size() < 1) {
            return;
        }

        //获取账号配置的路由通道信息
        List<SignChannel> routeChannels = this.signRegisterRepository.findRouteChannelByAccount(account);

        //获取账号配置的补发通道信息
        List<SignChannel> repairChannels = this.signRegisterRepository.findRepairChannelByAccount(account);

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            String deleteSql = "delete from account_sign_register_for_file where ACCOUNT='" + account + "' and REGISTER_STATUS='1'";
            stmt = conn.prepareStatement(deleteSql);
            stmt.execute();
            StringBuffer sql = new StringBuffer();
            sql.append("insert IGNORE into account_sign_register_for_file ");
            sql.append("(ID,REGISTER_SIGN_ID,ACCOUNT,CHANNEL_ID,CHANNEL_NAME,ACCESS_PROVINCE,ACCESS_CITY,REGISTER_ENTERPRISE,REGISTER_CARRIER,REGISTER_CODE_NUMBER,REGISTER_EXTEND_NUMBER,REGISTER_SIGN,NUMBER_SEGMENT,REGISTER_STATUS,CREATED_TIME,REGISTER_TYPE) ");
            sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?)");
            stmt = conn.prepareStatement(sql.toString());

            for (SignRegister signRegister : signRegisters) {
                String signExtendNumber = signRegister.getSignExtendNumber();
                String signExtendData = signRegister.getExtendData();
                String[] extendNumbers = signExtendData.split(",");
                for (String extend : extendNumbers) {

                    //主通道
                    for (SignChannel signChannel : channels) {
                        if (StringUtils.isEmpty(signChannel.getSrcId())) {
                            continue;
                        }
                        stmt.setString(1, UUID.uuid32());
                        stmt.setString(2, signRegister.getId());
                        stmt.setString(3, signRegister.getAccount());
                        stmt.setString(4, signChannel.getChannelId());
                        stmt.setString(5, signChannel.getChannelName());
                        stmt.setString(6, signChannel.getAccessProvince());
                        stmt.setString(7, signChannel.getAccessCity());
                        stmt.setString(8, signChannel.getRegisterEnterprise());
                        stmt.setString(9, signChannel.getCarrier());
                        stmt.setString(10, signChannel.getSrcId());
                        stmt.setString(11, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(12, signRegister.getSign());
                        stmt.setString(13, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(14, "1");
                        stmt.setString(15, "1");
                        stmt.addBatch();
                    }

                    //路由通道
                    if (null != routeChannels && routeChannels.size() > 0) {
                        for (SignChannel signChannel : routeChannels) {
                            if (StringUtils.isEmpty(signChannel.getSrcId())) {
                                continue;
                            }
                            stmt.setString(1, UUID.uuid32());
                            stmt.setString(2, signRegister.getId());
                            stmt.setString(3, signRegister.getAccount());
                            stmt.setString(4, signChannel.getChannelId());
                            stmt.setString(5, signChannel.getChannelName());
                            stmt.setString(6, signChannel.getAccessProvince());
                            stmt.setString(7, signChannel.getAccessCity());
                            stmt.setString(8, signChannel.getRegisterEnterprise());
                            stmt.setString(9, signChannel.getCarrier());
                            stmt.setString(10, signChannel.getSrcId());
                            stmt.setString(11, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                            stmt.setString(12, signRegister.getSign());
                            stmt.setString(13, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                            stmt.setString(14, "1");
                            stmt.setString(15, "1");
                            stmt.addBatch();
                        }
                    }

                    //补发通道
                    if (null != repairChannels && repairChannels.size() > 0) {
                        for (SignChannel signChannel : repairChannels) {
                            if (StringUtils.isEmpty(signChannel.getSrcId())) {
                                continue;
                            }
                            stmt.setString(1, UUID.uuid32());
                            stmt.setString(2, signRegister.getId());
                            stmt.setString(3, signRegister.getAccount());
                            stmt.setString(4, signChannel.getChannelId());
                            stmt.setString(5, signChannel.getChannelName());
                            stmt.setString(6, signChannel.getAccessProvince());
                            stmt.setString(7, signChannel.getAccessCity());
                            stmt.setString(8, signChannel.getRegisterEnterprise());
                            stmt.setString(9, signChannel.getCarrier());
                            stmt.setString(10, signChannel.getSrcId());
                            stmt.setString(11, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                            stmt.setString(12, signRegister.getSign());
                            stmt.setString(13, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                            stmt.setString(14, "1");
                            stmt.setString(15, "1");
                            stmt.addBatch();
                        }
                    }
                }
            }


            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (null != stmt) {
                    stmt.clearBatch();
                    stmt.clearParameters();
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 报备数据导入
     *
     * @param importList
     */
    public void importExcel(List<ExcelRegisterImportData> importList) {

        //log.info("importList:{}",new Gson().toJson(importList));

        if (null == importList || importList.size() < 1) {
            return;
        }

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            //Set<String> accounts = new HashSet<>();
            //Set<String> signs = new HashSet<>();
            List<AccountTemplateInfo> list = new ArrayList<>();
            for (ExcelRegisterImportData importData : importList) {

                if(StringUtils.isEmpty(importData.getAccount())){
                    continue;
                }

                //签名扩展号
                String signExtendNumber = sequenceRepository.findSequence(importData.getAccount()) + "";
                if (StringUtils.isEmpty(signExtendNumber)) {
                    continue;
                }

                //accounts.add(importData.getAccount().trim());
                //signs.add(importData.getSign().trim());
                AccountTemplateInfo info = new AccountTemplateInfo();
                info.setBusinessAccount(importData.getAccount().trim());
                info.setSignName(importData.getSign().trim());
                list.add(info);
                //String deleteSql = "delete from enterprise_sign_certify where SOCIAL_CREDIT_CODE='" + importData.getSocialCreditCode() + "' ";
                //stmt.addBatch(deleteSql);

                String authStartDate = DateTimeUtils.getDateFormat(new Date());
                String authEndDate = DateTimeUtils.getDateFormat(DateTimeUtils.dateAddYears(new Date(), 5));
                String rootPath = "";//"/share/resourceFile/certify/import/";
                StringBuffer insertCertify = new StringBuffer("insert IGNORE into enterprise_sign_certify(ID,REGISTER_ENTERPRISE_ID,REGISTER_ENTERPRISE_NAME,SOCIAL_CREDIT_CODE,BUSINESS_LICENSE,PERSON_LIABLE_NAME,PERSON_LIABLE_CERTIFICATE_TYPE,PERSON_LIABLE_CERTIFICATE_NUMBER,PERSON_LIABLE_CERTIFICATE_URL,PERSON_HANDLED_NAME,PERSON_HANDLED_CERTIFICATE_TYPE,PERSON_HANDLED_CERTIFICATE_NUMBER,PERSON_HANDLED_CERTIFICATE_URL,AUTHORIZE_START_DATE,AUTHORIZE_EXPIRE_DATE,OFFICE_PHOTOS,CERTIFY_STATUS,CREATED_BY,CREATED_TIME)");
                insertCertify.append(" values('" + importData.getSocialCreditCode().trim() + "','" + UUID.uuid32() + "','" + importData.getRegisterEnterpriseName().trim() + "','" + importData.getSocialCreditCode().trim() + "','" + rootPath + importData.getBusinessLicense().trim() + "','" + importData.getPersonLiableName().trim() + "','居民身份证','" + importData.getPersonLiableCertificateNumber().trim() + "','" + rootPath + importData.getPersonLiableCertificateUrl().trim() + "','" + importData.getPersonHandledName().trim() + "','居民身份证','" + importData.getPersonHandledCertificateNumber().trim() + "','" + rootPath + importData.getPersonHandledCertificateUrl().trim() + "','" + authStartDate + "','" + authEndDate + "','" + rootPath + importData.getOfficePhotos().trim() + "','1','系统导入',now())");
                //log.info("insertCertify:{}",insertCertify);
                stmt.addBatch(insertCertify.toString());
                String serviceType = "通知提醒";
                String mainApplication = "账号注册,账号登录,广告促销,通知提醒,公共服务";
                StringBuffer insertSign = new StringBuffer("insert IGNORE into account_sign_register(ID,ACCOUNT,SIGN,SIGN_EXTEND_NUMBER,EXTEND_TYPE,EXTEND_DATA,ENTERPRISE_ID,APP_NAME,SERVICE_TYPE,MAIN_APPLICATION,REGISTER_STATUS,SIGN_REGISTER_STATUS,KEY_VALUE_UPDATE_STATUS,CREATED_BY,CREATED_TIME)");
                insertSign.append(" values('" + UUID.uuid32() + "','" + importData.getAccount().trim() + "','" + importData.getSign().trim() + "','" + signExtendNumber + "','1','01','" + importData.getSocialCreditCode().trim() + "','" + importData.getSign().trim() + "','" + serviceType + "','" + mainApplication + "','1','0','0','系统导入',now())");
                //log.info("insertSign:{}",insertSign);
                stmt.addBatch(insertSign.toString());
            }
            stmt.executeBatch();
            conn.commit();

            batchGenerateSignRegisterByAccount(list);

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (null != stmt) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据账号批量生成报备数据
     *
     * @param list
     */
    public void batchGenerateSignRegisterByAccount(List<AccountTemplateInfo> list) {

        /*for (String account : accounts) {
            this.generateSignRegisterByAccount(account);

            //CMPP签名模版，重新加载模板放到reids
            this.accountTemplateInfoService.loadCMPPSignTemplate(account);

        }
*/
        for(AccountTemplateInfo info :list){
            this.generateSignRegisterByAccount(info.getBusinessAccount());

            this.addAccountTemplateInfo(info.getBusinessAccount(),info.getSignName());

            //CMPP签名模版，重新加载模板放到reids
            this.accountTemplateInfoService.loadCMPPSignTemplate(info.getBusinessAccount());
        }
    }

    /**
     * 根据账号，处理签名模板
     */
    private void addAccountTemplateInfo(String account,String sign) {

        List<AccountTemplateInfo> templateInfos = this.accountTemplateInfoRepository.findByBusinessAccountAndTemplateClassify(account, "3");
        //如果不存在，则创建签名模板
        if (null == templateInfos || templateInfos.size() < 1) {
            accountTemplateInfoRepository.createTemplate(account, "【" + sign + "】");
        } else {
            //如果存在，则修改签名模板内容
            AccountTemplateInfo accountTemplateInfo = templateInfos.get(0);
            String templateContent = accountTemplateInfo.getTemplateContent();
            Pattern pattern = Pattern.compile(templateContent);
            String message =  "【" + sign + "】";
            Matcher isMatch = pattern.matcher(message);
            if (isMatch.find()) {
                return;
            }

            templateContent = templateContent + "|" + "【" + sign + "】";

            accountTemplateInfo.setTemplateContent(templateContent);
            accountTemplateInfoRepository.saveAndFlush(accountTemplateInfo);
        }
    }

}
