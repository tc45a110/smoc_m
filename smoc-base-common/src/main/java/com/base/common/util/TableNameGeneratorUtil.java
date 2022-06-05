package com.base.common.util;

import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.TableNameConstant;


public class TableNameGeneratorUtil {
	
	/**
	 * 生成企业级状态报告表名
	 * @param enterpriseFlag
	 * @return
	 */
	public static final String generateEnterpriseMessageMRInfoTableName(String enterpriseFlag) {
		return new StringBuilder()
		.append(TableNameConstant.ENTERPRISE_MESSAGE_MR_INFO_TABLE_NAME_PREFIX)
		.append(enterpriseFlag.toLowerCase())
		.toString();
	}
	
	/**
	 * 生成通道级下行数据临时表名
	 * @param channelID
	 * @return
	 */
	public static final String generateRouteChannelMessageMTInfoTableName(String channelID) {
		return new StringBuilder()
		.append(TableNameConstant.CHANNEL_MESSAGE_MT_INFO_TABLE_NAME_PREFIX)
		.append(channelID.toLowerCase())
		.toString();
	}
	
	/**
	 * 生成通道码号级下行数据临时表名
	 * @param channelSRCID
	 * @return
	 */
	public static final String generateRouteChannelSRCIDMessageMTInfoTableName(String channelSRCID) {
		StringBuilder sb =  new StringBuilder(TableNameConstant.CHANNEL_SRCID_MESSAGE_MT_INFO_TABLE_NAME_PREFIX);	
		if(StringUtils.isNotEmpty(channelSRCID)){
			sb.append(channelSRCID);
		}
		return sb.toString();
	}
	
}
