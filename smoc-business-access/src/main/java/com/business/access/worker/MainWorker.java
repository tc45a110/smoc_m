/**
 * 获取下发数据主线程
 */
package com.business.access.worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.manager.BusinessWorkerManager;

public class MainWorker extends SuperQueueWorker<BusinessRouteValue>{

	@Override
	public void doRun() throws Exception {
		long startTime = System.currentTimeMillis();
		int messageLoadMaxNumber = BusinessDataManager.getInstance().getMessageLoadMaxNumber();
		
		if(BusinessWorkerManager.getInstance().getSize() < messageLoadMaxNumber * 10){
			List<BusinessRouteValue> businessRouteValueList = loadRouteMessageMtInfo(messageLoadMaxNumber);
			if(businessRouteValueList != null && businessRouteValueList.size() > 0 ){
				long interval = System.currentTimeMillis() - startTime;
				logger.info("本次加载数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
				startTime = System.currentTimeMillis();
				for(BusinessRouteValue businessRouteValue : businessRouteValueList){
					logger.info(
							new StringBuilder().append("分发数据")
							.append("{}accountID={}")
							.append("{}phoneNumber={}")
							.append("{}messageContent={}")
							.toString(),
							FixedConstant.SPLICER,businessRouteValue.getAccountID(),
							FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
							FixedConstant.SPLICER,businessRouteValue.getMessageContent()
							);

					
					BusinessWorkerManager.getInstance().process(businessRouteValue);
				}
				interval = System.currentTimeMillis() - startTime;
				logger.info("本次分发数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
			}
		}
		
		//每次加载都需按照时间暂停，避免缓存数量过大，造成内存溢出	
		long interval = BusinessDataManager.getInstance().getMessageLoadIntervalTime() - (System.currentTimeMillis() - startTime);
		if(interval > 0 ){
			Thread.sleep(interval);
		}
		
	}
	
	private List<BusinessRouteValue> loadRouteMessageMtInfo(int messageLoadMaxNumber){
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		
		ResultSet rs = null;
		
		sql.append("SELECT ID,ACCOUNT_ID,PHONE_NUMBER,SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_FORMAT,MESSAGE_ID,TEMPLATE_ID,PROTOCOL,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,");
		sql.append("PHONE_NUMBER_NUMBER,MESSAGE_CONTENT_NUMBER,REPORT_FLAG,OPTION_PARAM,CREATED_TIME ");
		sql.append("FROM smoc_route.route_message_mt_info ");
		sql.append("ORDER BY ID ASC LIMIT 0,");
		sql.append(messageLoadMaxNumber);
		List<BusinessRouteValue> businessRouteValueList = new ArrayList<BusinessRouteValue>();
		BusinessRouteValue businessRouteValue = null;
		ArrayList<Long> ids = new ArrayList<Long>();
		long maxID = 0L;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				businessRouteValue = new BusinessRouteValue();
				maxID = rs.getLong("ID");
				ids.add(maxID);
				String accountID = StringUtils.defaultString(rs.getString("ACCOUNT_ID"));
				String phoneNumber = StringUtils.defaultString(rs.getString("PHONE_NUMBER"));
				String accountSubmitTime = StringUtils.defaultString(rs.getString("SUBMIT_TIME"));
				String messageContent = StringUtils.defaultString(rs.getString("MESSAGE_CONTENT"));
				String messageFormat = StringUtils.defaultString(rs.getString("MESSAGE_FORMAT"));
				String accountMessageIDs = StringUtils.defaultString(rs.getString("MESSAGE_ID"));
				String accountTemplateID = StringUtils.defaultString(rs.getString("TEMPLATE_ID"));
				String protocol = StringUtils.defaultString(rs.getString("PROTOCOL"));
				String accountSubmitSRCID = StringUtils.defaultString(rs.getString("ACCOUNT_SRC_ID"));
				String accountBusinessCode = StringUtils.defaultString(rs.getString("ACCOUNT_BUSINESS_CODE"));
				int taskPhoneNumberNumber = rs.getInt("PHONE_NUMBER_NUMBER");
				int taskMessageNumber = rs.getInt("MESSAGE_CONTENT_NUMBER");
				int accountReportFlag = rs.getInt("REPORT_FLAG");
				String optionParam = StringUtils.defaultString(rs.getString("OPTION_PARAM"));
				
				Date createdTime = rs.getTimestamp("CREATED_TIME");
				if(createdTime == null) {
					createdTime = new Date();
				}
				
				accountSubmitTime = DateUtil.format(createdTime, DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI);
				int messageNumber = MessageContentUtil.splitSMSContentNumber(messageContent);
				
				businessRouteValue.setRouteLabel(FixedConstant.RouteLable.MT.toString());
				businessRouteValue.setAccountID(accountID);
				businessRouteValue.setPhoneNumber(phoneNumber);
				businessRouteValue.setAccountSubmitTime(accountSubmitTime);
				businessRouteValue.setMessageContent(messageContent);
				businessRouteValue.setMessageFormat(messageFormat);
				businessRouteValue.setAccountMessageIDs(accountMessageIDs);
				businessRouteValue.setAccountTemplateID(accountTemplateID);
				businessRouteValue.setProtocol(protocol);
				businessRouteValue.setAccountSubmitSRCID(accountSubmitSRCID);
				businessRouteValue.setAccountBusinessCode(accountBusinessCode);
				businessRouteValue.setTaskPhoneNumberNumber(taskPhoneNumberNumber);
				businessRouteValue.setTaskMessageNumber(taskMessageNumber);
				businessRouteValue.setAccountReportFlag(accountReportFlag);
				//当optionParam为空时，赋值空串
				businessRouteValue.setOptionParam(StringUtils.isNotEmpty(optionParam)?MessageContentUtil.handlingLineBreakCommas(optionParam):"");
				businessRouteValue.setMessageTotal(messageNumber);
				businessRouteValue.setMessageNumber(String.valueOf(messageNumber));
				
				businessRouteValueList.add(businessRouteValue);
			}
			
			if(ids.size() > 0) {
				sql.setLength(0);
				sql.append("DELETE FROM smoc_route.route_message_mt_info WHERE ID in (");
				for(int i = 0;i < ids.size();i++) {
					sql.append(ids.get(i));
					if(i != (ids.size() -1)) {
						sql.append(",");
					}
				}
				sql.append(")");
				pstmt2 = conn.prepareStatement(sql.toString());
				pstmt2.execute();
				logger.info("删除ROUTE_MESSAGE_MT_INFO 共{}条",ids.size());
			}
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e);
			}
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, null);
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return businessRouteValueList;
	}
	
}


