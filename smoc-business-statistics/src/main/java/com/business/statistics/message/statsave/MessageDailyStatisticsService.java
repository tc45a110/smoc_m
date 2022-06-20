package com.business.statistics.message.statsave;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.constant.LogPathConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.util.UUIDUtil;
import com.business.statistics.message.pojo.MessageDailyStatistics;
import com.business.statistics.util.FileFilterUtil;

/**
 * 	统计日消息数据:通过business-access的业务日志mt和business-proxy的业务日志mt和mr,入库前一天的客户提交数、提交成功数、提交失败数、状态报告成功数和状态报告失败数
 */
public class MessageDailyStatisticsService {

	private static final Logger logger = LoggerFactory.getLogger(MessageDailyStatisticsService.class);
	
	private static final int BUSINESS_ACCESS_MT_MESSAGE_LOG_LENGTH = ResourceManager.getInstance().getIntValue("business.access.mt.message.log.length");
	
	private static final int BUSINESS_RROXY_MT_MESSAGE_LOG_LENGTH = ResourceManager.getInstance().getIntValue("business.proxy.mt.message.log.length");
	
	private static final int BUSINESS_PROXY_MR_MESSAGE_LOG_LENGTH = ResourceManager.getInstance().getIntValue("business.proxy.mr.message.log.length");
	
	private static Map<String,MessageDailyStatistics> messageDailyStatisticsMap = new HashMap<String, MessageDailyStatistics>();

	public static void main(String[] args) {
		logger.info(Arrays.toString(args));
		try {
			int afterDay = -1;
			if (!(args.length == 1 || args.length == 0)) {
				logger.error("参数不符合规范");
				System.exit(0);
			}
			//通过传参 确定读取时间
			if (args.length >= 1) {
				afterDay = Integer.valueOf(args[0]);
			}
			String loadTime = DateUtil.getAfterDayDateTime(afterDay, DateUtil.DATE_FORMAT_COMPACT_DAY);
			loadAccessBusinessMT(loadTime);
			loadProxyBusinessMT(loadTime);
			loadProxyBusinessMR(loadTime);
			saveMessageDailyStatistics();
		
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		//保证所有数据处理完毕 关闭进程
		logger.info("程序正常退出");
		System.exit(0);
	}

	private static void loadAccessBusinessMT(String loadTime) {
		//读取的文件路径
		String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
				.append(LogPathConstant.getAccessBusinessFilePathPart(FixedConstant.RouteLable.MT.name()))
				.toString();
		//读取的文件名
		String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MT.name()))
				.append(loadTime).toString();
		//过滤文件
		File[] fileList = FileFilterUtil.findFile(filePath, fileName);
		
		if (fileList == null || fileList.length == 0) {
			logger.info("{},无短信状态报告文件,日期=",DateUtil.getCurDateTime(),loadTime);
		}else{
			logger.info("{},短信下发文件数量={},日期={}",DateUtil.getCurDateTime(),fileList.length,loadTime);
		}
	
