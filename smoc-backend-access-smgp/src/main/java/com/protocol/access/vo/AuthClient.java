/**
 * @desc
 * @author ma
 * @date 2017年9月7日
 * 
 */
package com.protocol.access.vo;

import java.util.Set;

public class AuthClient {
	
	/**
	 * 账号ID,主键
	 */
	private String accountID;
	/**
	 * 平台标识
	 */
	private String protocol;
	/**
	 * 密码
	 */
	private String accountPassword;
	/**
	 * 最大提交速率
	 */
	private Integer maxSubmitSecond;
	/**
	 * 最大发送速率
	 */
	private Integer maxSendSecond;
	/**
	 * 服务代码
	 */
	private String srcId;
	/**
	 * 鉴权IP
	 */
	private Set<String> identifyIP;
	/**
	 * 鉴权IP为*时，不做IP鉴权
	 */
	private boolean authFlag = true;
	/**
	 * 最大连接数
	 */
	private Integer maxConnect;
	/**
	 * 是否审核
	 */
	private String executeCheck;
	/**
	 * 上行地址
	 */
	private String moUrl;
	/**
	 * 状态报告地址
	 */
	private String statusReportUrl;

	public boolean isAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(boolean authFlag) {
		this.authFlag = authFlag;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public Integer getMaxSubmitSecond() {
		return maxSubmitSecond;
	}

	public void setMaxSubmitSecond(Integer maxSubmitSecond) {
		this.maxSubmitSecond = maxSubmitSecond;
	}

	public Integer getMaxSendSecond() {
		return maxSendSecond;
	}

	public void setMaxSendSecond(Integer maxSendSecond) {
		this.maxSendSecond = maxSendSecond;
	}

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public Integer getMaxConnect() {
		return maxConnect;
	}

	public void setMaxConnect(Integer maxConnect) {
		this.maxConnect = maxConnect;
	}

	public String getExecuteCheck() {
		return executeCheck;
	}

	public void setExecuteCheck(String executeCheck) {
		this.executeCheck = executeCheck;
	}

	public String getMoUrl() {
		return moUrl;
	}

	public void setMoUrl(String moUrl) {
		this.moUrl = moUrl;
	}

	public String getStatusReportUrl() {
		return statusReportUrl;
	}

	public void setStatusReportUrl(String statusReportUrl) {
		this.statusReportUrl = statusReportUrl;
	}

	public Set<String> getIdentifyIP() {
		return identifyIP;
	}

	public void setIdentifyIP(Set<String> identifyIP) {
		this.identifyIP = identifyIP;
	}

	@Override
	public String toString() {
		return "AuthClientVO [accountId=" + accountID + ", protocol=" + protocol + ", accountPassword="
				+ accountPassword + ", maxSubmitSecond=" + maxSubmitSecond + ", maxSendSecond=" + maxSendSecond
				+ ", srcId=" + srcId + ", ip=" + identifyIP + ", authFlag=" + authFlag + ", maxConnect=" + maxConnect
				+ ", executeCheck=" + executeCheck + ", moUrl=" + moUrl + ", statusReportUrl=" + statusReportUrl + "]";
	}

}
