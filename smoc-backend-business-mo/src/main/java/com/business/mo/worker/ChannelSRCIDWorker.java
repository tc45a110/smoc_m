/**
 * @desc
 * 
 */
package com.business.mo.worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.base.common.util.TableNameGeneratorUtil;
import com.base.common.util.UUIDUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.vo.ChannelMO;

public class ChannelSRCIDWorker implements Callable<BusinessRouteValue>{
	
	private static final Logger logger = LoggerFactory.getLogger(ChannelSRCIDWorker.class);
	
	private String tableName;
	private ChannelMO channelMO;
	
	public ChannelSRCIDWorker(ChannelMO channelMO) {
		this.channelMO = channelMO;
		this.tableName = TableNameGeneratorUtil.generateRouteChannelSRCIDMessageMTInfoTableName(channelMO.getChannelSRCID());
	}
	
	@Override
	public BusinessRouteValue call() throws Exception {
		return searchChannelMTByChannelMO(channelMO);
	}
	
	/**
	 * 通过通道码号级下行数据临时表名获取下发记录,手机号+上行扩展码需精准匹配
	 * @return
	 */
	private BusinessRouteValue searchChannelMTByChannelMO(ChannelMO channelMO){
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PHONE_NUMBER,MESSAGE_CONTENT,CHANNEL_ID,CHANNEL_MO_SRCID,ACCOUNT_ID,BUSINESS_CARRIER,CHANNEL_SUBMIT_TIME,AREA_NAME,  ");
		sql.append("ACCOUNT_BUSINESS_CODE,ACCOUNT_SUBMIT_SRCID,INFO_TYPE,ACCOUNT_TASK_ID,CHANNEL_SRCID,");
		sql.append("BUSINESS_TYPE,ACCOUNT_TEMPLATE_ID,OPTION_PARAM FROM ");
		sql.append("smoc_route.").append(tableName);
		sql.append(" where PHONE_NUMBER = ? and CHANNEL_MO_SRCID = ?");
		sql.append(" ORDER BY ID DESC");
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		BusinessRouteValue businessRouteValue = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, channelMO.getPhoneNumber());
			pstmt.setString(2, channelMO.getChannelMOSRCID());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				//当通道也能匹配上立即返回，当通道匹配不上
				String channelID = rs.getString("CHANNEL_ID");
				if(businessRouteValue != null && !channelID.equals(channelMO.getChannelID())){
					continue;
				}
				businessRouteValue = new BusinessRouteValue();
				businessRouteValue.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				businessRouteValue.setMessageContent(rs.getString("MESSAGE_CONTENT"));
				businessRouteValue.setChannelID(rs.getString("CHANNEL_ID"));
				businessRouteValue.setChannelSubmitSRCID(rs.getString("CHANNEL_MO_SRCID"));
				businessRouteValue.setAccountID(rs.getString("ACCOUNT_ID"));
				businessRouteValue.setBusinessCarrier(rs.getString("BUSINESS_CARRIER"));
				businessRouteValue.setChannelSubmitTime(rs.getString("CHANNEL_SUBMIT_TIME"));
				businessRouteValue.setAreaName(rs.getString("AREA_NAME"));
				businessRouteValue.setAccountBusinessCode(rs.getString("ACCOUNT_BUSINESS_CODE"));
				                   
				businessRouteValue.setAccountSubmitSRCID(rs.getString("ACCOUNT_SUBMIT_SRCID"));
				businessRouteValue.setInfoType(rs.getString("INFO_TYPE"));
				businessRouteValue.setAccountTaskID(rs.getString("ACCOUNT_TASK_ID"));
				businessRouteValue.setChannelSRCID(rs.getString("CHANNEL_SRCID"));
				businessRouteValue.setBusinessType(rs.getString("BUSINESS_TYPE"));
				businessRouteValue.setAccountTemplateID(rs.getString("ACCOUNT_TEMPLATE_ID"));
				businessRouteValue.setOptionParam(rs.getString("OPTION_PARAM"));
				
				businessRouteValue.setChannelReportTime(channelMO.getMOTime());
				businessRouteValue.setMOContent(channelMO.getMessageContent());
				businessRouteValue.setChannelMOSRCID(channelMO.getChannelMOSRCID());
				businessRouteValue.setBusinessMessageID(UUIDUtil.get32UUID());
				logger.info("下行匹配{}{}{}{}",
						FixedConstant.SPLICER,channelMO.getChannelSRCID(),FixedConstant.SPLICER,businessRouteValue.toString());
				
				if(channelID.equals(channelMO.getChannelID())){
					break;
				}	
			}
			
			sql.setLength(0);
			//进行逻辑判断，当满足过期删除时，需要封装BusinessRouteValue进行返回
			int expirationTime = BusinessDataManager.getInstance().getChannelMOMatchExpirationTime();
			int intervalTime = DateUtil.getNowIntervalTime(channelMO.getMOTime(), DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE);
			//匹配上信息和超过过期时间 则进行删除
			if(businessRouteValue != null || (intervalTime > expirationTime && channelMO.getMatchTimes() > 0) ) {
				//根据ID删除记录
				sql.append("DELETE FROM smoc_route.route_channel_mo_info WHERE ID = ?");
				pstmt2 = conn.prepareStatement(sql.toString());
				pstmt2.setLong(1, channelMO.getID());
				pstmt2.executeUpdate();
				logger.info("删除ROUTE_CHANNEL_MO_INFO ID={}",channelMO.getID());
				//为空 则为过期的数据
				if(businessRouteValue == null) {
					businessRouteValue = new BusinessRouteValue();
					businessRouteValue.setChannelID(channelMO.getChannelID());
					businessRouteValue.setPhoneNumber(channelMO.getPhoneNumber());
					businessRouteValue.setMOContent(channelMO.getMessageContent());
					businessRouteValue.setChannelReportTime(channelMO.getMOTime());
					businessRouteValue.setChannelSRCID(channelMO.getChannelSRCID());
					businessRouteValue.setChannelMOSRCID(channelMO.getChannelMOSRCID());
				}
			}
			
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e.getMessage(), e1);
			}
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, null);
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return businessRouteValue;
	}

}


