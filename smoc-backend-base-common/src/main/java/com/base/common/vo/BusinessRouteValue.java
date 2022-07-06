package com.base.common.vo;

import java.io.Serializable;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;

public class BusinessRouteValue implements Serializable,Cloneable {

	private static final long serialVersionUID = 1L;
	
	
	@Override
	public BusinessRouteValue clone(){
		try {
			return (BusinessRouteValue)super.clone();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BusinessRouteValue();
	}
	
	/**
	 * 路由标签：MO上行，RP状态报告，MT下行
	 */
	private String routeLabel;

	/**
	 * 业务账号ID
	 */
	private String accountID;
	
	/**
	 * 业务账号名称
	 */
	private String accountName;
	
	/**
	 * 业务账号优先级
	 */
	private String accountPriority;
	
	/**
	 * 计费账号ID
	 */
	private String financeAccountID;
	
	/**
	 * 计费单价
	 */
	private String messagePrice;
	
	/**
	 * 本条消息总条数
	 */
	private int messageTotal;
	
	/**
	 * 本条消息当前条数
	 */
	private int messageIndex;
	
	/**
	 * 本条信息计费条数
	 */
	private String messageNumber;
	
	/**
	 * 计费金额
	 */
	private String messageAmount;
	
	/**
	 * 企业ID
	 */
	private String enterpriseFlag;
	/**
	 * 接口协议
	 */
	private String protocol;
	/**
	 * 发送手机号
	 */
	private String phoneNumber;
	/**
	 * 号段运营商:根据号段识别的运营商标识
	 */
	private String segmentCarrier;
	/**
	 * 业务运营商:平台业务处理的运营商标识
	 */
	private String businessCarrier;
	/**
	 * 区域编码：国家编码或省份编码
	 */
	private String areaCode;
	/**
	 * 区域名称:国家名称或省份名称
	 */
	private String areaName;
	
	/**
	 * 地市名称:
	 */
	private String cityName;
	
	/**
	 * 价格区域编码
	 */
	private String priceAreaCode;
	
	/**
	 * 业务类型：普通短信、多媒体短信、5G短信、国际短信、彩信的编码
	 */
	private String businessType;
	
	/**
	 * 扣费方式 1：下发时扣费 2：回执返回时扣费
	 */
	private String consumeType;
	
	/**
	 * 账号提交信息到达平台时间
	 */
	private String accountSubmitTime;
	
	/**
	 * 账号提交的码号或扩展码
	 */
	private String accountSubmitSRCID;
	
	/**
	 * 账号在平台的扩展码包含随机扩展码加上账号自带扩展码
	 */
	private String accountExtendCode;
	
	/**
	 * 账号提交的业务码号
	 */
	private String accountBusinessCode;
	
	/**
	 * 账号需要状态报告标识，1为需要返回，其他为不需要返回
	 */
	private int accountReportFlag = 1;
	
	/**
	 * 账号模板id
	 */
	private String accountTemplateID = "0";
	
	/**
	 * 发送批次号
	 */
	private String accountTaskID = "0";
	
	/**
	 * 批次包含的手机号数量
	 */
	private long taskPhoneNumberNumber;
	
	/**
	 * 批次包含的消息条数,长短信算多条
	 */
	private long taskMessageNumber;
	
	/**
	 * 行业分类：多个行业用&分隔
	 */
	private String industryTypes;
	
	/**
	 * 信息分类:按照投诉高低分为：行业、会销、拉新、催收
	 */
	private String infoType;
	
	/**
	 * 账号提交平台响应消息ID,多个用&拼接
	 */
	private String accountMessageIDs;
	
	/**
	 * 标识一条消息，长短信该值相同
	 */
	private String businessMessageID;
	
	/**
	 * 到审核表时间
	 */
	private String tableAuditTime;
	
	/**
	 * 到下行通道表时间
	 */
	private String tableSubmitTime;
	
	/**
	 * 消息内容
	 */
	private String messageContent;
	
	/**
	 * 上行内容
	 */
	private String MOContent;
	
	/**
	 * 消息格式
	 */
	private String messageFormat;
	
	/**
	 * 消息签名
	 */
	private String messageSignature;
	
	/**
	 * 到下发队列时间
	 */
	private String queueSubmitTime;
	
	/**
	 * 通道ID
	 */
	private String channelID;
	
	/**
	 * 通道名称
	 */
	private String channelName;
	
	/**
	 * 通道单价
	 */
	private String channelPrice;
	
	/**
	 * 通道接入码
	 */
	private String channelSRCID;
	
	/**
	 * 提交到通道的码号
	 */
	private String channelSubmitSRCID;
	
	/**
	 * 提交到通道总条数
	 */
	private int channelTotal;
	
	/**
	 * 提交到通道本条序号
	 */
	private int channelIndex;
	
	/**
	 * 提交到通道时间
	 */
	private String channelSubmitTime;
	
	/**
	 * 提交到通道响应消息ID
	 */
	private String channelMessageID;
	
	/**
	 * 是否进入了审核的code
	 */
	private int executeCheckCode;
	
	/**
	 * 路由下一个节点的code：0成功；其他代表失败
	 */
	private String nextNodeCode;
	
	/**
	 * 路由下一个的节点的错误码
	 */
	private String nextNodeErrorCode;

	/**
	 * 通道返回状态报告时间
	 */
	private String channelReportTime;
	
	/**
	 * 成功码0代表成功2代表失败
	 */
	private String successCode;
	
	/**
	 * 状态码
	 */
	private String statusCode;
	
	/**
	 * 状态子码
	 */
	private String subStatusCode;
	
	/**
	 * 状态码来源0:内部;1:通道提交码;2:通道状态码
	 */
	private String statusCodeSource;
	
	/**
	 * 通道返回状态报告的码号，用于当通过消息ID未能匹配到提交记录时，可以通过码号+手机号+通道ID 进行模糊匹配，类似上行。
	 */
	private String channelReportSRCID;
	
	/**
	 * 通道上行码号
	 */
	private String channelMOSRCID;
	
	/**
	 * 通道模板id
	 */
	private String channelTemplateID="0";
	
	/**
	 * 重复发送次数
	 */
	private int repeatSendTimes;
	
	/**
	 * 用于个性化接口规范中的可选参数
	 */
	private String optionParam;
	
	/**
	 * 进入审核原因
	 */
	private String auditReason;
	
	/**
	 * 补发通道
	 */
	private ArrayList<String> channelRepairIDs;
	
	/**
	 * 补发时间
	 */
	private int repairTime;


	public ArrayList<String> getChannelRepairIDs() {
		return channelRepairIDs;
	}

	public void setChannelRepairIDs(ArrayList<String> channelRepairIDs) {
		this.channelRepairIDs = channelRepairIDs;
	}

	public int getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(int repairTime) {
		this.repairTime = repairTime;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	public String getRouteLabel() {
		return routeLabel;
	}

	public void setRouteLabel(String routeLabel) {
		this.routeLabel = routeLabel;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getFinanceAccountID() {
		return financeAccountID;
	}

	public void setFinanceAccountID(String financeAccountID) {
		this.financeAccountID = financeAccountID;
	}

	public String getMessagePrice() {
		return messagePrice;
	}

	public void setMessagePrice(String messagePrice) {
		this.messagePrice = messagePrice;
	}

	public int getMessageTotal() {
		return messageTotal;
	}

	public void setMessageTotal(int messageTotal) {
		this.messageTotal = messageTotal;
	}

	public int getMessageIndex() {
		return messageIndex;
	}

	public void setMessageIndex(int messageIndex) {
		this.messageIndex = messageIndex;
	}

	public String getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(String messageNumber) {
		this.messageNumber = messageNumber;
	}

	public String getMessageAmount() {
		return messageAmount;
	}

	public void setMessageAmount(String messageAmount) {
		this.messageAmount = messageAmount;
	}

	public String getEnterpriseFlag() {
		return enterpriseFlag;
	}

	public void setEnterpriseFlag(String enterpriseFlag) {
		this.enterpriseFlag = enterpriseFlag;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSegmentCarrier() {
		return segmentCarrier;
	}

	public void setSegmentCarrier(String segmentCarrier) {
		this.segmentCarrier = segmentCarrier;
	}

	public String getBusinessCarrier() {
		return businessCarrier;
	}

	public void setBusinessCarrier(String businessCarrier) {
		this.businessCarrier = businessCarrier;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPriceAreaCode() {
		return priceAreaCode;
	}

	public void setPriceAreaCode(String priceAreaCode) {
		this.priceAreaCode = priceAreaCode;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(String consumeType) {
		this.consumeType = consumeType;
	}

	public String getAccountSubmitTime() {
		return accountSubmitTime;
	}

	public void setAccountSubmitTime(String accountSubmitTime) {
		this.accountSubmitTime = accountSubmitTime;
	}

	public String getAccountSubmitSRCID() {
		return accountSubmitSRCID;
	}

	public void setAccountSubmitSRCID(String accountSubmitSRCID) {
		this.accountSubmitSRCID = accountSubmitSRCID;
	}

	public String getAccountExtendCode() {
		return accountExtendCode;
	}

	public void setAccountExtendCode(String accountExtendCode) {
		this.accountExtendCode = accountExtendCode;
	}

	public String getAccountBusinessCode() {
		return accountBusinessCode;
	}

	public void setAccountBusinessCode(String accountBusinessCode) {
		this.accountBusinessCode = accountBusinessCode;
	}

	public int getAccountReportFlag() {
		return accountReportFlag;
	}

	public void setAccountReportFlag(int accountReportFlag) {
		this.accountReportFlag = accountReportFlag;
	}

	public String getAccountTemplateID() {
		return accountTemplateID;
	}

	public void setAccountTemplateID(String accountTemplateID) {
		this.accountTemplateID = accountTemplateID;
	}

	public String getAccountTaskID() {
		return accountTaskID;
	}

	public void setAccountTaskID(String accountTaskID) {
		this.accountTaskID = accountTaskID;
	}

	public long getTaskPhoneNumberNumber() {
		return taskPhoneNumberNumber;
	}

	public void setTaskPhoneNumberNumber(long taskPhoneNumberNumber) {
		this.taskPhoneNumberNumber = taskPhoneNumberNumber;
	}

	public long getTaskMessageNumber() {
		return taskMessageNumber;
	}

	public void setTaskMessageNumber(long taskMessageNumber) {
		this.taskMessageNumber = taskMessageNumber;
	}

	public String getIndustryTypes() {
		return industryTypes;
	}

	public void setIndustryTypes(String industryTypes) {
		this.industryTypes = industryTypes;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getAccountMessageIDs() {
		return accountMessageIDs;
	}

	public void setAccountMessageIDs(String accountMessageIDs) {
		this.accountMessageIDs = accountMessageIDs;
	}

	public String getBusinessMessageID() {
		return businessMessageID;
	}

	public void setBusinessMessageID(String businessMessageID) {
		this.businessMessageID = businessMessageID;
	}

	public String getTableSubmitTime() {
		return tableSubmitTime;
	}

	public void setTableSubmitTime(String tableSubmitTime) {
		this.tableSubmitTime = tableSubmitTime;
	}


	public String getMessageSignature() {
		return messageSignature;
	}

	public void setMessageSignature(String messageSignature) {
		this.messageSignature = messageSignature;
	}

	public String getQueueSubmitTime() {
		return queueSubmitTime;
	}

	public void setQueueSubmitTime(String queueSubmitTime) {
		this.queueSubmitTime = queueSubmitTime;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getChannelSRCID() {
		return channelSRCID;
	}

	public void setChannelSRCID(String channelSRCID) {
		this.channelSRCID = channelSRCID;
	}

	public String getChannelSubmitSRCID() {
		return channelSubmitSRCID;
	}

	public void setChannelSubmitSRCID(String channelSubmitSRCID) {
		this.channelSubmitSRCID = channelSubmitSRCID;
	}

	public int getChannelTotal() {
		return channelTotal;
	}

	public void setChannelTotal(int channelTotal) {
		this.channelTotal = channelTotal;
	}

	public int getChannelIndex() {
		return channelIndex;
	}

	public void setChannelIndex(int channelIndex) {
		this.channelIndex = channelIndex;
	}

	public String getChannelSubmitTime() {
		return channelSubmitTime;
	}

	public void setChannelSubmitTime(String channelSubmitTime) {
		this.channelSubmitTime = channelSubmitTime;
	}

	public String getChannelMessageID() {
		return channelMessageID;
	}

	public void setChannelMessageID(String channelMessageID) {
		this.channelMessageID = channelMessageID;
	}

	public String getChannelReportTime() {
		return channelReportTime;
	}

	public void setChannelReportTime(String channelReportTime) {
		this.channelReportTime = channelReportTime;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getSubStatusCode() {
		return subStatusCode;
	}

	public void setSubStatusCode(String subStatusCode) {
		this.subStatusCode = subStatusCode;
	}

	public String getStatusCodeSource() {
		return statusCodeSource;
	}

	public void setStatusCodeSource(String statusCodeSource) {
		this.statusCodeSource = statusCodeSource;
	}

	public String getChannelReportSRCID() {
		return channelReportSRCID;
	}

	public void setChannelReportSRCID(String channelReportSRCID) {
		this.channelReportSRCID = channelReportSRCID;
	}

	public String getChannelMOSRCID() {
		return channelMOSRCID;
	}

	public void setChannelMOSRCID(String channelMOSRCID) {
		this.channelMOSRCID = channelMOSRCID;
	}

	public String getChannelTemplateID() {
		return channelTemplateID;
	}

	public void setChannelTemplateID(String channelTemplateID) {
		this.channelTemplateID = channelTemplateID;
	}

	public String getOptionParam() {
		return optionParam;
	}

	public void setOptionParam(String optionParam) {
		this.optionParam = optionParam;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}

	public String getAccountPriority() {
		return accountPriority;
	}

	public void setAccountPriority(String accountPriority) {
		this.accountPriority = accountPriority;
	}
	
	public int getExecuteCheckCode() {
		return executeCheckCode;
	}

	public void setExecuteCheckCode(int executeCheckCode) {
		this.executeCheckCode = executeCheckCode;
	}

	public int getRepeatSendTimes() {
		return repeatSendTimes;
	}

	public void setRepeatSendTimes(int repeatSendTimes) {
		this.repeatSendTimes = repeatSendTimes;
	}

	public String getTableAuditTime() {
		return tableAuditTime;
	}

	public void setTableAuditTime(String tableAuditTime) {
		this.tableAuditTime = tableAuditTime;
	}

	public String getNextNodeErrorCode() {
		return nextNodeErrorCode;
	}

	public void setNextNodeErrorCode(String nextNodeErrorCode) {
		this.nextNodeErrorCode = nextNodeErrorCode;
	}

	public String getChannelPrice() {
		return channelPrice;
	}

	public void setChannelPrice(String channelPrice) {
		this.channelPrice = channelPrice;
	}
	
	public String getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	public String getNextNodeCode() {
		return nextNodeCode;
	}

	public void setNextNodeCode(String nextNodeCode) {
		this.nextNodeCode = nextNodeCode;
	}
	
	
	public String getMOContent() {
		return MOContent;
	}

	public void setMOContent(String mOContent) {
		MOContent = mOContent;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * 将部分属性转化成json数据，当值为空时不进行封装
	 * @return
	 */
	public String toJSONString() {
		JSONObject JSONObject = (JSONObject)com.alibaba.fastjson.JSONObject.toJSON(this);
		JSONObject.remove("messageContent");
		return JSONObject.toJSONString();
	}
	
	/**
	 * 输出全部属性信息的json数据
	 */
	public String toString(){
		JSONObject JSONObject = (JSONObject)com.alibaba.fastjson.JSONObject.toJSON(this);
		return JSONObject.toJSONString();
	}
	
	/**
	 * 通过json数据转对象
	 * @param JSONString
	 * @return
	 */
	public static BusinessRouteValue toObject(String JSONString){
		return com.alibaba.fastjson2.JSONObject.parseObject(JSONString, BusinessRouteValue.class);
	}
	
	/**
	 * 更新状态报告状态
	 * @param businessRouteValue
	 */
	public void setBusinessRouteValueReport(BusinessRouteValue businessRouteValue) {
		setChannelReportTime(businessRouteValue.getChannelReportTime());
		setStatusCode(businessRouteValue.getStatusCode());
		setSubStatusCode(businessRouteValue.getSubStatusCode());
		setSuccessCode(businessRouteValue.getSuccessCode());
		setChannelReportSRCID(businessRouteValue.getChannelReportSRCID());
		setStatusCodeSource(businessRouteValue.getStatusCodeSource());
	}
}
