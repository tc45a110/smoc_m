/**
 * @desc
 * 
 */
package com.base.common.constant;

public class TableNameConstant {
	//通道级下行数据临时表名前缀
	public static String CHANNEL_MESSAGE_MT_INFO_TABLE_NAME_PREFIX ="route_channel_message_mt_info_";
	//通道码号级下行数据临时表名前缀
	public static String CHANNEL_SRCID_MESSAGE_MT_INFO_TABLE_NAME_PREFIX ="route_channel_srcid_message_mt_info_";
	//企业级状态报告表名前缀
	public static String ENTERPRISE_MESSAGE_MR_INFO_TABLE_NAME_PREFIX ="enterprise_message_mr_info_";
	
	//存储过程名称：创建企业级状态报告表
	public static String PROCEDURE_CREATE_ENTERPRISE_MESSAGE_MR_INFO ="create_enterprise_message_mr_info";
	//存储过程名称：创建通道级下发信息表
	public static String PROCEDURE_CREATE_ROUTE_CHANNEL_MESSAGE_MT_INFO ="create_route_channel_message_mt_info";
	//存储过程名称：创建通道码号级下发信息表
	public static String PROCEDURE_CREATE_ROUTE_CHANNEL_SRCID_MESSAGE_MT_INFO ="create_route_channel_srcid_message_mt_info";
	
	//存储过程名称：创建天表代理业务下发及回执信息表
	public static String PROCEDURE_CREATE_PROXY_MESSAGE_INFO ="create_proxy_message_info";
	
	//存储过程名称：删除天表代理业务下发及回执信息表
	public static String PROCEDURE_DROP_PROXY_MESSAGE_INFO ="drop_route_table";
	
}


