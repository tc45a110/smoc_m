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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.manager.BusinessWorkerManager;
import com.business.access.manager.InsideFilterWorkerManager;

public class MainWorker extends SuperQueueWorker<BusinessRouteValue>{

	@Override
	public void doRun() throws Exception {
		long startTime = System.currentTimeMillis();
		long costTime = 0;

		int messageLoadMaxNumber = BusinessDataManager.getInstance().getMessageLoadMaxNumber();
		//业务数据缓存队列数量小于阈值和内部过滤缓存队列数量小于阈值
		if(isBelowCacheThreshold(messageLoadMaxNumber) && InsideFilterWorkerManager.getInstance().isBelowCacheThreshold()){
			List<BusinessRouteValue> businessRouteValueList = loadRouteMessageMtInfo(messageLoadMaxNumber);
			if(CollectionUtils.isNotEmpty(businessRouteValueList)){
				costTime = System.currentTimeMillis() - startTime;
				logger.info("本次加载数据条数{},耗时{}毫秒",businessRouteValueList.size(),costTime);

				for(BusinessRouteValue businessRouteValue : businessRouteValueList){
					logger.debug(
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
				costTime = System.currentTimeMillis() - startTime;
				logger.info("本次加载及分发数据条数{},耗时{}毫秒",businessRouteValueList.size(),costTime);
			}
		}

		//每次加载都需按照时间暂停，避免缓存数量过大，造成内存溢出	
		controlSubmitSpeed(BusinessDataManager.getInstance().getMessageLoadIntervalTime(),costTime);
	}
	
	/**
	 * 判断缓存是否低于阈值
	 * @param messageLoadMaxNumber
	 * @return
	 */
	private boolean isBelowCacheThreshold(int messageLoadMaxNumber){
		//业务数据缓存队列数量与单次加载条数的倍数
		int businessCacheSizeMultiple = ResourceManager.getInstance().getIntValue("business.cache.size.multiple");
		if(businessCacheSizeMultiple == 0){
			businessCacheSizeMultiple = 10;
		}
		int size = BusinessWorkerManager.getInstance().getSize();
		int threshold = messageLoadMaxNumber*businessCacheSizeMultiple;
		if( size < threshold){
			return true;
		}
		logger.warn("业务数据缓存队列数量{},超过阈值{}",size,threshold);
		return false;
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
		ArrayList<Long> idList = new ArrayList<Long>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				businessRouteValue = new BusinessRouteValue();
				idList.add(rs.getLong("ID"));
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
			
			if(idList.size() > 0) {
				sql.setLength(0);
				sql.append("DELETE FROM smoc_route.route_message_mt_info ");
				sql.append(" WHERE ");
				sql.append("  ID in ( ");
				for(int i = 0;i < idList.size();i++) {
					sql.append(idList.get(i));
					if(i != (idList.size() -1)) {
						sql.append(",");
					}
				}
				sql.append(")");
				pstmt2 = conn.prepareStatement(sql.toString());
				pstmt2.execute();
			}
			conn.commit();
			if(idList.size() > 0) {
				logger.info("删除smoc_route.route_message_mt_info共{}条",idList.size());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e);
			}
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, null);
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return businessRouteValueList;
	}
	
}


