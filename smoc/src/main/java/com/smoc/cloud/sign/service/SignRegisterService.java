package com.smoc.cloud.sign.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.entity.AccountSignRegister;
import com.smoc.cloud.customer.repository.AccountSignRegisterRepository;
import com.smoc.cloud.customer.repository.BusinessAccountRepository;
import com.smoc.cloud.sign.mode.SignChannel;
import com.smoc.cloud.sign.mode.SignRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

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
        if(null == channels || channels.size()<1){
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

            String deleteSql = "delete from account_sign_register_for_file where REGISTER_SIGN_ID='"+accountSignRegister.getId()+"' and REGISTER_STATUS='1'";
            stmt = conn.prepareStatement(deleteSql);
            stmt.execute();
            StringBuffer sql = new StringBuffer();
            sql.append("insert IGNORE into account_sign_register_for_file ");
            sql.append("(ID,REGISTER_SIGN_ID,ACCOUNT,CHANNEL_ID,CHANNEL_NAME,ACCESS_PROVINCE,REGISTER_CARRIER,REGISTER_CODE_NUMBER,REGISTER_EXTEND_NUMBER,REGISTER_SIGN,NUMBER_SEGMENT,REGISTER_STATUS,CREATED_TIME,REGISTER_TYPE) ");
            sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,now(),?)");
            stmt = conn.prepareStatement(sql.toString());

            String signExtendNumber = accountSignRegister.getSignExtendNumber();
            String signExtendData = accountSignRegister.getExtendData();
            String[] extendNumbers = signExtendData.split(",");
            for(String extend:extendNumbers){
                 //主通道
                 for(SignChannel signChannel:channels){
                     if(StringUtils.isEmpty(signChannel.getSrcId())){
                         continue;
                     }
                     stmt.setString(1, UUID.uuid32());
                     stmt.setString(2, accountSignRegister.getId());
                     stmt.setString(3, accountSignRegister.getAccount());
                     stmt.setString(4, signChannel.getChannelId());
                     stmt.setString(5, signChannel.getChannelName());
                     stmt.setString(6, signChannel.getAccessProvince());
                     stmt.setString(7, signChannel.getCarrier());
                     stmt.setString(8, signChannel.getSrcId());
                     stmt.setString(9, accountBasicInfo.getExtendNumber()+signExtendNumber+extend);
                     stmt.setString(10, accountSignRegister.getSign());
                     stmt.setString(11, signChannel.getSrcId()+accountBasicInfo.getExtendNumber()+signExtendNumber+extend);
                     stmt.setString(12, "1");
                     stmt.setString(13, "1");
                     stmt.addBatch();
                 }

                //路由通道
                if(null != routeChannels && routeChannels.size()>0){
                    for(SignChannel signChannel:routeChannels){
                        if (StringUtils.isEmpty(signChannel.getSrcId())) {
                            continue;
                        }
                        stmt.setString(1, UUID.uuid32());
                        stmt.setString(2, accountSignRegister.getId());
                        stmt.setString(3, accountSignRegister.getAccount());
                        stmt.setString(4, signChannel.getChannelId());
                        stmt.setString(5, signChannel.getChannelName());
                        stmt.setString(6, signChannel.getAccessProvince());
                        stmt.setString(7, signChannel.getCarrier());
                        stmt.setString(8, signChannel.getSrcId());
                        stmt.setString(9, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(10, accountSignRegister.getSign());
                        stmt.setString(11, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(12, "1");
                        stmt.setString(13, "2");
                        stmt.addBatch();
                    }
                }

                //补发通道
                if(null != repairChannels && repairChannels.size()>0){
                    for(SignChannel signChannel:repairChannels){
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
                        stmt.setString(7, signChannel.getCarrier());
                        stmt.setString(8, signChannel.getSrcId());
                        stmt.setString(9, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(10, accountSignRegister.getSign());
                        stmt.setString(11, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(12, "1");
                        stmt.setString(13, "3");
                        stmt.addBatch();
                    }
                }
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
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
    public void generateSignRegisterByAccount(String account){


        //获取业务账号信息
        Optional<AccountBasicInfo> accountBasicInfoOptional = businessAccountRepository.findById(account);
        if (!accountBasicInfoOptional.isPresent()) {
            return;
        }
        AccountBasicInfo accountBasicInfo = accountBasicInfoOptional.get();

        //获取签名报备信息
        List<SignRegister> signRegisters = this.signRegisterRepository.findSignRegisterByAccount(account);
        if(null == signRegisters || signRegisters.size()<1){
            return;
        }

        //获取账号配置通道信息
        List<SignChannel> channels = this.signRegisterRepository.findChannelByAccount(account);
        if(null == channels || channels.size()<1){
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
            String deleteSql = "delete from account_sign_register_for_file where ACCOUNT='"+account+"' and REGISTER_STATUS='1'";
            stmt = conn.prepareStatement(deleteSql);
            stmt.execute();
            StringBuffer sql = new StringBuffer();
            sql.append("insert IGNORE into account_sign_register_for_file ");
            sql.append("(ID,REGISTER_SIGN_ID,ACCOUNT,CHANNEL_ID,CHANNEL_NAME,ACCESS_PROVINCE,REGISTER_CARRIER,REGISTER_CODE_NUMBER,REGISTER_EXTEND_NUMBER,REGISTER_SIGN,NUMBER_SEGMENT,REGISTER_STATUS,CREATED_TIME,REGISTER_TYPE) ");
            sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,now(),?)");
            stmt = conn.prepareStatement(sql.toString());

            for(SignRegister signRegister:signRegisters) {
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
                        stmt.setString(7, signChannel.getCarrier());
                        stmt.setString(8, signChannel.getSrcId());
                        stmt.setString(9, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(10, signRegister.getSign());
                        stmt.setString(11, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                        stmt.setString(12, "1");
                        stmt.setString(13, "1");
                        stmt.addBatch();
                    }

                    //路由通道
                    if(null != routeChannels && routeChannels.size()>0){
                        for(SignChannel signChannel:routeChannels){
                            if (StringUtils.isEmpty(signChannel.getSrcId())) {
                                continue;
                            }
                            stmt.setString(1, UUID.uuid32());
                            stmt.setString(2, signRegister.getId());
                            stmt.setString(3, signRegister.getAccount());
                            stmt.setString(4, signChannel.getChannelId());
                            stmt.setString(5, signChannel.getChannelName());
                            stmt.setString(6, signChannel.getAccessProvince());
                            stmt.setString(7, signChannel.getCarrier());
                            stmt.setString(8, signChannel.getSrcId());
                            stmt.setString(9, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                            stmt.setString(10, signRegister.getSign());
                            stmt.setString(11, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                            stmt.setString(12, "1");
                            stmt.setString(13, "2");
                            stmt.addBatch();
                        }
                    }

                    //补发通道
                    if(null != repairChannels && repairChannels.size()>0){
                        for(SignChannel signChannel:repairChannels){
                            if (StringUtils.isEmpty(signChannel.getSrcId())) {
                                continue;
                            }
                            stmt.setString(1, UUID.uuid32());
                            stmt.setString(2, signRegister.getId());
                            stmt.setString(3, signRegister.getAccount());
                            stmt.setString(4, signChannel.getChannelId());
                            stmt.setString(5, signChannel.getChannelName());
                            stmt.setString(6, signChannel.getAccessProvince());
                            stmt.setString(7, signChannel.getCarrier());
                            stmt.setString(8, signChannel.getSrcId());
                            stmt.setString(9, accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                            stmt.setString(10, signRegister.getSign());
                            stmt.setString(11, signChannel.getSrcId() + accountBasicInfo.getExtendNumber() + signExtendNumber + extend);
                            stmt.setString(12, "1");
                            stmt.setString(13, "3");
                            stmt.addBatch();
                        }
                    }
                }
            }


            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
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
}
