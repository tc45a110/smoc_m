package com.business.statistics.finance.vo;

import com.business.statistics.finance.entity.FinanceAccountReturnStatistics;

public class FinanceAccountReturnStatisticsVo extends FinanceAccountReturnStatistics{

	private String accountChargeType;
	
	private Integer consumeNum = 0;
	
	private Double consumeSum = 0d;
	
	private String consumeType;
	
	private String consumeTime;
	

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	public String getAccountChargeType() {
		return accountChargeType;
	}

	public void setAccountChargeType(String accountChargeType) {
		this.accountChargeType = accountChargeType;
	}

	public Integer getConsumeNum() {
		return consumeNum;
	}

	public void setConsumeNum(Integer consumeNum) {
		this.consumeNum = consumeNum;
	}

	public Double getConsumeSum() {
		return consumeSum;
	}

	public void setConsumeSum(Double consumeSum) {
		this.consumeSum = consumeSum;
	}

	public String getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(String consumeType) {
		this.consumeType = consumeType;
	}
	
}
