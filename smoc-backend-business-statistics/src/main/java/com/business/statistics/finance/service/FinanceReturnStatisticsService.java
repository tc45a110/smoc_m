package com.business.statistics.finance.service;

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
import com.base.common.manager.AccountInfoManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.util.UUIDUtil;
import com.business.statistics.finance.vo.FinanceAccountReturnStatisticsVo;
import com.business.statistics.util.FileFilterUtil;

/**
 * 	根据前一天的业务日志，统计次日返还的金额和数量
 */
public class FinanceReturnStatisticsService {

	private static final Logger logger = LoggerFactory.getLogger(FinanceReturnStatisticsService.class);
	
	private static final int BUSINESS_RROXY_MT_MESSAGE_LOG_LENGTH = ResourceManager.getInstance().getIntValue("business.proxy.mt.message.log.length");
	
	private static final int BUSINESS_ACCESS_MR_MESSAGE_LOG_LENGTH = ResourceManager.getInstance().getIntValue("business.access.mr.message.log.length");
	
	private static Map<String,FinanceAccountReturnStatisticsVo> saveReturnStatisticsMap = new HashMap<String, FinanceAccountReturnStatisticsVo>();
	
	public static void main(String[] args) {
		logger.info(Arrays.toString(args));
		try {
			long start = System.currentTimeMillis();
			int afterDay = -1;
			if (!(args.length == 1 || args.length == 0)) {
				logger.error("参数不符合规范");
				System.exit(0);
			}
			//通过传参 确定读取时间
			if (args.length >= 1) {
				afterDay = Integer.valueOf(args[0]);
			}
			logger.info("*********************开始次日返还统计*********************");
			String loadTime = DateUtil.getAfterDayDateTime(afterDay, DateUtil.DATE_FORMAT_COMPACT_DAY);
			statisticsSubmitMT(loadTime);
			statisticsReportMR(loadTime);
			//保存今天的数据
			saveFinanceAccountReturnStatistics();
			//返还失败的金额  解冻冻结金额
			updateFinanceAccount();
			logger.info("*********************次日返还统计结束,统计时间:{},耗时:{}毫秒*********************",loadTime,(System.currentTimeMillis() - start));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		//保证所有数据处理完毕 关闭进程
		logger.info("程序正常退出");
		System.exit(0);
	}
	
	/**
	 * 统计下发计费的账号
	 * @param loadTime
	 */
	private static void statisticsSubmitMT(String loadTime) {
		//读取的文件路径
		String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
				.append(LogPathConstant.getProxyBusinessFilePathPart(FixedConstant.RouteLable.MT.name()))
				.toString();
		//读取的文件名
		String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MT.name()))
				.append(loadTime).toString();
		long start = System.currentTimeMillis();
		//过滤文件
		File[] fileList = FileFilterUtil.findFile(filePath, fileName);
		
		if (fileList == null || fileList.length == 0) {
			logger.info("{},无短信状态报告文件,日期=",DateUtil.getCurDateTime(),loadTime);
			return;
		}else{
			logger.info("{},短信下发文件数量={},日期={}",DateUtil.getCurDateTime(),fileList.length,loadTime);
		}
	
