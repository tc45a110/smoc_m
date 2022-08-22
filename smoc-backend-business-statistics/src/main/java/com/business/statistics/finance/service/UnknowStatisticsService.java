package com.business.statistics.finance.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.util.DateUtil;
import com.base.common.util.UUIDUtil;
import com.business.statistics.finance.vo.FinanceAccountReturnStatisticsVo;

/**
 *  针对按天返还72小时后，针对未知部分进行返还和冻结解除
 */
public class UnknowStatisticsService {
	
	private static final Logger logger = LoggerFactory.getLogger(UnknowStatisticsService.class);
	
	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			logger.info(Arrays.toString(args));
			String afterDay = null;

			if (!(args.length == 1 || args.length == 0)) {
				logger.error("参数不符合规范");
				System.exit(0);
			}
			
			if (args.length == 1) {
				afterDay = args[0];
			}else{
				afterDay = DateUtil.getAfterDayDateTime(-4, DateUtil.DATE_FORMAT_COMPACT_DAY);
			}
			logger.info("*********************开始未知返回统计*********************");
			Map<String,HashMap<String,Object>> accountConsumeSumMap = loadAccountConsumeSum(afterDay);
			Map<String,HashMap<String,Object>> accountUnfreezeSumMap = loadAccountUnfreezeSum(afterDay);
			logger.info(accountConsumeSumMap.toString());
			logger.info(accountUnfreezeSumMap.toString());
			List<FinanceAccountReturnStatisticsVo> voList = new ArrayList<FinanceAccountReturnStatisticsVo>();
			FinanceAccountReturnStatisticsVo vo = null;
			for(String key : accountConsumeSumMap.keySet()) {
				HashMap<String,Object> consumeMap = accountConsumeSumMap.get(key);
				HashMap<String,Object> unfreezeMap = accountUnfreezeSumMap.get(key);
				if(unfreezeMap != null) {
					vo = new FinanceAccountReturnStatisticsVo();
					String accountID = key.split(FixedConstant.SPLICER)[0];
					String consumeAccountID = key.split(FixedConstant.SPLICER)[1];
					//返还未知数量 = 消费数量 - 已解冻数量
					int noReportNum = (int)consumeMap.get("CONSUME_NUM") - (int)unfreezeMap.get("UNFREEZE_NUM");
					//返还未知金额 = 消费金额 - 已解冻金额
					double noReportSum = (double)consumeMap.get("CONSUME_SUM") - (double)unfreezeMap.get("UNFREEZE_SUM");
					
					vo.setAccountID(accountID);
					vo.setConsumeAccountID(consumeAccountID);
					vo.setSendTime(afterDay);
					vo.setReturnNum(noReportNum);
					vo.setReturnSum(noReportSum);
					vo.setNoReportNum(noReportNum);
					vo.setNoReportSum(noReportSum);
					voList.add(vo);
				}
			}
			logger.info(voList.toString());
			//返回未知金额
			updateFinanceAccount(voList);
			//保存返回未知的金额和数量记录-返还统计表
			saveFinanceAccountReturnStatistics(voList);
			logger.info("*********************未知返回统计结束,统计时间:{},耗时:{}毫秒*********************",afterDay,(System.currentTimeMillis() - start));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		//保证所有数据处理完毕 关闭进程
		logger.info("程序正常退出");
		System.exit(0);
	}
	
	
	/**
	 * 获取loadTime当天的消费金额和消费数量
	 * @param loadTime 
	 * @return
	 */
	private static Map<String,HashMap<String,Object>> loadAccountConsumeSum(String sendTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ACCOUNT_ID,CONSUME_ACCOUNT_ID,SUM(CONSUME_NUM) CONSUME_NUM,SUM(CONSUME_SUM) CONSUME_SUM ");
		sql.append("FROM smoc.finance_account_consume_flow WHERE SEND_TIME = ? AND CONSUME_TYPE = 1 ");
		sql.append("GROUP BY ACCOUNT_ID,CONSUME_ACCOUNT_ID");
		Map<String,HashMap<String,Object>> accountConsumeSumMap = new HashMap<String, HashMap<String,Object>>();
		HashMap<String,Object> map = null;
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, sendTime);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String key = new StringBuffer().append(rs.getString("ACCOUNT_ID")).append(FixedConstant.SPLICER)
											   .append(rs.getString("CONSUME_ACCOUNT_ID")).toString();
				
				map = new HashMap<String,Object>();
				map.put("CONSUME_NUM", rs.getInt("CONSUME_NUM"));
				map.put("CONSUME_SUM", rs.getDouble("CONSUME_SUM"));
				accountConsumeSumMap.put(key, map);
			}
			logger.info("根据消费流水表,获取发送时间:{},当天的消费金额和消费数量,耗时:{}毫秒",sendTime,(System.currentTimeMillis() - start));
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
		return accountConsumeSumMap;
	}
	
	/**
	 * 获取已解冻的金额
	 * @param sendTime
	 * @return
	 */
	private static Map<String,HashMap<String,Object>> loadAccountUnfreezeSum(String sendTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ACCOUNT_ID,CONSUME_ACCOUNT_ID,SUM(UNFREEZE_NUM) UNFREEZE_NUM,SUM(UNFREEZE_SUM) UNFREEZE_SUM FROM smoc.finance_account_return_statistics ");
		sql.append("WHERE SEND_TIME = ? AND (REPORT_TIME IS NOT NULL OR REPORT_TIME != \"\") ");
		sql.append("GROUP BY ACCOUNT_ID,CONSUME_ACCOUNT_ID");
		Map<String,HashMap<String,Object>> accountConsumeSumMap = new HashMap<String, HashMap<String,Object>>();
		HashMap<String,Object> map = null;
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, sendTime);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String key = new StringBuffer().append(rs.getString("ACCOUNT_ID")).append(FixedConstant.SPLICER)
											   .append(rs.getString("CONSUME_ACCOUNT_ID")).toString();
											   
				map = new HashMap<String,Object>();
				map.put("UNFREEZE_NUM", rs.getInt("UNFREEZE_NUM"));
				map.put("UNFREEZE_SUM", rs.getDouble("UNFREEZE_SUM"));
				accountConsumeSumMap.put(key, map);
			}
			
			logger.info("根据返还统计表,获取发送时间:{},获取已解冻的总金额,耗时:{}毫秒",sendTime,(System.currentTimeMillis() - start));
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
		return accountConsumeSumMap;
	}

	/**
	 * 记录返还未知部分的金额
	 * @param voList
	 */
	private static void saveFinanceAccountReturnStatistics(List<FinanceAccountReturnStatisticsVo> voList) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc.finance_account_return_statistics (ID,ACCOUNT_ID,CONSUME_ACCOUNT_ID,SEND_TIME,");
		sql.append("RETURN_NUM,RETURN_SUM,NO_REPORT_NUM,NO_REPORT_SUM,");
		sql.append("CREATED_TIME) VALUES(?,?,?,?,?,?,?,?,NOW())");
		
		logger.debug("saveFinanceAccountReturnStatistics sql={}",sql.toString());
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (FinanceAccountReturnStatisticsVo vo : voList) {
				pstmt.setString(1, UUIDUtil.get32UUID());
				pstmt.setString(2, vo.getAccountID());
				pstmt.setString(3, vo.getConsumeAccountID());
				pstmt.setString(4, vo.getSendTime());
				pstmt.setInt(5, vo.getReturnNum());
				pstmt.setDouble(6, vo.getReturnSum());
				pstmt.setInt(7, vo.getNoReportNum());
				pstmt.setDouble(8, vo.getNoReportSum());
				logger.info("账号:{},消费账号:{},发送时间:{},未知返还数量:{},未知返还金额:{}",vo.getAccountID(),vo.getConsumeAccountID(),vo.getSendTime(),vo.getReturnNum(),vo.getReturnSum());
				
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
			conn.commit();
			logger.info("返还统计表增加返回冻结的金额的记录,总耗时:{}毫秒",(System.currentTimeMillis() - start));
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
	 * 返回未知的金额
	 * @param voList
	 */
	private static void updateFinanceAccount(List<FinanceAccountReturnStatisticsVo> voList) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE smoc.finance_account SET ACCOUNT_USABLE_SUM = ACCOUNT_USABLE_SUM + ?,");
		sql.append("ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM - ? ");
		sql.append("WHERE ACCOUNT_ID = ?");
		
		logger.debug("updateFinanceAccount sql={}",sql.toString());
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (FinanceAccountReturnStatisticsVo vo : voList) {
				pstmt.setDouble(1, vo.getNoReportSum());
				pstmt.setDouble(2, vo.getNoReportSum());
				pstmt.setString(3, vo.getConsumeAccountID());
				
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
			conn.commit();
			logger.info("返还账号未知的金额,,总耗时:{}毫秒",(System.currentTimeMillis() - start));
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
