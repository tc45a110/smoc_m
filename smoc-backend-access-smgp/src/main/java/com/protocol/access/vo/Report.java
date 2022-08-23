package com.protocol.access.vo;

import java.io.Serializable;

public class Report implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 账号ID
	 */
	private String accountId;
	/**
	 * 手机号
	 */
	private String phoneNumber;
	/**
	 * 客户提交时间
	 */
	private String submitTime;
	/**
	 * 状态报告返回时间
	 */
	private String reportTime;
	/**
	 * 上行返回时间
	 */
	private String moTime;
	/**
	 * 状态码
	 */
	private String statusCode;
	/**
	 * 消息ID/批次ID
	 */
	private String messageId;
	/**
	 * 模板ID
	 */
	private String templateId;
	/**
	 * 客户提交服务码号/扩展码
	 */
	private String accountSrcId;//发送号码
	
	/**
	 * 上行信息
	 */
	private String MOMessageContent;
	
	/**
	 * 客户提交业务代码
	 */
	private String accountBusinessCode;
	/**
	 * 本条消息总条数
	 */
	private int messageTotal;
	/**
	 * 本条消息当前条数
	 */
	private int messageIndex;
	/**
	 * HTTP协议,客户提交的可选参数,状态报告和上行会附带该值
	 */
	private String optionParam;
	/**
	 * 状态报告推送次数
	 */
	private int reportPushTimes = 0;
	/**
	 * 业务标识
	 * @return
	 */
	private String businessMessageID;
	/**
	 * 账号需要状态报告标识，1为需要返回，其他为不需要返回
	 */
	private int accountReportFlag = 1;
	
	
	public int getReportPushTimes() {
		return reportPushTimes;
	}

	public void setReportPushTimes(int reportPushTimes) {
		this.reportPushTimes = reportPushTimes;
	}

	public int getAccountReportFlag() {
		return accountReportFlag;
	}

	public void setAccountReportFlag(int accountReportFlag) {
		this.accountReportFlag = accountReportFlag;
	}

	public String getBusinessMessageID() {
		return businessMessageID;
	}

	public void setBusinessMessageID(String businessMessageID) {
		this.businessMessageID = businessMessageID;
	}

	public String getMoTime() {
		return moTime;
	}

	public void setMoTime(String moTime) {
		this.moTime = moTime;
	}

	public String getMOMessageContent() {
		return MOMessageContent;
	}

	public void setMOMessageContent(String mOMessageContent) {
		MOMessageContent = mOMessageContent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getAccountSrcId() {
		return accountSrcId;
	}

	public void setAccountSrcId(String accountSrcId) {
		this.accountSrcId = accountSrcId;
	}

	public String getAccountBusinessCode() {
		return accountBusinessCode;
	}

	public void setAccountBusinessCode(String accountBusinessCode) {
		this.accountBusinessCode = accountBusinessCode;
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

	public String getOptionParam() {
		return optionParam;
	}

	public void setOptionParam(String optionParam) {
		this.optionParam = optionParam;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Report [id=" + id + ", accountId=" + accountId + ", phoneNumber=" + phoneNumber + ", submitTime="
				+ submitTime + ", reportTime=" + reportTime + ", moTime=" + moTime + ", statusCode=" + statusCode
				+ ", messageId=" + messageId + ", templateId=" + templateId + ", accountSrcId=" + accountSrcId
				+ ", MOMessageContent=" + MOMessageContent + ", accountBusinessCode=" + accountBusinessCode
				+ ", messageTotal=" + messageTotal + ", messageIndex=" + messageIndex + ", optionParam=" + optionParam
				+ ", reportPushTimes=" + reportPushTimes + ", businessMessageID=" + businessMessageID
				+ ", accountReportFlag=" + accountReportFlag + "]";
	}

}