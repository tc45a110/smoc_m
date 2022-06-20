/**
 * @desc
 * 
 */
package com.business.access.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.util.UUIDUtil;

public class AccountFinanceDAO {
	private static final Logger logger = LoggerFactory.getLogger(AccountFinanceDAO.class);
	
	/**
	 * 扣费及维护消费流水
	 * @param accountFinanceMap
	 * @param consumeType
	 * @param consumeTime
	 */
	static public void deductBalance(Map<String,Map<String,Object>> accountConsumeFlowMap,Map<String,Double> accountFinanceMap,String consumeType,String consumeTime) {
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE smoc.finance_account SET");
		
		sql.append(" ACCOUNT_USABLE_SUM = ACCOUNT_USABLE_SUM - ?,");
	
		
		//下发时计费：扣费金额即冻结金额，暂不产生消费金额，一定周期内返还余额、产生消费金额
		//回执成功计费:扣费金额即消费金额，不产生冻结金额;
		if (String.valueOf(FixedConstant.AccountConsumeType.REPORT.ordinal()).equals(consumeType)) {
			sql.append(" ACCOUNT_CONSUME_SUM = ACCOUNT_CONSUME_SUM + ?,");
		}else{
			sql.append(" ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM + ?,");
		}
		
		sql.append(" UPDATED_TIME=NOW() ");
		
		sql.append(" WHERE ACCOUNT_ID = ? ");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			
			//扣减余额
			pstmt1 = conn.prepareStatement(sql.toString());
			for(Map.Entry<String,Double> entry: accountFinanceMap.entrySet()) {				
				pstmt1.setDouble(1, entry.getValue());
				pstmt1.setDouble(2, entry.getValue());
				pstmt1.setString(3, entry.getKey());		
				pstmt1.addBatch();
			}
			pstmt1.executeBatch();
			
			
			sql.setLength(0);
			sql.append("INSERT INTO smoc.finance_account_consume_flow");
			sql.append(" (ID,ACCOUNT_ID,SEND_TIME,CONSUME_ACCOUNT_ID,CONSUME_TIME,CONSUME_TYPE,CONSUME_NUM,CONSUME_SUM,CREATED_TIME,UPDATED_TIME)");
			sql.append(" values(");
			sql.append("?,?,?,?,?,?,?,?,NOW(),NOW())");
			
			pstmt2 = conn.prepareStatement(sql.toString());
			for(Map.Entry<String,Map<String,Object>> entry: accountConsumeFlowMap.entrySet()) {	
				String key = entry.getKey();
				//以计费账号的维度统计数据
				String accountID = key.split(FixedConstant.SPLICER)[0];
				String financeAccountID = key.split(FixedConstant.SPLICER)[1];
				//获取下发时间，归属到天yyyyMMdd
				String sendTime = key.split(FixedConstant.SPLICER)[2];
				//扣费金额及数量
				Long consumeNum = (Long)entry.getValue().get("CONSUME_NUM");
				Double consumeSum = (Double)entry.getValue().get("CONSUME_SUM");
				
				pstmt2.setString(1,UUIDUtil.get32UUID());	
				pstmt2.setString(2,accountID);	
				pstmt2.setString(3,sendTime);	
				pstmt2.setString(4,financeAccountID);	
				pstmt2.setString(5,consumeTime);	
				pstmt2.setString(6,consumeType);	
				pstmt2.setLong(7, consumeNum);
				pstmt2.setDouble(8, consumeSum);
					
				pstmt2.addBatch();
			}
			pstmt2.executeBatch();
			
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e.getMessage(), e1);
			}
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, null);
			LavenderDBSingleton.getInstance().closeAll(null, pstmt1, conn);
		}
	}
}