		String line = null;
		String[] array = null;
		String signature = null;
		int count = 0;
		for (File file : fileList) {
			try {
				LineIterator lines = FileUtils.lineIterator(file);
				while (lines.hasNext()) {
					count++;
					line = lines.next();
					array = line.split(FixedConstant.LOG_SEPARATOR);
					if(array.length < BUSINESS_ACCESS_MT_MESSAGE_LOG_LENGTH){
						continue;
					}
					
					signature = array[17];
					if(signature!= null && signature.length() > 30){
						signature = signature.substring(0, 30);
					}

					MessageDailyStatistics newMessageDailyStatistics = new MessageDailyStatistics();
					newMessageDailyStatistics.setEnterpriseFlag(array[2]);
					newMessageDailyStatistics.setBusinessAccount(array[1]);
					
					newMessageDailyStatistics.setChannelID(array[23]);
					newMessageDailyStatistics.setAreaCode(array[9]);
					newMessageDailyStatistics.setPriceAreaCode(array[32]);
					newMessageDailyStatistics.setCarrier(array[8]);
					newMessageDailyStatistics.setBusinessType(array[28]);
					newMessageDailyStatistics.setInfoType(array[19]);
					newMessageDailyStatistics.setMessageSign(signature);
					newMessageDailyStatistics.setMessageDate(array[4].substring(0, 10));
					String key = getKey(newMessageDailyStatistics);
					MessageDailyStatistics messageDailyStatistics = messageDailyStatisticsMap.get(key);
					if(messageDailyStatistics == null) {
						messageDailyStatistics = new MessageDailyStatistics(newMessageDailyStatistics);
						messageDailyStatisticsMap.put(key, messageDailyStatistics);
					}
					messageDailyStatistics.setCustomerSubmitNum(Integer.valueOf(array[18]) + messageDailyStatistics.getCustomerSubmitNum());
				}

			} catch (Exception e) {
				logger.error("{},读取短信状态报告日志错误:file={}",DateUtil.getCurDateTime(),file.getAbsolutePath());
				logger.error(e.getMessage(),e);
			}
		}
		logger.info("{},总loadAccessBusinessMT记录数:{}",DateUtil.getCurDateTime(),count);
	}
	
	private static void loadProxyBusinessMT(String loadTime) {
		//读取的文件路径
		String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
				.append(LogPathConstant.getProxyBusinessFilePathPart(FixedConstant.RouteLable.MT.name()))
				.toString();
		//读取的文件名
		String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MT.name()))
				.append(loadTime).toString();
		//过滤文件
		File[] fileList = FileFilterUtil.findFile(filePath, fileName);
		
		if (fileList == null || fileList.length == 0) {
			logger.info("{},无短信状态报告文件,日期=",DateUtil.getCurDateTime(),loadTime);
		}else{
			logger.info("{},短信下发文件数量={},日期={}",DateUtil.getCurDateTime(),fileList.length,loadTime);
		}
	
		String line = null;
		String[] array = null;
		String signature = null;
		String successFlag = null;
		int count = 0;
		for (File file : fileList) {
			try {
				LineIterator lines = FileUtils.lineIterator(file);
				while (lines.hasNext()) {
					count++;
					line = lines.next();
					array = line.split(FixedConstant.LOG_SEPARATOR);
					if(array.length < BUSINESS_RROXY_MT_MESSAGE_LOG_LENGTH){
						continue;
					}
					
					signature = array[22];
					if(signature!= null && signature.length() > 30){
						signature = signature.substring(0, 30);
					}

					MessageDailyStatistics newMessageDailyStatistics = new MessageDailyStatistics();
					newMessageDailyStatistics.setEnterpriseFlag(array[2]);
					newMessageDailyStatistics.setBusinessAccount(array[1]);
					newMessageDailyStatistics.setChannelID(array[27]);
					newMessageDailyStatistics.setAreaCode(array[13]);
					newMessageDailyStatistics.setPriceAreaCode(array[36]);
					newMessageDailyStatistics.setCarrier(array[12]);
					newMessageDailyStatistics.setBusinessType(array[41]);
					newMessageDailyStatistics.setInfoType(array[23]);
					newMessageDailyStatistics.setMessageSign(signature);
					newMessageDailyStatistics.setMessageDate(array[4].substring(0, 10));
					successFlag = array[32];
					String key = getKey(newMessageDailyStatistics);
					MessageDailyStatistics messageDailyStatistics = messageDailyStatisticsMap.get(key);
					if(messageDailyStatistics == null) {
						messageDailyStatistics = new MessageDailyStatistics(newMessageDailyStatistics);
						messageDailyStatisticsMap.put(key, messageDailyStatistics);
					}
					int successSubmitNum = Integer.parseInt(array[31]);
					if(InsideStatusCodeConstant.SUCCESS_CODE.equals(successFlag)) {
						messageDailyStatistics.setSuccessSubmitNum(successSubmitNum + messageDailyStatistics.getSuccessSubmitNum());
					}else {
						messageDailyStatistics.setFailureSubmitNum(successSubmitNum + messageDailyStatistics.getFailureSubmitNum());
					}
				}

			} catch (Exception e) {
				logger.error("{},读取短信状态报告日志错误:file={}",DateUtil.getCurDateTime(),file.getAbsolutePath());
				logger.error(e.getMessage(),e);
			}
		}
		logger.info("{},总loadProxyBusinessMT记录数:{}",DateUtil.getCurDateTime(),count);
	}
	
	private static void loadProxyBusinessMR(String loadTime) {
		// 读取的文件路径
		String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
				.append(LogPathConstant.getProxyBusinessFilePathPart(FixedConstant.RouteLable.MR.name())).toString();
		// 读取的文件名
		String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MR.name()))
				.append(loadTime).toString();
		// 过滤文件
		File[] fileList = FileFilterUtil.findFile(filePath, fileName);

		if (fileList == null || fileList.length == 0) {
			logger.info("{},无短信状态报告文件,日期=", DateUtil.getCurDateTime(), loadTime);
		} else {
			logger.info("{},短信下发文件数量={},日期={}", DateUtil.getCurDateTime(), fileList.length, loadTime);
		}

		String line = null;
		String[] array = null;
		String signature = null;
		String successFlag = null;
		int count = 0;
		for (File file : fileList) {
			try {
				LineIterator lines = FileUtils.lineIterator(file);
				while (lines.hasNext()) {
					count++;
					line = lines.next();
					array = line.split(FixedConstant.LOG_SEPARATOR);
					if (array.length < BUSINESS_PROXY_MR_MESSAGE_LOG_LENGTH) {
						continue;
					}

					signature = array[22];
					if (signature != null && signature.length() > 30) {
						signature = signature.substring(0, 30);
					}

					MessageDailyStatistics newMessageDailyStatistics = new MessageDailyStatistics();
					newMessageDailyStatistics.setEnterpriseFlag(array[2]);
					newMessageDailyStatistics.setBusinessAccount(array[1]);
					newMessageDailyStatistics.setChannelID(array[26]);
					newMessageDailyStatistics.setAreaCode(array[13]);
					newMessageDailyStatistics.setPriceAreaCode(array[37]);
					newMessageDailyStatistics.setCarrier(array[12]);
					newMessageDailyStatistics.setBusinessType(array[42]);
					newMessageDailyStatistics.setInfoType(array[23]);
					newMessageDailyStatistics.setMessageSign(signature);
					newMessageDailyStatistics.setMessageDate(array[4].substring(0, 10));
					successFlag = array[28];
					String key = getKey(newMessageDailyStatistics);
					MessageDailyStatistics messageDailyStatistics = messageDailyStatisticsMap.get(key);
					if (messageDailyStatistics == null) {
						messageDailyStatistics = new MessageDailyStatistics(newMessageDailyStatistics);
						messageDailyStatisticsMap.put(key, messageDailyStatistics);
					}
					
					if (InsideStatusCodeConstant.SUCCESS_CODE.equals(successFlag)) {
						messageDailyStatistics.setMessageSuccessNum(1 + messageDailyStatistics.getMessageSuccessNum());
					}else {
						messageDailyStatistics.setMessageFailureNum(1 + messageDailyStatistics.getMessageFailureNum());
					}
				}

			} catch (Exception e) {
				logger.error("{},读取短信状态报告日志错误:file={}", DateUtil.getCurDateTime(), file.getAbsolutePath());
				logger.error(e.getMessage(), e);
			}
		}
		logger.info("{},总loadProxyBusinessMR记录数:{}", DateUtil.getCurDateTime(), count);
	}
	
	private static String getKey(MessageDailyStatistics messageDailyStatistics) {
		return new StringBuilder()
							.append(messageDailyStatistics.getEnterpriseFlag()).append(FixedConstant.SPLICER)
							.append(messageDailyStatistics.getBusinessAccount()).append(FixedConstant.SPLICER)
							.append(messageDailyStatistics.getChannelID()).append(FixedConstant.SPLICER)
							.append(messageDailyStatistics.getAreaCode()).append(FixedConstant.SPLICER)
							.append(messageDailyStatistics.getPriceAreaCode()).append(FixedConstant.SPLICER)
							.append(messageDailyStatistics.getCarrier()).append(FixedConstant.SPLICER)
							.append(messageDailyStatistics.getBusinessType()).append(FixedConstant.SPLICER)
							.append(messageDailyStatistics.getInfoType()).append(FixedConstant.SPLICER)
							.append(messageDailyStatistics.getMessageSign()).append(FixedConstant.SPLICER)
							.append(messageDailyStatistics.getMessageDate()).toString();
	}
	
	
	private static void saveMessageDailyStatistics() {
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc.message_daily_statistics (ID,ENTERPRISE_FLAG,BUSINESS_ACCOUNT,CHANNEL_ID,AREA_CODE,PRICE_AREA_CODE");
		sql.append(",CARRIER,BUSINESS_TYPE,INFO_TYPE,CUSTOMER_SUBMIT_NUM,SUCCESS_SUBMIT_NUM,FAILURE_SUBMIT_NUM,MESSAGE_SUCCESS_NUM");
		sql.append(",MESSAGE_FAILURE_NUM,MESSAGE_SIGN,MESSAGE_DATE,CREATED_TIME,CHANNEL_BATCH_DATE,ACCOUNT_BATCH_DATE,MESSAGE_NO_REPORT_NUM) values(");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?)");
		
		logger.info("saveMessageDailyStatistics sql={}",sql.toString());
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (MessageDailyStatistics messageDailyStatistics : messageDailyStatisticsMap.values()) {
				pstmt.setString(1, UUIDUtil.get32UUID());
				pstmt.setString(2, messageDailyStatistics.getEnterpriseFlag());
				pstmt.setString(3, messageDailyStatistics.getBusinessAccount());
				pstmt.setString(4, messageDailyStatistics.getChannelID());
				pstmt.setString(5, messageDailyStatistics.getAreaCode());
				pstmt.setString(6, messageDailyStatistics.getPriceAreaCode());
				pstmt.setString(7, messageDailyStatistics.getCarrier());
				pstmt.setString(8, messageDailyStatistics.getBusinessType());
				pstmt.setString(9, messageDailyStatistics.getInfoType());
				pstmt.setInt(10, messageDailyStatistics.getCustomerSubmitNum());
				pstmt.setInt(11, messageDailyStatistics.getSuccessSubmitNum());
				pstmt.setInt(12, messageDailyStatistics.getFailureSubmitNum());
				pstmt.setInt(13, messageDailyStatistics.getMessageSuccessNum());
				pstmt.setInt(14, messageDailyStatistics.getMessageFailureNum());
				pstmt.setString(15, messageDailyStatistics.getMessageSign());
				pstmt.setString(16, messageDailyStatistics.getMessageDate());
				
				pstmt.setString(17, "0");
				pstmt.setString(18, "0");
				pstmt.setInt(19, 0);

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
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
	}
		
}
