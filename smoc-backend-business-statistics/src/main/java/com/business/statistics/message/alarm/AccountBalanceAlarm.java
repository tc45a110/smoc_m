package com.business.statistics.message.alarm;


import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.AccountInfoManager;
import com.base.common.manager.AlarmManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.vo.AlarmMessage;
import com.base.common.vo.BalanceAlarm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


public class AccountBalanceAlarm {
    private static final Logger logger = LoggerFactory.getLogger(AccountBalanceAlarm.class);
    private static final String ACCOUNT_CACHE = "accountCache";

    public static void main(String[] args) {
        logger.info("***************账号余额告警开始***************");
        long start = System.currentTimeMillis();
        try{
            List<AlarmMessage> alarmMessageList = new ArrayList<>();
            Map<String, BalanceAlarm> accountBalanceAlarmMap = load();
            for(String accountID : accountBalanceAlarmMap.keySet()){
                double accountAvailableAmount = AccountInfoManager.getInstance().getAccountAvailableAmount(accountID);
                //AccountInfoManager.getInstance().getAcc
                BalanceAlarm balanceAlarm = accountBalanceAlarmMap.get(accountID);
                Date nowDate = new Date();
                //达到告警金额
                if(accountAvailableAmount < balanceAlarm.getAlarmAmount()){
                    //获取上次
                    int intervalTime = DateUtil.getMinuteIntervalTime(balanceAlarm.getLastAlarmDate(), nowDate);
                    logger.info("上次告警的间隔时间"+intervalTime);
                    if(intervalTime % balanceAlarm.getAlarmFrequency() == 0 && balanceAlarm.getAlreadyAlarmNumber() < balanceAlarm.getAlarmNumber()){
                        String sendText = new StringBuilder("账号:").append(accountID).append(",当前可用余额为:")
                                .append(accountAvailableAmount).append(",余额已不足,请及时充值!").toString();
                        //AlarmManager.getInstance().process(new AlarmMessage(AlarmMessage.AlarmKey.AccountFinance, sendText));
                        alarmMessageList.add(new AlarmMessage(AlarmMessage.AlarmKey.AccountFinance, sendText));
                        balanceAlarm.setAlreadyAlarmNumber(balanceAlarm.getAlreadyAlarmNumber()+1);
                        balanceAlarm.setLastAlarmDate(nowDate);
                        logger.info("余额告警{}accountID={}{}sendText={}{}balanceAlarm={}", FixedConstant.SPLICER,accountID,FixedConstant.SPLICER,sendText,FixedConstant.SPLICER,balanceAlarm);
                        CacheBaseService.saveBalanceAlarmToMiddlewareCache(balanceAlarm);
                    }else{
                        logger.info("未达到余额告警要求{}accountID={}{}balanceAlarm={}", FixedConstant.SPLICER,accountID,FixedConstant.SPLICER,balanceAlarm);
                    }
                }else{
                    //没有达到告警金额
                    BalanceAlarm alarmCache = CacheBaseService.getBalanceAlarmToMiddlewareCache(accountID);
                    if(alarmCache != null){
                        //账号已充值或者告警金额已调整，删除redis里面的告警信息
                        CacheBaseService.deleteBalanceAlarmToMiddlewareCache(accountID);
                        logger.info("账号{}已充值或者告警金额已调整",accountID);
                    }
                }
            }
            if(alarmMessageList.size() > 0){
                long saveStartTime = System.currentTimeMillis();
                saveAlarmValue(alarmMessageList);
                logger.info("保存账号余额告警条数:{},耗时:{}毫秒",alarmMessageList.size(),(System.currentTimeMillis() - saveStartTime));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        logger.info("账号余额告警结束,耗时:{}毫秒",(System.currentTimeMillis() - start));
        logger.info("***************账号余额告警结束***************");
        System.exit(0);
    }

    /**
     * 加载告警参数
     * @return
     */
    private static Map<String, BalanceAlarm> load() {
        Map<String,BalanceAlarm> resultMap = new HashMap<>();
        String accountIDStr = ResourceManager.getInstance().getValue("alarm.amount.accountIds");
        if(StringUtils.isEmpty(accountIDStr)){

            return null;
        }

        String [] accountIDs = accountIDStr.split(",");
        logger.info("{}本次需要进行余额监控的账号{}accountIDs={}",DateUtil.getCurDateTime(),FixedConstant.SPLICER,StringUtils.join(accountIDs,","));
        for (int i = 0; i < accountIDs.length; i++) {
            String accountID = accountIDs[i];
            String alarmAmount = ResourceManager.getInstance().getValue(accountID + ".alarm.amount");
            int alarmFrequency = ResourceManager.getInstance().getIntValue(accountID + ".alarm.frequency");
            int alarmNumber = ResourceManager.getInstance().getIntValue(accountID + ".alarm.number");

            if (StringUtils.isEmpty(alarmAmount) || alarmNumber == 0){
                continue;
            }

            BalanceAlarm balanceAlarm = CacheBaseService.getBalanceAlarmToMiddlewareCache(accountID);
            if(balanceAlarm != null){
                balanceAlarm.setAlarmAmount(Double.valueOf(alarmAmount));
                balanceAlarm.setAlarmFrequency(alarmFrequency);
                balanceAlarm.setAlarmNumber(alarmNumber);
            }else{
                balanceAlarm = new BalanceAlarm(accountID,Double.valueOf(alarmAmount),alarmFrequency,alarmNumber);
                balanceAlarm.setAlreadyAlarmNumber(0);
                balanceAlarm.setLastAlarmDate(new Date());
            }
            resultMap.put(accountID,balanceAlarm);
        }

        String accountBalanceAlarmCache = CacheBaseService.getAccountBalanceAlarmToMiddlewareCache(ACCOUNT_CACHE);
        if(StringUtils.isNotEmpty(accountBalanceAlarmCache)){
            String [] accountBalanceAlarmCacheArr = accountBalanceAlarmCache.split(FixedConstant.SPLICER);
            logger.info("上次需要进行余额监控的账号{}accountIDs={}",FixedConstant.SPLICER,StringUtils.join(accountBalanceAlarmCacheArr,","));
            for (int i = 0; i < accountBalanceAlarmCacheArr.length; i++) {
                String account = accountBalanceAlarmCacheArr[i];
                if(!resultMap.containsKey(account)){
                    //删除已取消告警的账号
                    CacheBaseService.deleteBalanceAlarmToMiddlewareCache(account);
                    logger.info("删除已取消告警监控的账号{}accountID={}",FixedConstant.SPLICER,account);
                }
            }
        }
        //保存本次需要告警的账号
        CacheBaseService.saveAccountBalanceAlarmToMiddlewareCache(ACCOUNT_CACHE,StringUtils.join(accountIDs,FixedConstant.SPLICER));
        return resultMap;
    }

    private static void saveAlarmValue(List<AlarmMessage> alarmMessageList) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO smoc_route.alarm_message_info");
        sql.append(" (ALARM_KEY,ALARM_VALUE,CREATED_TIME) ");
        sql.append("values(?,?,NOW())");

        // 在一个事务中更新数据
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql.toString());

            for (AlarmMessage alarmMessage : alarmMessageList) {
                pstmt.setString(1, alarmMessage.getAlarmKey().name());
                pstmt.setString(2, alarmMessage.getAlarmValue());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            try {
                conn.rollback();
            } catch (Exception e1) {
                logger.error(e.getMessage(), e1);
            }
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
        }

    }

//    private static Map<String, Object> loadAccountFinanceMonitor() {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select * from smoc.*");
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        Map<String, Object> resultMap = new HashMap<>();
//        try {
//            conn = LavenderDBSingleton.getInstance().getConnection();
//            pstmt = conn.prepareStatement(sql.toString());
//            pstmt.setString(1, "");
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//
//            }
//            return resultMap;
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        } finally {
//            LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
//        }
//        return null;
//    }
}
