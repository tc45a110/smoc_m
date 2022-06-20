package com.business.statistics.message.access;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.vo.BusinessRouteValue;
public  class AccessBusinessDao {
	private static final Logger logger = LoggerFactory.getLogger(AccessBusinessDao.class);
	/**mt文件入库
	 * @param map
	 */	
	public static void insertMtLog(List<BusinessRouteValue> businessRouteValues,String tableName) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("insert into smoc_route.").append(tableName);
		sql.append("(ACCOUNT_ID,PHONE_NUMBER,CARRIER,AREA_NAME,SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_ID,TEMPLATE_ID,");
		sql.append("ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,MESSAGE_INDEX,BUSINESS_MESSAGE_ID,MESSAGE_TOTAL,CREATED_TIME)");
		sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())");
		logger.info("sql={}",sql.toString());
				
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			
		   for(BusinessRouteValue businessRouteValue : businessRouteValues) {
		
			pstmt.setString(1,businessRouteValue.getAccountID());
			
			pstmt.setString(2,businessRouteValue.getPhoneNumber());
			
			pstmt.setString(3,businessRouteValue.getBusinessCarrier());
		
			pstmt.setString(4,businessRouteValue.getAreaName());
			
			pstmt.setString(5,businessRouteValue.getAccountSubmitTime());
			
			pstmt.setString(6,businessRouteValue.getMessageContent());
		
			pstmt.setString(7,businessRouteValue.getAccountMessageIDs());
		
			pstmt.setString(8,businessRouteValue.getChannelTemplateID());
			
			pstmt.setString(9,businessRouteValue.getAccountSubmitSRCID());
			
			pstmt.setString(10,businessRouteValue.getAccountBusinessCode());
			
			pstmt.setInt(11,businessRouteValue.getChannelIndex());	
						
			pstmt.setString(12,businessRouteValue.getBusinessMessageID());
		
			pstmt.setInt(13,businessRouteValue.getChannelTotal());
			pstmt.addBatch();
			
			}

			pstmt.executeBatch();	
		    conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
	}
	
	/** 通过mr中businessMessageId字段修改表enterprise_message_mr_info_中的数据
	 * @param map
	 */	
	public static void updateMtLog(BusinessRouteValue businessRouteValue,String tableName) {	
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt =null;
		PreparedStatement pstmt2 =null;
		ResultSet rs = null;
		try {		
		conn = LavenderDBSingleton.getInstance().getConnection();
		conn.setAutoCommit(false);		
		String OldStatusCodeExtend=null;		
		
		sql.append("SELECT STATUS_CODE_EXTEND FROM smoc_route.").append(tableName);
		sql.append(" where BUSINESS_MESSAGE_ID=?");
		pstmt = conn.prepareStatement(sql.toString());	
		pstmt.setString(1, businessRouteValue.getBusinessMessageID());
		logger.info("sql={}",sql.toString());
		rs = pstmt.executeQuery();		
		while (rs.next()) {				
			OldStatusCodeExtend =rs.getString("STATUS_CODE_EXTEND");	
			
		}
				
		sql.setLength(0);	
		
		sql.append("update smoc_route.").append(tableName);
		sql.append(" set STATUS_CODE=?,REPORT_TIME=?,UPDATED_TIME=?,STATUS_CODE_EXTEND=CONCAT_WS(',',?,?),MESSAGE_INDEX=?,MESSAGE_TOTAL=? ");
		sql.append("where BUSINESS_MESSAGE_ID=?");
		logger.info("sql={}",sql.toString());   
					
		pstmt2  =conn.prepareStatement(sql.toString());			
		pstmt2.setString(1,businessRouteValue.getStatusCode());
		pstmt2.setString(2,businessRouteValue.getChannelReportTime());		
		Date date = new  Date(); 		
	    pstmt2.setTimestamp(3,new Timestamp(date.getTime()));
	    //追加状态码		
		pstmt2.setString(4,OldStatusCodeExtend);
		
		String NewStatusCodeExtend = new StringBuilder().append(businessRouteValue.getMessageIndex()).append("=").append(businessRouteValue.getStatusCode()).toString();	
	
		pstmt2.setString(5,NewStatusCodeExtend);
		pstmt2.setInt(6,businessRouteValue.getMessageIndex());
		pstmt2.setInt(7,businessRouteValue.getMessageTotal());		
		pstmt2.setString(8,businessRouteValue.getBusinessMessageID());				
        pstmt2 .executeUpdate();
		 
		conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt , conn);		
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2 , conn);
		}
	}
	
	
	/**根据接入业务层mr修改message_web_task_info中的数据
	 * @param businessRouteValue
	 */
	public static void updateMessageWebTaskInfo(BusinessRouteValue businessRouteValue) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		try {		
		conn = LavenderDBSingleton.getInstance().getConnection();
		conn.setAutoCommit(false);		
							
		sql.append("update smoc.message_web_task_info set UPDATED_TIME=now(),");
		if("0".equals(businessRouteValue.getSuccessCode())) {
		sql.append("SUCCESS_SEND_NUMBER=ifnull(SUCCESS_SEND_NUMBER,0)+1 ");
		}
		if("2".equals(businessRouteValue.getSuccessCode())) {
		sql.append("FAILURE_NUMBER=ifnull(FAILURE_NUMBER,0)+1 ");
		}
		sql.append("where ID=?");
		logger.info("sql={}",sql.toString());   
					
		pstmt =conn.prepareStatement(sql.toString());			
		pstmt.setString(1,businessRouteValue.getAccountMessageIDs());					
        pstmt.executeUpdate();
		 
		conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {		
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt , conn);
		}
		
	}
	
	
	/**根据接入业务层mr修改message_https_task_info中的数据
	 * @param businessRouteValue
	 */
	public static void updateMessageHttpsTaskInfo(BusinessRouteValue businessRouteValue) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		try {		
		conn = LavenderDBSingleton.getInstance().getConnection();
		conn.setAutoCommit(false);		
							
		sql.append("update smoc.message_https_task_info set UPDATED_TIME=now(),");
		if("0".equals(businessRouteValue.getSuccessCode())) {
		sql.append("SUCCESS_SEND_NUMBER=ifnull(SUCCESS_SEND_NUMBER,0)+1 ");
		}
		if("2".equals(businessRouteValue.getSuccessCode())) {
		sql.append("FAILURE_NUMBER=ifnull(FAILURE_NUMBER,0)+1 ");
		}
		sql.append("where ID=?");
		logger.info("sql={}",sql.toString());   
					
		pstmt =conn.prepareStatement(sql.toString());			
		pstmt.setString(1,businessRouteValue.getAccountMessageIDs());					
        pstmt.executeUpdate();
		 
		conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {		
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt , conn);
		}
		
	}
	
	
	
}
