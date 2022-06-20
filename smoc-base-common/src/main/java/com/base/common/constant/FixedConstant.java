/**
 * @desc
 * 固定常量，便于引用
 */
package com.base.common.constant;

public class FixedConstant {
	/**
	 * 系统数据之间的拼接符
	 */
	public final static String SPLICER ="&";
	
	public final static String REIDS_SPLICER =":";
	
	/**
	 * 日志字段之间的分隔符
	 */
	public final static String LOG_SEPARATOR =",";
	
	/**
	 * 数据库存储多个字段的分隔符
	 */
	public final static String DATABASE_SEPARATOR =",";
	
	/**
	 * 通用运行标识，可以通过更改该标识，停止线程运行。
	 */
	public static boolean COMMON_RUN_FLAG = true;
	
	/**
	 * 数据库账号密码加密的key
	 */
	public final static String ENCRYPT_KEY = "f38b5b64-c2ba-4e59-901c-5f0c1ec19fe4";
	
	/***
	 * 通用生效时间：一般指某个数据项增、删、改之后系统多长时间生效，如果黑名单、白名单、省地市号段、运营商号段、频次限制、签名限制等
	 */
	public final static long COMMON_EFFECTIVE_TIME = 60 * 1000;
	
	/**
	 * 通用间隔时间:一般指某项操作之后需要暂停的时间，如从redis拉取状态报告、给客户推送状态报告、数据保存的间隔时间
	 */
	public final static long COMMON_INTERVAL_TIME = 1000;
	
	/**
	 * 通用监控间隔时间:特指监控打印数据的间隔时间
	 */
	public final static long COMMON_MONITOR_INTERVAL_TIME = 60 * 1000;
	
	/**
	 * 失败补发错误码-不是成功就行补发
	 */
	public final static String FAIL_REPAIR_ALL_FAILED_CODE = "ALLFAILED";
	
	/**
	 * 失败补发错误码-不是成功就行补发-正则表达式
	 */
	public final static String REPAIR_ALL_FAILED_CODE_REGEXP = "^((?!DELIVRD).)*$";
	
	/**
	 * 服务器cpu数量
	 */
	public final static int CPU_NUMBER = Runtime.getRuntime().availableProcessors();
	
	/**
	 * 运营商
	 */
	public static enum Carrier {
		//移动,联通,电信,国际
		CMCC,UNIC,TELC,INTL
	}
	
	/**
	 * 是否支持携号转网标识
	 */
	public static enum TransferSupport{
		//不支持,支持
		UNSUPPORT,SUPPORT
	}
	
	/**
	 * 账号状态
	 */
	public static enum AccountStatus {
		//注销,正常,编辑,暂停
		CANCEL,NORMAL,EDIT,SUSPEND
	}
	
	/**
	 * 付费方式：预付，后付
	 */
	public static enum AccountPayType {
					//1-预付费,2-后付费
		PLACEHOLDER,PREPAYMENT,POSTPAID
	}
	
	/**
	 * 扣费方式:扣费时间点
	 */
	public static enum AccountConsumeType {
					//1-下发时扣费,2-回执时扣费
		PLACEHOLDER,SUBMIT,REPORT
	}
	
	/**
	 * 计费方式:以提交还是以回执
	 */
	public static enum AccountChargeType {
					//1-下发扣费,2-回执扣费
		PLACEHOLDER,SUBMIT,REPORT
	}
	
	/**
	 * 通道状态
	 */
	public static enum ChannelStatus {
		//正常,编辑,暂停
		NORMAL,EDIT,SUSPEND
	}
	
	/**
	 * 通道运行状态
	 */
	public static enum ChannelRunStatus {
					//正常,异常
		PLACEHOLDER,NORMAL,ABNORMAL
	}
	
	/**
	 * 进入审核队列或通道队列标识码
	 */
	public static enum ExecuteCheckCode {
		CHECK,CHANNEL
	}
	
	/**
	 * 状态报告来源
	 */
	public static enum StatusReportSource {
		//接入层,代理层,提交响应,通道返回
		ACCESS,PROXY,RESPONSE,CHANNEL
	}
	
	/**
	 * 缓存队列或key的名称
	 */
	public static enum MiddlewareCacheName {
		//提交，响应，代理层状态报告，接入层状态报告,携号转网,消息ID，上行锁,状态锁，审核锁
		SUBMIT,RESPONSE,PROXY_REPORT,ACCESS_REPORT,MNP,MESSAGE_ID,LOCK_MO,LOCK_REPORT,LOCK_AUDIT
	}
	
	/**
	 * 数据路由标签
	 */
	public static enum RouteLable {
		MT,MR,MO,FEE
	}
	
	/**
	 * 黑名单层级过滤
	 */
	public static enum BlackListLevelFiltering {
		NO,LOW,MIDDLE,HIGH
	}
	
	/**
	 * 通道的区域范围:全国、分省、国际
	 */
	public static enum BusinessAreaType{
		COUNTRY,PROVINCE,INTL
	}
	
	/**
	 * 计价方式;统一计价、区域计价
	 */
	public static enum PriceStyle{
		AREA_PRICE,UNIFIED_PRICE
	}
	
