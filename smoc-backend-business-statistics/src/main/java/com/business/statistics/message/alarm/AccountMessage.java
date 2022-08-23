package com.business.statistics.message.alarm;

public class AccountMessage {
	/**
	 * 账号
	 */
	private String accountID;
	/**
	 * 提交条数
	 */
	private int mtNumber;
	/**
	 * 状态报告返回条数
	 */
	private int mrNumber;
	/**
	 * 成功发送条数
	 */
	private int successSendNumber;
	/**
	 * 失败条数
	 */
	private int failureNumber;
	/**
	 * 延迟条数
	 */
	private int delayNumber;

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public int getMtNumber() {
		return mtNumber;
	}

	public void setMtNumber(int mtNumber) {
		this.mtNumber = mtNumber;
	}

	public int getSuccessSendNumber() {
		return successSendNumber;
	}

	public void setSuccessSendNumber(int successSendNumber) {
		this.successSendNumber = successSendNumber;
	}

	public int getFailureNumber() {
		return failureNumber;
	}

	public void setFailureNumber(int failureNumber) {
		this.failureNumber = failureNumber;
	}

	public int getMrNumber() {
		return mrNumber;
	}

	public void setMrNumber(int mrNumber) {
		this.mrNumber = mrNumber;
	}

	public int getDelayNumber() {
		return delayNumber;
	}

	public void setDelayNumber(int delayNumber) {
		this.delayNumber = delayNumber;
	}

}
