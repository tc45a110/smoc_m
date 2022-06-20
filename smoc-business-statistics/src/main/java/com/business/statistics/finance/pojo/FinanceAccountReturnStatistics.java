package com.business.statistics.finance.pojo;

import java.util.Date;

public class FinanceAccountReturnStatistics {

	private String id;	
	/**
	 * 	业务账号id;外键
	 */
	private String accountID;		
	/**
	 * 	消费的财务账户id
	 */
	private String consumeAccountID;	
	/**
	 * 	发送时间(yyyymmdd)
	 */
	private String sendTime;			
	/**
	 * 	状态报告时间(yyyymmdd)
	 */
	private String reportTime;		
	/**
	 * 	返还数量
	 */
	private Integer returnNum = 0;			
	/**
	 * 	返还金额（元）
	 */
	private Double returnSum = 0d;			
	/**
	 * 	成功数量
	 */
	private Integer sucessNum = 0;			
	/**
	 * 	成功金额（元）
	 */
	private Double sucessSum = 0d;			
	/**
	 *	 解冻数量
	 */
	private Integer unfreezeNum = 0;		
	/**
	 * 	解冻金额（元）
	 */
	private Double unfreezeSum = 0d;		
	/**
	 * 	失败数量
	 */
	private Integer failureNum = 0;			
	/**
	 * 	失败返还金额（元）
	 */
	private Double failureSum = 0d;			
	/**
	 * 	未知数量
	 */
	private Integer noReportNum = 0;		
	/**
	 * 	未知返还金额（元）
	 */
	private Double noReportSum = 0d;		
	/**
	 * 	创建人
	 */
	private String createdBy;			
	/**
	 * 	创建时间
	 */
	private Date createdTime;		
	/**
	 * 	更新时间
	 */
	private Date updatedTime;		
	/**
	 * 	更新人
	 */
	private String updatedBy;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public String getConsumeAccountID() {
		return consumeAccountID;
	}
	public void setConsumeAccountID(String consumeAccountID) {
		this.consumeAccountID = consumeAccountID;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public Integer getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(Integer returnNum) {
		this.returnNum = returnNum;
	}
	public Double getReturnSum() {
		return returnSum;
	}
	public void setReturnSum(Double returnSum) {
		this.returnSum = returnSum;
	}
	public Integer getSucessNum() {
		return sucessNum;
	}
	public void setSucessNum(Integer sucessNum) {
		this.sucessNum = sucessNum;
	}
	public Double getSucessSum() {
		return sucessSum;
	}
	public void setSucessSum(Double sucessSum) {
		this.sucessSum = sucessSum;
	}
	public Integer getUnfreezeNum() {
		return unfreezeNum;
	}
	public void setUnfreezeNum(Integer unfreezeNum) {
		this.unfreezeNum = unfreezeNum;
	}
	public Double getUnfreezeSum() {
		return unfreezeSum;
	}
	public void setUnfreezeSum(Double unfreezeSum) {
		this.unfreezeSum = unfreezeSum;
	}
	public Integer getFailureNum() {
		return failureNum;
	}
	public void setFailureNum(Integer failureNum) {
		this.failureNum = failureNum;
	}
	public Double getFailureSum() {
		return failureSum;
	}
	public void setFailureSum(Double failureSum) {
		this.failureSum = failureSum;
	}
	public Integer getNoReportNum() {
		return noReportNum;
	}
	public void setNoReportNum(Integer noReportNum) {
		this.noReportNum = noReportNum;
	}
	public Double getNoReportSum() {
		return noReportSum;
	}
	public void setNoReportSum(Double noReportSum) {
		this.noReportSum = noReportSum;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
		
	
}