	/**
	 * 日限量方式
	 */
	public static enum SendLimitStyleDaily {
		//按客户提交限量(长短信算多条),按客户提交限量(长短信算1条),按状态成功限量(长短信算多条),按状态成功限量(长短信算1条)
		NO,MT_MESSAGE_CONTENT,MT_PHONE_NUMBER,RP_MESSAGE_CONTENT,RP_PHONE_NUMBER
	}
	
	/**
	 * 业务数据项分类
	 */
	public static enum BusinessDataCategory {
		CHANNEL,CHANNEL_FILTER,BUSINESS_ACCOUNT,BUSINESS_ACCOUNT_FILTER,SYSTEM_PARAM
	}
	
	//系统级业务项
	public static enum SystemItem {
		COMMON_EFFECTIVE_TIME,//通用
		WHITELIST_EFFECTIVE_TIME,//白名单
		BLACKLIST_EFFECTIVE_TIME,//黑名单
		CARRIER_SEGMENT_EFFECTIVE_TIME,//运营商号段 3-4位
		AREA_SEGMENT_EFFECTIVE_TIME,//地市号段7位
		MESSAGE_SAVE_INTERVAL_TIME,//数据保存间隔时间
		MESSAGE_LOAD_INTERVAL_TIME,//数据加载间隔时间
		MESSAGE_LOAD_MAX_NUMBER,//数据加载最大条数
		MAX_SMS_TEXT_LENGTH,//文本短信最大长度
		SESSION_MONITOR_INTERVAL_TIME,//client session监控间隔时间
		REPORT_REDIS_POP_INTERVAL_TIME,//接入平台状态报告拉取为空时间隔时间
		REDIS_POP_INTERVAL_TIME,//接入平台数据拉取为空时间隔时间
		REPORT_PUSH_INTERVAL_TIME,//状态报告推送间隔时间
		LOG_BASE_PATH,//业务日志根路径
		HOST_ID_TYPE,//主机标识类型IP或HOSTNAME
		REPORT_STORE_TO_REDIS_PROTOCOL,//状态报告存储到REDIS的接入协议
		REPORT_STORE_TO_DATABASE_PROTOCOL,//状态报告存储到DATABASE的接入协议
		RESPONSE_STORE_TO_REDIS_EXPIRATION_TIME,//响应信息存储到REDIS的有效期
		CHANNEL_MO_MATCH_EXPIRATION_TIME,//上行匹配有效期
		CHANNEL_MO_LOAD_INTERVAL_TIME,//通道上行数据加载间隔时间
		REDIS_LOCK_EXPIRATION_TIME,//redis分布锁的过期时间
		ACCOUNT_FINANCE_LOAD_INTERVAL_TIME,//账号财务信息加载间隔时间
	}
	
	//业务账号的过滤项
	public static enum AccountFilterItem {
		COMMON_BLACK_WORD_FILTERING,//黑词过滤
		COMMON_AUDIT_WORD_FILTERING,//审核词过滤
		COMMON_SEND_FREQUENCY_LIMIT,//相同手机号频次限制
		COMMON_BLACK_LIST_LEVEL_FILTERING,//黑名单层级过滤
		COMMON_SEND_LIMIT_STYLE_DAILY,//日限量方式
		COMMON_SEND_LIMIT_NUMBER_DAILY_CMCC,//移动日限量
		COMMON_SEND_LIMIT_NUMBER_DAILY_UNIC,//联通日限量
		COMMON_SEND_LIMIT_NUMBER_DAILY_TELC,//电信日限量	
		COMMON_SEND_TIME_LIMIT,//发送时间限制
		COMMON_CMCC_MASK_PROVINCE,//移动屏蔽省份
		COMMON_UNIC_MASK_PROVINCE,//联通屏蔽省份
		COMMON_TELC_MASK_PROVINCE//电信屏蔽省份
		
	}
	
	//业务账号的扩展项
	public static enum AccountExtendItem {
		ACCOUNT_REPORT_PUSH_INTERVAL_TIME,//状态报告推送间隔时间
		ACCOUNT_CONSUME_TYPE,//扣费方式
	}
	
	//业务数据的生效时间
	public static enum ChannelExtendItem {
		DAY_SUCCESS_NUMBER,//每日成功量(仅用于通道组中通道量满时权重为0)
		MONTH_SUCCESS_NUMBER,//每月成功量(仅用于通道组中通道量满时权重为0)
		EXTEND_INTERFACE_PARAM,//通道扩展参数
	}
	
	//关键词分类的大类别
	public static enum KeyWordBigClassifyItem {
		CARRIER,//运营商
		INFO_TYPE,//信息分类
		SYSTEM,//系统级
		CHANNEL,//通道
		BUSINESS_ACCOUNT,//业务账号
	}
	
	public static enum KeyWordTypeItem {
		BLACK,//黑词
		CHECK,//审核词
		WHITE_AVOID_BLACK_WORD,//白词免黑词限制
		WHITE_AVOID_CHECK_WORD,//白词免审核词限制
		WHITE_AVOID_FREQUENCY,//白词免频次限制
		WHITE_AVOID_BLACK_LIST,//白词免黑名单限制
	}
	 	
	public static enum InfoTypeItem {
		INDUSTRY,//行业
		MARKETING,//会销
		NEW,//拉新
		COLLECTION,//催收
	}
	
	public static enum RepairStatus {
		NO_CONFIG,//未配置
		NO_REPAIR,//不补发
		REPAIR,//补发
	}
}


