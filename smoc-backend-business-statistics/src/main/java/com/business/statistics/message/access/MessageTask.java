package com.business.statistics.message.access;

public class MessageTask {
	
	/**
	 * 批次号
	 */
	private String  batchNumber;
	
	/**
	 * 成功发送条数
	 */
	private  int  successSendNumber;
	
	/**
	 * 失败条数
	 */
	private  int  failureNumber;

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
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

	
	
	
	
	

}
