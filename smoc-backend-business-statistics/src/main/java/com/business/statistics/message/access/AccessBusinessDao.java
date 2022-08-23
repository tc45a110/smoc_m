package com.business.statistics.message.access;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.dao.LavenderDBSingleton;
import com.base.common.vo.BusinessRouteValue;

public class AccessBusinessDao {
	private static final Logger logger = LoggerFactory.getLogger(AccessBusinessDao.class);

	/**
	 * mt文件入库
	 * 
	 * @param map
	 */
	public static void insertMtLog(List<BusinessRouteValue> businessRouteValues, String tableName) {

		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		sql.append("insert into smoc_route.").append(tableName);
		sql.append("(ACCOUNT_ID,PHONE_NUMBER,CARRIER,AREA_NAME,SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_ID,TEMPLATE_ID,");
		sql.append(
				"ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,MESSAGE_INDEX,BUSINESS_MESSAGE_ID,MESSAGE_TOTAL,SUBMIT_STYLE,SIGN,CREATED_TIME)");
		sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())");

		try {
			int num = 0;
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (BusinessRouteValue businessRouteValue : businessRouteValues) {
				num++;
				pstmt.setString(1, businessRouteValue.getAccountID());

				pstmt.setString(2, businessRouteValue.getPhoneNumber());

				pstmt.setString(3, businessRouteValue.getBusinessCarrier());

				pstmt.setString(4, businessRouteValue.getAreaName());

				pstmt.setString(5, businessRouteValue.getAccountSubmitTime());

				pstmt.setString(6, businessRouteValue.getMessageContent());

				if (businessRouteValue.getAccountMessageIDs().contains("&")) {
					pstmt.setString(7, businessRouteValue.getAccountMessageIDs()
							.substring(businessRouteValue.getAccountMessageIDs().lastIndexOf("&") + 1));
				} else {
					pstmt.setString(7, businessRouteValue.getAccountMessageIDs());
				}
				pstmt.setString(8, businessRouteValue.getChannelTemplateID());

				pstmt.setString(9, businessRouteValue.getAccountSubmitSRCID());

				pstmt.setString(10, businessRouteValue.getAccountBusinessCode());

				pstmt.setInt(11, businessRouteValue.getChannelIndex());

				pstmt.setString(12, businessRouteValue.getBusinessMessageID());

				pstmt.setInt(13, businessRouteValue.getChannelTotal());

				pstmt.setString(14, businessRouteValue.getMessageFormat());

				pstmt.setString(15, businessRouteValue.getMessageSignature());

				pstmt.addBatch();
				
				if (num % 10000 == 0) {
					
					pstmt.executeBatch();
				}

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
	 * 通过mr中businessMessageId字段修改表enterprise_message_mr_info_中的数据
	 * 
	 * @param map
	 */
	public static void updateMtLog(List<BusinessRouteValue> businessRouteValues, String tableName) {

		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			int num = 0;
			
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);

			sql.append("update smoc_route.").append(tableName);
			sql.append(" set STATUS_CODE=?,MESSAGE_INDEX=?,STATUS_CODE_EXTEND=CONCAT_WS(',',STATUS_CODE_EXTEND,?),REPORT_TIME=?,TIME_ELAPSED=? ");
			sql.append("where BUSINESS_MESSAGE_ID=?");
			pstmt = conn.prepareStatement(sql.toString());

			for (BusinessRouteValue businessRouteValue : businessRouteValues) {
				num++;
				pstmt.setString(1, businessRouteValue.getStatusCode());
				pstmt.setInt(2, businessRouteValue.getMessageIndex());
				// 追加状态码
				String NewStatusCodeExtend =new StringBuilder().append(businessRouteValue.getMessageIndex())
						.append("=").append(businessRouteValue.getStatusCode()).toString();
				pstmt.setString(3, NewStatusCodeExtend);
				pstmt.setString(4, businessRouteValue.getChannelReportTime());
				pstmt.setInt(5, businessRouteValue.getRepairTime());
				pstmt.setString(6, businessRouteValue.getBusinessMessageID());

				pstmt.addBatch();
				if (num % 10000 == 0) {					
					pstmt.executeBatch();
				}
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
	 * 根据接入业务层mr修改message_web_task_info中的数据
	 * 
	 * @param businessRouteValue
	 */
	public static void updateMessageWebTaskInfo(List<MessageTask> messageTasklist) {
		int num = 0;
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		

		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			sql.append("update smoc.message_web_task_info set SUCCESS_SEND_NUMBER=ifnull(SUCCESS_SEND_NUMBER,0)+?,");
			sql.append("FAILURE_NUMBER=ifnull(FAILURE_NUMBER,0)+? where ID=?");

			pstmt = conn.prepareStatement(sql.toString());
			
			for (MessageTask messagetask : messageTasklist) {
				
				num++;
				
				pstmt.setInt(1, messagetask.getSuccessSendNumber());
				pstmt.setInt(2, messagetask.getFailureNumber());
				pstmt.setString(3, messagetask.getBatchNumber());

				pstmt.addBatch();
				if (num % 10000 == 0) {

					pstmt.executeBatch();
				}
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
	 * 根据接入业务层mr修改message_https_task_info中的数据
	 * 
	 * @param businessRouteValue
	 */
	public static void updateMessageHttpsTaskInfo(List<MessageTask> messageTasklist) {
		int num = 0;
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);

			sql.append("update smoc.message_https_task_info set SUCCESS_SEND_NUMBER=ifnull(SUCCESS_SEND_NUMBER,0)+?,");
			sql.append("FAILURE_NUMBER=ifnull(FAILURE_NUMBER,0)+? where ID=?");

			pstmt = conn.prepareStatement(sql.toString());

			for (MessageTask messagetask : messageTasklist) {
				num++;
				
				pstmt.setInt(1, messagetask.getSuccessSendNumber());
				pstmt.setInt(2, messagetask.getFailureNumber());
				pstmt.setString(3, messagetask.getBatchNumber());

				pstmt.addBatch();
				if (num % 10000 == 0) {
					pstmt.executeBatch();
				}
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
