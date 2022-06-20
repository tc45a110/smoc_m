package com.protocol.access.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.dao.LavenderDBSingleton;

import com.protocol.access.vo.Report;
import com.protocol.access.vo.MessageInfo;

public class DAO {

	private static final Logger logger = LoggerFactory.getLogger(DAO.class);



	/**
	 * 保存下发数据
	 * @param list
	 */
	public static void saveRouteMessageMtInfoList(List<MessageInfo> list) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.route_message_mt_info (ACCOUNT_ID,PHONE_NUMBER,SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_FORMAT,MESSAGE_ID,TEMPLATE_ID,PROTOCOL,");
		sql.append("ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,PHONE_NUMBER_NUMBER,MESSAGE_CONTENT_NUMBER,REPORT_FLAG,OPTION_PARAM,CREATED_TIME) ");
		sql.append("values(");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW()");
		sql.append(")");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			
			for(MessageInfo vo : list) {
				pstmt.setString(1, vo.getAccountId());
				pstmt.setString(2, vo.getPhoneNumber());
				pstmt.setString(3, vo.getSubmitTime());
				pstmt.setString(4, vo.getMessageContent());
				pstmt.setString(5, vo.getMessageFormat());
				pstmt.setString(6, vo.getMessageId());
				pstmt.setString(7, vo.getTemplateId());
				pstmt.setString(8, vo.getProtocol().toUpperCase());
				pstmt.setString(9, vo.getAccountSrcId());
				pstmt.setString(10, vo.getAccountBusinessCode());
				pstmt.setInt(11, vo.getPhoneNumberNumber());
				pstmt.setInt(12, vo.getMessageContentNumber());
				pstmt.setInt(13, vo.getReportFlag());
				pstmt.setString(14, vo.getOptionParam());
				
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
	
	/**
	 * 保存状态报告数据
	 * @param reports
	 */
	public static void saveRouteMessageMrInfoList(List<Report> reports) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("INSERT INTO smoc_route.route_message_mr_info ");
		sql.append("(ACCOUNT_ID,PHONE_NUMBER,REPORT_TIME,SUBMIT_TIME,STATUS_CODE, MESSAGE_ID,TEMPLATE_ID,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,");
		sql.append("MESSAGE_TOTAL,MESSAGE_INDEX,OPTION_PARAM,CREATED_TIME) ");
		sql.append("values(");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,NOW()");
		sql.append(")");
		logger.debug("save report sql:"+sql.toString());
		
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			for(Report vo:reports){
				pstmt.setString(1, vo.getAccountId());
				pstmt.setString(2, vo.getPhoneNumber());
				pstmt.setString(3, vo.getReportTime());
				pstmt.setString(4, vo.getSubmitTime());
				pstmt.setString(5, vo.getStatusCode());
				pstmt.setString(6, vo.getMessageId());
				pstmt.setString(7, vo.getTemplateId());
				
				pstmt.setString(8, vo.getAccountSrcId());
				pstmt.setString(9, vo.getAccountBusinessCode());
				pstmt.setInt(10, vo.getMessageTotal());
				
				pstmt.setInt(11, vo.getMessageIndex());
				pstmt.setString(12, vo.getOptionParam());
				
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e);
			}
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
	}
	
