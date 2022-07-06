package com.business.statistics.message.pojo;

public class MessageDailyStatistics {
	/**
	 * 唯一标识
	 */
	private String ID;
	/**
	 * 企业ID
	 */
	private String enterpriseFlag;
	/**
	 * 业务账号ID
	 */
	private String businessAccount;
	/**
	 * 通道ID
	 */
	private String channelID;
	/**
	 * 区域编码：国家编码或省份编码
	 */
	private String areaCode;
	/**
	 * 价格区域编码
	 */
	private String priceAreaCode;
	/**
	 * 所属运营商
	 */
	private String carrier;
	/**
	 * 业务类型：普通短信、多媒体短信、5G短信、国际短信、彩信的编码
	 */
	private String businessType;
	/**
	 * 信息分类:按照投诉高低分为：行业、会销、拉新、催收
	 */
	private String infoType;
	/**
	 * 客户提交数
	 */
	private Integer customerSubmitNum = 0;
	/**
	 * 提交成功数
	 */
	private Integer successSubmitNum = 0;
	/**
	 * 提交失败数
	 */
	private Integer failureSubmitNum = 0;
	/**
	 * 成功发送数
	 */
	private Integer messageSuccessNum = 0;
	/**
	 * 成功发送数
	 */
	private Integer messageFailureNum = 0;
	/**
	 * 未知数
	 */
	private Integer messageNoReportNum = 0;
	/**
	 * 消息签名
	 */
	private String messageSign;
	/**
	 * 数据日期
	 */
	private String messageDate;
	/**
	 * 通道批处理日期
	 */
	private String channelBatchDate;
	/**
	 * 账号批处理日期
	 */
	private String accountBatchDate;
	/**
	 * 通道价格
	 */
	private String channelPrice;
	/**
	 * 账号价格
	 */
	private String accountPrice;
	/**
	 * 创建人
	 */
	private String createdBy;
	/**
	 * 创建时间
	 */
	private String createdTime;
	/**
	 * 更新人
	 */
	private String updatedBy;
	/**
	 * 更新时间
	 */
	private String updatedTime;
	
	public MessageDailyStatistics() {
		
	}
	
	public MessageDailyStatistics(MessageDailyStatistics messageDailyStatistics) {
		super();
		this.enterpriseFlag = messageDailyStatistics.getEnterpriseFlag();
		this.businessAccount = messageDailyStatistics.getBusinessAccount();
		this.channelID = messageDailyStatistics.getChannelID();
		this.areaCode = messageDailyStatistics.getAreaCode();
		this.priceAreaCode = messageDailyStatistics.getPriceAreaCode();
		this.carrier = messageDailyStatistics.getCarrier();
		this.businessType = messageDailyStatistics.getBusinessType();
		this.infoType = messageDailyStatistics.getInfoType();
		this.messageSign = messageDailyStatistics.getMessageSign();
		this.messageDate = messageDailyStatistics.getMessageDate();
	}

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getEnterpriseFlag() {
		return enterpriseFlag;
	}
	public void setEnterpriseFlag(String enterpriseFlag) {
		this.enterpriseFlag = enterpriseFlag;
	}
	public String getBusinessAccount() {
		return businessAccount;
	}
	public void setBusinessAccount(String businessAccount) {
		this.businessAccount = businessAccount;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getPriceAreaCode() {
		return priceAreaCode;
	}
	public void setPriceAreaCode(String priceAreaCode) {
		this.priceAreaCode = priceAreaCode;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public Integer getCustomerSubmitNum() {
		return customerSubmitNum;
	}
	public void setCustomerSubmitNum(Integer customerSubmitNum) {
		this.customerSubmitNum = customerSubmitNum;
	}
	public Integer getSuccessSubmitNum() {
		return successSubmitNum;
	}
	public void setSuccessSubmitNum(Integer successSubmitNum) {
		this.successSubmitNum = successSubmitNum;
	}
	public Integer getFailureSubmitNum() {
		return failureSubmitNum;
	}
	public void setFailureSubmitNum(Integer failureSubmitNum) {
		this.failureSubmitNum = failureSubmitNum;
	}
	public Integer getMessageSuccessNum() {
		return messageSuccessNum;
	}
	public void setMessageSuccessNum(Integer messageSuccessNum) {
		this.messageSuccessNum = messageSuccessNum;
	}
	public Integer getMessageFailureNum() {
		return messageFailureNum;
	}
	public void setMessageFailureNum(Integer messageFailureNum) {
		this.messageFailureNum = messageFailureNum;
	}
	public Integer getMessageNoReportNum() {
		return messageNoReportNum;
	}
	public void setMessageNoReportNum(Integer messageNoReportNum) {
		this.messageNoReportNum = messageNoReportNum;
	}
	public String getMessageSign() {
		return messageSign;
	}
	public void setMessageSign(String messageSign) {
		this.messageSign = messageSign;
	}
	public String getMessageDate() {
		return messageDate;
	}
	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}
	public String getChannelBatchDate() {
		return channelBatchDate;
	}
	public void setChannelBatchDate(String channelBatchDate) {
		this.channelBatchDate = channelBatchDate;
	}
	public String getAccountBatchDate() {
		return accountBatchDate;
	}
	public void setAccountBatchDate(String accountBatchDate) {
		this.accountBatchDate = accountBatchDate;
	}
	public String getChannelPrice() {
		return channelPrice;
	}
	public void setChannelPrice(String channelPrice) {
		this.channelPrice = channelPrice;
	}
	public String getAccountPrice() {
		return accountPrice;
	}
	public void setAccountPrice(String accountPrice) {
		this.accountPrice = accountPrice;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	@Override
	public String toString() {
		return "MessageDailyStatistics [ID=" + ID + ", enterpriseFlag=" + enterpriseFlag + ", businessAccount="
				+ businessAccount + ", channelID=" + channelID + ", areaCode=" + areaCode + ", priceAreaCode="
				+ priceAreaCode + ", carrier=" + carrier + ", businessType=" + businessType + ", infoType=" + infoType
				+ ", customerSubmitNum=" + customerSubmitNum + ", successSubmitNum=" + successSubmitNum
				+ ", failureSubmitNum=" + failureSubmitNum + ", messageSuccessNum=" + messageSuccessNum
				+ ", messageFailureNum=" + messageFailureNum + ", messageNoReportNum=" + messageNoReportNum
				+ ", messageSign=" + messageSign + ", messageDate=" + messageDate + ", channelBatchDate="
				+ channelBatchDate + ", accountBatchDate=" + accountBatchDate + ", channelPrice=" + channelPrice
				+ ", accountPrice=" + accountPrice + ", createdBy=" + createdBy + ", createdTime=" + createdTime
				+ ", updatedBy=" + updatedBy + ", updatedTime=" + updatedTime + "]";
	}
	
}