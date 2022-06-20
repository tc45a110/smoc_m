/**
 * @desc
 * 
 */
package com.base.common.constant;

import java.util.HashMap;
import java.util.Map;

import com.base.common.manager.BusinessDataManager;

public class LogPathConstant {
	//日志基础路径
	public static String LOG_BASE_PATH = BusinessDataManager.getInstance().getLogBasePath();
	//本机标识
	public static String LOCALHOST_IP = BusinessDataManager.getInstance().getHostID();
	
	//下行、状态报告、上行、计费日志文件名前缀
	public static String LOG_FILENAME_PREFIX_MT = "mt.";
	public static String LOG_FILENAME_PREFIX_MR = "mr.";
	public static String LOG_FILENAME_PREFIX_MO = "mo.";
	
	//接入协议层日志路径
	//public static String ACCESS_PROTOCOL_MT_LOG_PATH = "access/protocol/mt";
	//public static String ACCESS_PROTOCOL_MR_LOG_PATH = "access/protocol/mr";
	
	//接入业务层日志路径
	private static String ACCESS_BUSINESS_MT_LOG_PATH = "business/access/mt";
	private static String ACCESS_BUSINESS_MR_LOG_PATH = "business/access/mr";
	
	//代理业务层日志路径
	private static String PROXY_BUSINESS_MT_LOG_PATH = "business/proxy/mt";
	private static String PROXY_BUSINESS_MR_LOG_PATH = "business/proxy/mr";
	
	//业务层上行日志
	public static String BUSINESS_MO_LOG_PATH = "business/mo";
	
	//代理协议层日志路径
	//public static String PROXY_PROTOCOL_MT_LOG_PATH = "proxy/protocol/mt";
	//public static String PROXY_PROTOCOL_MR_LOG_PATH = "proxy/protocol/mr";
	
	//文件前缀集合:lable-文件前缀
	private static Map<String,String> fileNamePrefixMap = new HashMap<String, String>();
	static{
		fileNamePrefixMap.put(FixedConstant.RouteLable.MT.name(), LOG_FILENAME_PREFIX_MT);
		fileNamePrefixMap.put(FixedConstant.RouteLable.MR.name(), LOG_FILENAME_PREFIX_MR);
		fileNamePrefixMap.put(FixedConstant.RouteLable.MO.name(), LOG_FILENAME_PREFIX_MO);
	}
	
	//接入协议层文件路径集合:lable-文件路径
//	private static Map<String,String> accessProtocolFilePathPartMap = new HashMap<String, String>();
//	static{
//		accessProtocolFilePathPartMap.put(FixedConstant.RouteLable.MT.name(), ACCESS_PROTOCOL_MT_LOG_PATH);
//		accessProtocolFilePathPartMap.put(FixedConstant.RouteLable.MR.name(), ACCESS_PROTOCOL_MR_LOG_PATH);
//	}
	
	//接入业务层文件路径集合:lable-文件路径
	private static Map<String,String> accessBusinessFilePathPartMap = new HashMap<String, String>();
	static{
		accessBusinessFilePathPartMap.put(FixedConstant.RouteLable.MT.name(), ACCESS_BUSINESS_MT_LOG_PATH);
		accessBusinessFilePathPartMap.put(FixedConstant.RouteLable.MR.name(), ACCESS_BUSINESS_MR_LOG_PATH);
	}
	
	//代理业务层文件路径集合:lable-文件路径
	private static Map<String,String> proxyBusinessFilePathPartMap = new HashMap<String, String>();
	static{
		proxyBusinessFilePathPartMap.put(FixedConstant.RouteLable.MT.name(), PROXY_BUSINESS_MT_LOG_PATH);
		proxyBusinessFilePathPartMap.put(FixedConstant.RouteLable.MR.name(), PROXY_BUSINESS_MR_LOG_PATH);
	}
	
	public static String getFileNamePrefix(String lable){
		return fileNamePrefixMap.get(lable);
	}
	
//	public static String getAccessProtocolFilePathPart(String lable){
//		return accessProtocolFilePathPartMap.get(lable);
//	}
	
	public static String getAccessBusinessFilePathPart(String lable){
		return accessBusinessFilePathPartMap.get(lable);
	}
	
	public static String getProxyBusinessFilePathPart(String lable){
		return proxyBusinessFilePathPartMap.get(lable);
	}
	
}