	/**
	 * 保存上行数据
	 * @param reports
	 */
	public static void saveRouteMessageMoInfoList(List<Report> reports) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("INSERT INTO smoc_route.route_message_mo_info ");
		sql.append("(ACCOUNT_ID,PHONE_NUMBER,MO_TIME,MESSAGE_CONTENT,MESSAGE_FORMAT,MESSAGE_ID,");
		sql.append("ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,OPTION_PARAM,CREATED_TIME) ");
		sql.append("values(");
		sql.append("?,?,?,?,?,?,?,?,?,NOW()");
		sql.append(")");
		logger.debug("save report sql:"+sql.toString());
		
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			logger.info("pstmt:"+pstmt);
			for(Report vo:reports){
				logger.info("vo:"+vo);
				pstmt.setString(1, vo.getAccountId());
				pstmt.setString(2, vo.getPhoneNumber());
				pstmt.setString(3, vo.getReportTime());
				pstmt.setString(4, vo.getMOMessageContent());
				pstmt.setString(5, "8");
				pstmt.setString(6, vo.getMessageId());
				pstmt.setString(7, vo.getAccountSrcId());
				
				pstmt.setString(8, vo.getAccountBusinessCode());
				pstmt.setString(9, vo.getOptionParam());
				
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e);
			}
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
	}

	/**
	 * 加载状态报告数据
	 * @param accountId
	 * @return
	 */
	public static List<Report> loadRouteMessageMrInfoList(String accountId) {
		//load后删除
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		sql.append("SELECT ID,ACCOUNT_ID,PHONE_NUMBER,SUBMIT_TIME,REPORT_TIME,STATUS_CODE,MESSAGE_ID,TEMPLATE_ID,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,MESSAGE_TOTAL,MESSAGE_INDEX,OPTION_PARAM ");
		sql.append("FROM smoc_route.route_message_mr_info ");
		sql.append("WHERE ACCOUNT_ID = ? ORDER BY ID ASC");
		
		List<Report> list = new ArrayList<Report>();
		Report vo = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, accountId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vo = new Report();
				vo.setId(rs.getLong("ID"));
				vo.setAccountId(rs.getString("ACCOUNT_ID"));
				vo.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				vo.setSubmitTime(rs.getString("SUBMIT_TIME"));
				vo.setReportTime(rs.getString("REPORT_TIME"));
				vo.setStatusCode(rs.getString("STATUS_CODE"));
				vo.setMessageId(rs.getString("MESSAGE_ID"));
				vo.setTemplateId(rs.getString("TEMPLATE_ID"));
				vo.setAccountSrcId(rs.getString("ACCOUNT_SRC_ID"));
				vo.setAccountBusinessCode(rs.getString("ACCOUNT_BUSINESS_CODE"));
				vo.setMessageTotal(rs.getInt("MESSAGE_TOTAL"));
				vo.setMessageIndex(rs.getInt("MESSAGE_INDEX"));
				vo.setOptionParam(rs.getString("OPTION_PARAM"));
				vo.setDbFlag(true);
				list.add(vo);	
			}
			
			if(list.size() > 0){
				logger.info("{}本次load report 数据:{}",accountId,list.size());
				long max =  list.get(list.size() - 1).getId();
				StringBuffer sql2 = new StringBuffer();
				sql2.append("DELETE FROM smoc_route.route_message_mr_info ");
				sql2.append("WHERE ID <= ? ");
				sql2.append("AND ACCOUNT_ID = ?");
				logger.info("delete report sql:"+sql2.toString());
				pstmt2 = conn.prepareStatement(sql2.toString());
				pstmt2.setLong(1, max);
				pstmt2.setString(2, accountId);
				int count = pstmt2.executeUpdate();
				conn.commit();
				logger.info("{}删除report ID<={},共{}条",accountId,max,count);
			}
				
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e1);
			}
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, conn);
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return list;
	}
	
	/**
	 * 加载上行数据
	 * @param accountId
	 * @return
	 */
	public static List<Report> loadRouteMessageMOInfoList(String accountId) {
		//load后删除
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		sql.append("SELECT ID,ACCOUNT_ID,PHONE_NUMBER,MO_TIME,MESSAGE_CONTENT,MESSAGE_FORMAT,MESSAGE_ID,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,OPTION_PARAM ");
		sql.append("FROM smoc_route.route_message_mo_info ");
		sql.append("WHERE ACCOUNT_ID = ? ORDER BY ID ASC");
		
		List<Report> list = new ArrayList<Report>();
		Report report = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, accountId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				report = new Report();
				report.setId(rs.getLong("ID"));
				report.setAccountId(rs.getString("ACCOUNT_ID"));
				report.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				report.setMoTime(rs.getString("MO_TIME"));
				report.setMOMessageContent(rs.getString("MESSAGE_CONTENT"));
				report.setMessageId(rs.getString("MESSAGE_ID"));
				report.setAccountSrcId(rs.getString("ACCOUNT_SRC_ID"));
				report.setAccountBusinessCode(rs.getString("ACCOUNT_BUSINESS_CODE"));
				report.setOptionParam(rs.getString("OPTION_PARAM"));
				list.add(report);	
			}
			
			if(list.size() > 0){
				logger.info("{}本次load MO 数据:{}",accountId,list.size());
				long max =  list.get(list.size() - 1).getId();
				StringBuffer sql2 = new StringBuffer();
				sql2.append("DELETE FROM smoc_route.route_message_mo_info ");
				sql2.append("WHERE ACCOUNT_ID = ? ");  
				sql2.append("AND ID <= ? ");
				logger.info("delete report sql:"+sql2.toString());
				pstmt2 = conn.prepareStatement(sql2.toString());
				pstmt2.setString(1, accountId);
				pstmt2.setLong(2, max);
				int count = pstmt2.executeUpdate();
				conn.commit();
				logger.info("{}删除MO ID<={}，共{}条",accountId,max,count);
			}
				
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e1);
			}
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, conn);
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return list;
	}
}