		String line = null;
		String[] array = null;
		String accountID = null;
		String successFlag = null;
		String financeAccountID = null;
		String accountChargeType = null;
		String sendTime = null;
		int count = 0;
		int row = 0;
		for (File file : fileList) {
			long startTime = System.currentTimeMillis();
			try {
				logger.info("开始处理文件:{}",file.getName());
				row = 0;
				LineIterator lines = FileUtils.lineIterator(file);
				while (lines.hasNext()) {
					count++;
					row++;
					line = lines.next();
					array = line.split(FixedConstant.LOG_SEPARATOR);
					if(array.length < BUSINESS_RROXY_MT_MESSAGE_LOG_LENGTH){
						continue;
					}
					//状态报告扣费无需操作
					if(FixedConstant.AccountConsumeType.REPORT.ordinal() == Integer.valueOf(array[array.length - 1])) {
						continue;
					}
					accountID = array[1];
					successFlag = array[32];
					financeAccountID = array[37];
					sendTime = array[4].substring(0,10).replace("-", "");
					accountChargeType = AccountInfoManager.getInstance().getChargeType(accountID);
					
					if(!FixedConstant.AccountChargeType.SUBMIT.name().equals(accountChargeType)) {
						continue;
					}
					
					String key = new StringBuffer().append(accountID).append(FixedConstant.SPLICER).append(financeAccountID).toString();
					
					FinanceAccountReturnStatisticsVo vo = saveReturnStatisticsMap.get(key);
					if(vo == null) {
						vo = new FinanceAccountReturnStatisticsVo();
						saveReturnStatisticsMap.put(key, vo);
						vo.setAccountID(accountID);
						vo.setConsumeAccountID(financeAccountID);
						vo.setAccountChargeType(accountChargeType);
						vo.setConsumeType(array[37]);
						vo.setSendTime(sendTime);
						vo.setNoReportNum(0);
						vo.setNoReportSum(0d);
					}
					
					int SubmitNum = Integer.parseInt(array[31]);
					double consumeSum = Double.parseDouble(array[39]) * SubmitNum;
					
					if(InsideStatusCodeConstant.SUCCESS_CODE.equals(successFlag)) {
						vo.setSucessNum(SubmitNum + (vo.getSucessNum() == null ? 0 : vo.getSucessNum()));
						vo.setSucessSum(consumeSum + (vo.getSucessSum() == null ? 0d : vo.getSucessSum()));
					}else {
						//下发计费模式，返还数量和返还金额应该和失败数量和失败返还金额相等
						vo.setFailureNum(SubmitNum + (vo.getFailureNum() == null ? 0 : vo.getFailureNum()));
						vo.setFailureSum(consumeSum + (vo.getFailureSum() == null ? 0d : vo.getFailureSum()));
						
						vo.setReturnNum(SubmitNum + (vo.getReturnNum() == null ? 0 : vo.getReturnNum()));
						vo.setReturnSum(consumeSum + (vo.getReturnSum() == null ? 0d : vo.getReturnSum()));
					}
					//下发计费模式，下发计费暂时不考虑未知情况，下发多少解冻多少
					vo.setUnfreezeNum(SubmitNum + (vo.getUnfreezeNum() == null ? 0 : vo.getUnfreezeNum()));
					vo.setUnfreezeSum(consumeSum + (vo.getUnfreezeSum() == null ? 0d : vo.getUnfreezeSum()));
					
				}

			} catch (Exception e) {
				logger.error("{},读取短信状态报告日志错误:file={}",DateUtil.getCurDateTime(),file.getAbsolutePath());
				logger.error(e.getMessage(),e);
			}
			logger.info("文件已处理完毕:{},处理数据:{},耗时:{}毫秒",file.getName(),row,(System.currentTimeMillis() - startTime));
		}
		logger.info("{},总loadProxyBusinessMT记录数:{},总耗时:{}毫秒", DateUtil.getCurDateTime(), count, (System.currentTimeMillis() - start));
	}
	
	/**
	 * 统计状态报告计费的账号
	 * @param loadTime
	 */
	private static void statisticsReportMR(String loadTime) {
		// 读取的文件路径
		String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
				.append(LogPathConstant.getAccessBusinessFilePathPart(FixedConstant.RouteLable.MR.name())).toString();
		// 读取的文件名
		String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MR.name()))
				.append(loadTime).toString();
		long start = System.currentTimeMillis();
		// 过滤文件
		File[] fileList = FileFilterUtil.findFile(filePath, fileName);

		if (fileList == null || fileList.length == 0) {
			logger.info("{},无短信状态报告文件,日期=", DateUtil.getCurDateTime(), loadTime);
			return;
		} else {
			logger.info("{},短信下发文件数量={},日期={}", DateUtil.getCurDateTime(), fileList.length, loadTime);
		}

		String line = null;
		String[] array = null;
		String accountID = null;
		String successFlag = null;
		String financeAccountID = null;
		String sendTime = null;
		String accountChargeType = null;
		String statusCodeSource = null;
		String reportTime = null;
		int count = 0;
		int row = 0;
		for (File file : fileList) {
			long startTime = System.currentTimeMillis();
			try {
				logger.info("开始处理文件:{}",file.getName());
				row = 0;
				LineIterator lines = FileUtils.lineIterator(file);
				while (lines.hasNext()) {
					row++;
					count++;
					line = lines.next();
					array = line.split(FixedConstant.LOG_SEPARATOR);
					if (array.length < BUSINESS_ACCESS_MR_MESSAGE_LOG_LENGTH) {
						continue;
					}

					accountID = array[1];
					successFlag = array[24];
					financeAccountID = array[31];
					sendTime = array[4].substring(0, 10).replace("-", "");
					reportTime = array[0].substring(0, 10).replace("-", "");
					accountChargeType = AccountInfoManager.getInstance().getChargeType(accountID);
					statusCodeSource = array[27];
					
					if(FixedConstant.AccountConsumeType.REPORT.ordinal() == Integer.valueOf(array[37])) {
						continue;
					}
					
					if(FixedConstant.StatusReportSource.ACCESS.name().equals(statusCodeSource)) {
						continue;
					}
					
					if(!FixedConstant.AccountChargeType.REPORT.name().equals(accountChargeType)) {
						continue;
					}
					
					String key = new StringBuffer().append(accountID).append(FixedConstant.SPLICER)
												   .append(financeAccountID).append(FixedConstant.SPLICER)
							                       .append(sendTime).append(FixedConstant.SPLICER)
							                       .append(reportTime).toString();
					
					FinanceAccountReturnStatisticsVo vo = saveReturnStatisticsMap.get(key);
					if(vo == null) {
						vo = new FinanceAccountReturnStatisticsVo();
						saveReturnStatisticsMap.put(key, vo);
						vo.setAccountID(accountID);
						vo.setAccountChargeType(accountChargeType);
						vo.setConsumeAccountID(financeAccountID);
						vo.setSendTime(sendTime);
						vo.setReportTime(reportTime);
						vo.setConsumeType(array[37]);
					}
					
					double consumeSum = Double.parseDouble(array[32]);
					
					if(InsideStatusCodeConstant.SUCCESS_CODE.equals(successFlag)) {
						vo.setSucessNum(1 + vo.getSucessNum());
						vo.setSucessSum(consumeSum + vo.getSucessSum());
					} else {
						vo.setFailureNum(1 + vo.getFailureNum());
						vo.setFailureSum(consumeSum + vo.getFailureSum());
						
						vo.setReturnNum(1 + vo.getReturnNum());
						vo.setReturnSum(consumeSum + vo.getReturnSum());
					}
					
					vo.setUnfreezeNum(1 + vo.getUnfreezeNum());
					vo.setUnfreezeSum(consumeSum + vo.getUnfreezeSum());
				}

			} catch (Exception e) {
				logger.error("{},读取短信状态报告日志错误:file={}", DateUtil.getCurDateTime(), file.getAbsolutePath());
				logger.error(e.getMessage(), e);
			}
			logger.info("文件已处理完毕:{},处理数据:{},耗时:{}毫秒",file.getName(),row,(System.currentTimeMillis() - startTime));
		}
		logger.info("{},总loadAccessBusinessMR记录数:{},总耗时:{}毫秒", DateUtil.getCurDateTime(), count, (System.currentTimeMillis() - start));
	}
	
	private static void saveFinanceAccountReturnStatistics() {
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc.finance_account_return_statistics (ID,ACCOUNT_ID,CONSUME_ACCOUNT_ID,SEND_TIME,REPORT_TIME,");
		sql.append("RETURN_NUM,RETURN_SUM,SUCESS_NUM,SUCESS_SUM,UNFREEZE_NUM,UNFREEZE_SUM,FAILURE_NUM,FAILURE_SUM,");
		sql.append("CREATED_TIME) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())");
		
		logger.debug("saveFinanceAccountReturnStatistics sql={}",sql.toString());
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (FinanceAccountReturnStatisticsVo vo : saveReturnStatisticsMap.values()) {
				pstmt.setString(1, UUIDUtil.get32UUID());
				pstmt.setString(2, vo.getAccountID());
				pstmt.setString(3, vo.getConsumeAccountID());
				pstmt.setString(4, vo.getSendTime());
				pstmt.setString(5, vo.getReportTime());
				pstmt.setInt(6, vo.getReturnNum());
				pstmt.setDouble(7, vo.getReturnSum());
				pstmt.setInt(8, vo.getSucessNum());
				pstmt.setDouble(9, vo.getSucessSum());
				pstmt.setInt(10, vo.getUnfreezeNum());
				pstmt.setDouble(11, vo.getUnfreezeSum());
				pstmt.setInt(12, vo.getFailureNum());
				pstmt.setDouble(13, vo.getFailureSum());
				
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

	/**
	 * 返回失败的金额
	 * @param voList
	 */
	private static void updateFinanceAccount() {
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE smoc.finance_account SET ACCOUNT_USABLE_SUM = ACCOUNT_USABLE_SUM + ?,");
		sql.append("ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM - ?,ACCOUNT_CONSUME_SUM = ACCOUNT_CONSUME_SUM + ? ");
		sql.append("WHERE ACCOUNT_ID = ?");
		
		logger.debug("updateFinanceAccount sql={}",sql.toString());
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			
			for (FinanceAccountReturnStatisticsVo vo : saveReturnStatisticsMap.values()) {
				pstmt.setDouble(1, vo.getReturnSum());
				pstmt.setDouble(2, vo.getUnfreezeSum());
				pstmt.setDouble(3, vo.getSucessSum());
				pstmt.setString(4, vo.getConsumeAccountID());
				
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
