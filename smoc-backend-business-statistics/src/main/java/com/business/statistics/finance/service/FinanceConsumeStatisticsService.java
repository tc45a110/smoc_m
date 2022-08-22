package com.business.statistics.finance.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.util.DateUtil;
import com.base.common.util.UUIDUtil;
import com.business.statistics.finance.vo.FinanceAccountReturnStatisticsVo;

/**
 * 	统计消费记录:根据前一天的消费流水和今天统计入库的返回统计的数据进行入库
 */
public class FinanceConsumeStatisticsService {

	private static final Logger logger = LoggerFactory.getLogger(FinanceConsumeStatisticsService.class);
	
	private static List<FinanceAccountReturnStatisticsVo> saveConsumeStatisticsList = new ArrayList<FinanceAccountReturnStatisticsVo>();
	
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
			logger.info("*********************开始消费记录统计*********************");
			String consumeTime = DateUtil.getAfterDayDateTime(afterDay, DateUtil.DATE_FORMAT_COMPACT_DAY);
			//1.先统计消费数量和消费金额
			loadAccountConsumeType(consumeTime);
			//2.根据创建时间获取当天的记录，统计成功数量、成功金额、失败数量、失败金额、冻结数量、解冻金额、返回数量、返回金额、未知数量和未知金额
			loadFinanceAccountReturnStatistics();
			//3.保存今天的数据
			saveFinanceAccountConsumeStatistics();
			logger.info("*********************消费记录统计结束,统计时间:{},耗时:{}毫秒*********************",consumeTime,(System.currentTimeMillis() - start));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		//保证所有数据处理完毕 关闭进程
		logger.info("程序正常退出");
		System.exit(0);
	}
	
	/**
	 * 获取loadTime当天的消费金额和消费数量
	 * @param sendTime 
	 */
	private static void loadAccountConsumeType(String consumeTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ACCOUNT_ID,CONSUME_ACCOUNT_ID,SEND_TIME,CONSUME_TIME,CONSUME_TYPE,SUM(CONSUME_NUM) CONSUME_NUM,SUM(CONSUME_SUM) CONSUME_SUM ");
		sql.append("FROM smoc.finance_account_consume_flow WHERE CONSUME_TIME = ? ");
		sql.append("GROUP BY ACCOUNT_ID,CONSUME_ACCOUNT_ID,CONSUME_TYPE,SEND_TIME,CONSUME_TIME");
		FinanceAccountReturnStatisticsVo vo = null;
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, consumeTime);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vo = new FinanceAccountReturnStatisticsVo();
				vo.setAccountID(rs.getString("ACCOUNT_ID"));
				vo.setConsumeAccountID(rs.getString("CONSUME_ACCOUNT_ID"));
				vo.setSendTime(rs.getString("SEND_TIME"));
				vo.setConsumeTime(rs.getString("CONSUME_TIME"));
				vo.setConsumeType(rs.getString("CONSUME_TYPE"));
				vo.setConsumeNum(rs.getInt("CONSUME_NUM"));
				vo.setConsumeSum(rs.getDouble("CONSUME_SUM"));
				
				saveConsumeStatisticsList.add(vo);
			}
			logger.info("根据消费流水统计消费时间:{},总消费金额和消费数量,耗时:{}毫秒",consumeTime,start);
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

	private static void saveFinanceAccountConsumeStatistics() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc.finance_account_consume_statistics (ID,ACCOUNT_ID,CONSUME_ACCOUNT_ID,");
		sql.append("SEND_TIME,CONSUME_NUM,CONSUME_SUM,SUCESS_NUM,SUCESS_SUM,UNFREEZE_NUM,UNFREEZE_SUM,");
		sql.append("FAILURE_NUM,FAILURE_SUM,CONSUME_TYPE,NO_REPORT_NUM,NO_REPORT_SUM,CREATED_TIME) ");
		sql.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())");
		
		logger.debug("saveFinanceAccountConsumeStatistics sql={}",sql.toString());
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (FinanceAccountReturnStatisticsVo vo : saveConsumeStatisticsList) {
				pstmt.setString(1, UUIDUtil.get32UUID());
				pstmt.setString(2, vo.getAccountID());
				pstmt.setString(3, vo.getConsumeAccountID());
				pstmt.setString(4, vo.getSendTime());
				pstmt.setInt(5, vo.getConsumeNum());
				pstmt.setDouble(6, vo.getConsumeSum());
				pstmt.setInt(7, vo.getSucessNum());
				pstmt.setDouble(8, vo.getSucessSum());
				pstmt.setInt(9, vo.getUnfreezeNum());
				pstmt.setDouble(10, vo.getUnfreezeSum());
				pstmt.setInt(11, vo.getFailureNum());
				pstmt.setDouble(12, vo.getFailureSum());
				pstmt.setString(13, vo.getConsumeType());
				pstmt.setInt(14, vo.getNoReportNum());
				pstmt.setDouble(15, vo.getNoReportSum());
				
				pstmt.addBatch();
			}
			
			pstmt.executeBatch();
			conn.commit();
			logger.info("保存当前需要出入消费统计的数据,条数:{},耗时:{}毫秒",saveConsumeStatisticsList.size(),(System.currentTimeMillis() - start));
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
	
	private static void loadFinanceAccountReturnStatistics() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long start = System.currentTimeMillis();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ACCOUNT_ID,CONSUME_ACCOUNT_ID,SEND_TIME,SUM(RETURN_NUM) RETURN_NUM,SUM(RETURN_SUM) RETURN_SUM,SUM(SUCESS_NUM) SUCESS_NUM,");
		sql.append("SUM(SUCESS_SUM) SUCESS_SUM,SUM(UNFREEZE_NUM) UNFREEZE_NUM,SUM(UNFREEZE_SUM) UNFREEZE_SUM,SUM(FAILURE_NUM) FAILURE_NUM,");
		sql.append("SUM(FAILURE_SUM) FAILURE_SUM,SUM(NO_REPORT_NUM) NO_REPORT_NUM,SUM(NO_REPORT_SUM) NO_REPORT_SUM ");
		sql.append("FROM smoc.finance_account_return_statistics WHERE DATE_FORMAT(CREATED_TIME, '%Y-%m-%d') = ? GROUP BY ACCOUNT_ID,CONSUME_ACCOUNT_ID,SEND_TIME");
		FinanceAccountReturnStatisticsVo vo = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			String nowTime = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_DAY);
			pstmt.setString(1, nowTime);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vo = new FinanceAccountReturnStatisticsVo();
				vo.setAccountID(rs.getString("ACCOUNT_ID"));
				vo.setConsumeAccountID(rs.getString("CONSUME_ACCOUNT_ID"));
				vo.setSendTime(rs.getString("SEND_TIME"));
				vo.setReturnNum(rs.getInt("RETURN_NUM"));
				vo.setReturnSum(rs.getDouble("RETURN_SUM"));
				vo.setSucessNum(rs.getInt("SUCESS_NUM"));
				vo.setSucessSum(rs.getDouble("SUCESS_SUM"));
				vo.setUnfreezeNum(rs.getInt("UNFREEZE_NUM"));
				vo.setUnfreezeSum(rs.getDouble("UNFREEZE_SUM"));
				vo.setFailureNum(rs.getInt("FAILURE_NUM"));
				vo.setFailureSum(rs.getDouble("FAILURE_SUM"));
				vo.setNoReportNum(rs.getInt("NO_REPORT_NUM"));
				vo.setNoReportSum(rs.getDouble("NO_REPORT_SUM"));
				vo.setConsumeType(String.valueOf(FixedConstant.AccountConsumeType.SUBMIT.ordinal()));
				
				saveConsumeStatisticsList.add(vo);
			}
			logger.info("加载当前处理的返回统计数据(次日返的数据),当前时间:{},耗时:{}毫秒",nowTime,start);
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
}
