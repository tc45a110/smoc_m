/**
 * @desc
 * 
 */
package com.base.common.constant;

public class InsideStatusCodeConstant {
	//下行成功和状态成功均用该值
	//成功标记
	public final static String SUCCESS_CODE = "0";
	//未知标记
	public final static String UNKNOWN_CODE = "1";
	//失败标记
	public final static String FAIL_CODE = "2";
	
	/**
	 * 内部状态码枚举,按照首字符顺序排序
	 */
	public static enum StatusCode{		
		DELIVRD,//成功
		
		AUDIT,//进入审核
		BLACK,//黑名单
		BBLACK,//行业黑名单
		DLIMIT,//运营商日限量
		ERRSIGN,//签名未报备
		FLIMIT,//频次限制
		FUSING,//熔断
		GWPAUSE,//通道暂停
		INVAREQ,//无效请求
		IPLIMIT,//iP限制
		ILLEGAL,//账号非法、时间非法
		KEYWORD,//关键词(敏感词)
		MISSING,//长短信缺失
		NOROUTE,//运营商、通道等为空
		NOPRICE,//价格为空
		NOMONEY,//余额不足
		NOLIMIT,//未设置频次限制
		OVERLEN,//内容超长
		PRKWORD,//省份内容拦截
		PNLIMIT,//手机号拦截
		REPEATD,//一分钟内重复号码、重复内容提交
		REJECTD,
		RLIMIT,//请求限制
		RBLACK,
		SHIELD,//省份屏蔽
		SYSERR,//系统错误：如超过外部黑名单过滤的最大缓存容量
		TBLACK,//第三方黑名单
		UNMATCH,//模板不匹配
		UNSIGNA,//没有签名
		UNKNOWN,//未知
	}
	
}


