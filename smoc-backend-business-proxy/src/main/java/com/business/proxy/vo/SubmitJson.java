/**
 * @desc
 * 
 */
package com.business.proxy.vo;

public class SubmitJson {
	private String  messageJson ;
	private String  messageContent ;
	private String tableSubmitTime ;
	public SubmitJson(String messageJson, String messageContent,
			String tableSubmitTime) {
		super();
		this.messageJson = messageJson;
		this.messageContent = messageContent;
		this.tableSubmitTime = tableSubmitTime;
	}
	public String getMessageJson() {
		return messageJson;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public String getTableSubmitTime() {
		return tableSubmitTime;
	}
}


