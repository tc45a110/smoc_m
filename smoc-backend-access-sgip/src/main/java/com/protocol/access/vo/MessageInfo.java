package com.protocol.access.vo;

public class MessageInfo {

	/**
	 * 账号ID,主键
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
	 * 短信内容
	 */
	private String messageContent;
	/**
	 * 短信内容编码
	 */
	private String messageFormat;
	/**
	 * 消息ID/批次ID
	 */
	private String messageId;
	/**
	 * 模板ID
	 */
	private String templateId;
	/**
	 * 平台表示
	 */
	private String protocol;
	/**
	 * 客户提交服务码号/扩展码
	 */
	private String accountSrcId;
	/**
	 * 客户提交业务代码
	 */
	private String accountBusinessCode;
	/**
	 * 一个批次包含的手机号数量
	 */
	private Integer phoneNumberNumber;
	/**
	 * 一个批次包含的消息条数,长短信算多条
	 */
	private Integer messageContentNumber;
	/**
	 * 是否需要回传状态报告标识
	 */
	private Byte reportFlag;
	/**
	 * HTTP协议,客户提交的可选参数,状态报告和上行会附带该值
	 */
	private String optionParam;
	/**
	 * 总条数
	 */
	private Integer total;
	/**
	 * 第几条
	 */
	private Integer number;
	/**
	 * 长短信标识
	 */
	private Integer longsmid;

	public MessageInfo() {

	}

	public MessageInfo(MessageInfo vo) {
		this.total = vo.getTotal();
		this.longsmid = vo.getLongsmid();

		this.accountId = vo.getAccountId();
		this.phoneNumber = vo.getPhoneNumber();
		this.submitTime = vo.getSubmitTime();
		this.messageContent = vo.getMessageContent();
		this.messageId = vo.getMessageId();
		this.messageFormat = vo.getMessageFormat();
		this.templateId = vo.getTemplateId();
		this.protocol = vo.getProtocol();
		this.accountSrcId = vo.getAccountSrcId();
		this.accountBusinessCode = vo.getAccountBusinessCode();
		this.phoneNumberNumber = vo.getPhoneNumberNumber();
		this.messageContentNumber = vo.getMessageContentNumber();
		this.reportFlag = vo.getReportFlag();
		this.optionParam = vo.getOptionParam();
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getLongsmid() {
		return longsmid;
	}

	public void setLongsmid(Integer longsmid) {
		this.longsmid = longsmid;
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

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
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

	public Integer getPhoneNumberNumber() {
		return phoneNumberNumber;
	}

	public void setPhoneNumberNumber(Integer phoneNumberNumber) {
		this.phoneNumberNumber = phoneNumberNumber;
	}

	public Integer getMessageContentNumber() {
		return messageContentNumber;
	}

	public void setMessageContentNumber(Integer messageContentNumber) {
		this.messageContentNumber = messageContentNumber;
	}

	public Byte getReportFlag() {
		return reportFlag;
	}

	public void setReportFlag(Byte reportFlag) {
		this.reportFlag = reportFlag;
	}

	public String getOptionParam() {
		return optionParam;
	}

	public void setOptionParam(String optionParam) {
		this.optionParam = optionParam;
	}

	@Override
	public String toString() {
		return "RouteMessageMtInfoVo [accountId=" + accountId + ", phoneNumber=" + phoneNumber + ", submitTime="
				+ submitTime + ", messageContent=" + messageContent + ", messageFormat=" + messageFormat
				+ ", messageId=" + messageId + ", templateId=" + templateId + ", protocol=" + protocol
				+ ", accountSrcId=" + accountSrcId + ", accountBusinessCode=" + accountBusinessCode
				+ ", phoneNumberNumber=" + phoneNumberNumber + ", messageContentNumber=" + messageContentNumber
				+ ", reportFlag=" + reportFlag + ", optionParam=" + optionParam + ", total=" + total + ", number="
				+ number + ", longsmid=" + longsmid + "]";
	}

}
