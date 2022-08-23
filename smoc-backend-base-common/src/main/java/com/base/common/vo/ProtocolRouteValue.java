package com.base.common.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class ProtocolRouteValue implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Override
	public ProtocolRouteValue clone() {
		try {
			return (ProtocolRouteValue) super.clone();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ProtocolRouteValue();
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
	 * 本条消息总条数
	 */
	private int messageTotal;

	/**
	 * 本条消息当前条数
	 */
	private int messageIndex;

	/**
	 * 发送手机号
	 */
	private String phoneNumber;

	/**
	 * 账号提交信息到达平台时间
	 */
	private String accountSubmitTime;

	/**
	 * 账号提交的码号或扩展码
	 */
	private String accountSubmitSRCID;

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
	 * 账号提交平台响应消息ID,多个用&拼接
	 */
	private String accountMessageIDs;

	/**
	 * 通道返回状态报告时间
	 */
	private String channelReportTime;
	
	/**
	 * 状态码
	 */
	private String statusCode;

	/**
	 * 状态子码
	 */
	private String subStatusCode;
	
	/**
	 * 状态报告推送次数
	 */
	private int reportPushTimes = 0;
	
	/**
	 * 用于个性化接口规范中的可选参数
	 */
	private String optionParam;
	
	public ProtocolRouteValue() {}
	
	public ProtocolRouteValue(BusinessRouteValue businessRouteValue) {
		setAccountID(businessRouteValue.getAccountID());
		setPhoneNumber(businessRouteValue.getPhoneNumber());
		setChannelReportTime(businessRouteValue.getChannelReportTime());
		setAccountSubmitTime(businessRouteValue.getAccountSubmitTime());
		setStatusCode(businessRouteValue.getStatusCode());
		
		setAccountTemplateID(businessRouteValue.getAccountTemplateID());
		setAccountSubmitSRCID(businessRouteValue.getAccountSubmitSRCID());
		setAccountBusinessCode(businessRouteValue.getAccountBusinessCode());
		setMessageTotal(businessRouteValue.getMessageTotal());
		setMessageIndex(businessRouteValue.getMessageIndex());
		
		setOptionParam(businessRouteValue.getOptionParam());
		setRouteLabel(businessRouteValue.getRouteLabel());
		setAccountMessageIDs(businessRouteValue.getAccountMessageIDs());
		setAccountReportFlag(businessRouteValue.getAccountReportFlag());
		setSubStatusCode(businessRouteValue.getSubStatusCode());
	}
	
	public int getReportPushTimes() {
		return reportPushTimes;
	}

	public void setReportPushTimes(int reportPushTimes) {
		this.reportPushTimes = reportPushTimes;
	}

	public String getChannelReportTime() {
		return channelReportTime;
	}

	public void setChannelReportTime(String channelReportTime) {
		this.channelReportTime = channelReportTime;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public String getAccountMessageIDs() {
		return accountMessageIDs;
	}

	public void setAccountMessageIDs(String accountMessageIDs) {
		this.accountMessageIDs = accountMessageIDs;
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

	public String getOptionParam() {
		return optionParam;
	}

	public void setOptionParam(String optionParam) {
		this.optionParam = optionParam;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 将部分属性转化成json数据，当值为空时不进行封装
	 * 
	 * @return
	 */
	public String toJSONString() {
		JSONObject JSONObject = (JSONObject) com.alibaba.fastjson.JSONObject.toJSON(this);
		return JSONObject.toJSONString();
	}

	/**
	 * 输出全部属性信息的json数据
	 */
	public String toString() {
		JSONObject JSONObject = (JSONObject) com.alibaba.fastjson.JSONObject.toJSON(this);
		return JSONObject.toJSONString();
	}

	
}
