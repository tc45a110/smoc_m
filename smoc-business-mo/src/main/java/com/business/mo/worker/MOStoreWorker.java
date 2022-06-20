/**
 * @desc
 * 
 */
package com.business.mo.worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.MOBusinessLogManager;
import com.base.common.manager.AccountInfoManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.util.UUIDUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;

/**
 * 保存1.匹配到下行记录2.上行扩展码完整匹配3.指定通道模糊匹配扩展码;2和3需要通过客户接入码和通道扩展码生成accountSubmitSRCID
 */
public class MOStoreWorker extends SuperQueueWorker<BusinessRouteValue>{

	@Override
	public void doRun() throws Exception {

		if(superQueue.size() > 0){
			long startTime = System.currentTimeMillis();
			//临时数据
			List<BusinessRouteValue> businessRouteValueList;
			synchronized (lock) {
				businessRouteValueList = new ArrayList<BusinessRouteValue>(superQueue);
				superQueue.clear();
			}
			//对businessRouteValueList进行判断1.匹配到提交信息需保存smoc_route.route_message_mo_info和smoc.message_mo_info
			//2.未匹配提交信息但是过了匹配周期，需将上行信息保存到message_mo_info
			saveMessageMoInfo(businessRouteValueList);	
			long interval = System.currentTimeMillis() - startTime;
			logger.info("上行保存数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
			
			List<BusinessRouteValue> matchBusinessRouteValueList = new ArrayList<BusinessRouteValue>();
			
			for(BusinessRouteValue businessRouteValue : businessRouteValueList) {
				//记录日志
				StringBuilder sb = new StringBuilder();
				sb.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI))
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getChannelID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getPhoneNumber())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(businessRouteValue.getChannelMOSRCID())
				.append(FixedConstant.LOG_SEPARATOR)
				.append(MessageContentUtil.handlingLineBreakCommas(businessRouteValue.getMOContent()));
					
				if(StringUtils.isNotEmpty(businessRouteValue.getAccountID())) {
					//accountID不为空  则匹配上
					matchBusinessRouteValueList.add(businessRouteValue);
					sb.append(FixedConstant.LOG_SEPARATOR)
					.append(MessageContentUtil.handlingLineBreakCommas(businessRouteValue.getAccountID()))
					.append(FixedConstant.LOG_SEPARATOR)
					.append(MessageContentUtil.handlingLineBreakCommas(MessageContentUtil.handlingLineBreakCommas(businessRouteValue.getMessageContent())));
	
				}
				sb.append(System.getProperty("line.separator"));
				MOBusinessLogManager.getInstance().add(sb.toString());
			}
			if(CollectionUtils.isNotEmpty(matchBusinessRouteValueList)){
				saveRouteMessageMoInfo(matchBusinessRouteValueList);
				interval = System.currentTimeMillis() - startTime;
				logger.info("下行匹配成功的上行保存数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
			}

		}else{
			//当没有数据时，需要暂停一会
			long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
			Thread.sleep(interval);
		}
	
	}
	
	/**
	 * 保存上行数据
	 * @param businessRouteValueList
	 * @param tableName
	 */
	private void saveRouteMessageMoInfo(
			List<BusinessRouteValue> businessRouteValueList) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.route_message_mo_info");
		sql.append(" (ACCOUNT_ID,PHONE_NUMBER,MO_TIME,MESSAGE_CONTENT,MESSAGE_FORMAT,MESSAGE_ID,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,OPTION_PARAM,CREATED_TIME) ");
		sql.append("values(");
		sql.append("?,?,?,?,?,?,?,?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (BusinessRouteValue businessRouteValue : businessRouteValueList) {
				pstmt.setString(1, businessRouteValue.getAccountID());
				pstmt.setString(2, businessRouteValue.getPhoneNumber());
				pstmt.setString(3, businessRouteValue.getChannelReportTime());
				pstmt.setString(4, businessRouteValue.getMOContent());
				pstmt.setString(5, businessRouteValue.getMessageFormat());
				pstmt.setString(6, businessRouteValue.getAccountMessageIDs());
				pstmt.setString(7, businessRouteValue.getAccountSubmitSRCID());
				pstmt.setString(8, businessRouteValue.getAccountBusinessCode());
				pstmt.setString(9, businessRouteValue.getOptionParam());

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
	 *  保存上行信息任务单 供页面展示
	 * @param businessRouteValueList
	 */
	private void saveMessageMoInfo(List<BusinessRouteValue> businessRouteValueList) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc.message_mo_info ");
		sql.append("(ID,ENTERPRISE_ID,ACCOUNT_ID,CHANNEL_ID,CHANNEL_SRC_ID,MO_SRC_ID,BUSINESS_CODE,BUSINESS_TYPE,INFO_TYPE,MOBILE,TASK_ID,WEB_TEMPLATE_ID");
		sql.append(",MO_MESSAGE_CONTENT,MO_DATE,MT_MESSAGE_CONTENT,MT_DATE,CARRIER,AREA,ACCOUNT_SRC_ID,CREATED_TIME) ");
		sql.append(" VALUES(?,?,?,?,? ,?,?,?,?,? ,?,?,?,STR_TO_DATE(?,\"%Y-%m-%d %T %f\"),? ,STR_TO_DATE(?,\"%Y-%m-%d %T %f\"),?,?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (BusinessRouteValue businessRouteValue : businessRouteValueList) {
				pstmt.setString(1, UUIDUtil.get32UUID());
				pstmt.setString(2, AccountInfoManager.getInstance().getEnterpriseID(businessRouteValue.getAccountID()));
				pstmt.setString(3, businessRouteValue.getAccountID());
				pstmt.setString(4, businessRouteValue.getChannelID());
				pstmt.setString(5, businessRouteValue.getChannelSRCID());
				
				pstmt.setString(6, businessRouteValue.getChannelMOSRCID());
				pstmt.setString(7, businessRouteValue.getAccountBusinessCode());
				pstmt.setString(8, businessRouteValue.getBusinessType());
				pstmt.setString(9, businessRouteValue.getInfoType());
				
				pstmt.setString(10, businessRouteValue.getPhoneNumber());
				pstmt.setString(11, businessRouteValue.getAccountTaskID());
				pstmt.setString(12, businessRouteValue.getAccountTemplateID());
				pstmt.setString(13, businessRouteValue.getMOContent());
				pstmt.setString(14, businessRouteValue.getChannelReportTime());
				pstmt.setString(15, businessRouteValue.getMessageContent());
				pstmt.setString(16, businessRouteValue.getChannelSubmitTime());
				pstmt.setString(17, businessRouteValue.getBusinessCarrier());
				
				pstmt.setString(18, businessRouteValue.getAreaName());
				pstmt.setString(19, businessRouteValue.getAccountSubmitSRCID());

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


