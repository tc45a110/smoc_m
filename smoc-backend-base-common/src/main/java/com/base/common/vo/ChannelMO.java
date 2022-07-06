/**
 * @desc
 * @author ma
 * @date 2017年10月10日
 * 
 */
package com.base.common.vo;

import com.base.common.util.UUIDUtil;

public class ChannelMO implements Cloneable{
	/**
	 * 入库后标识
	 */
	private long ID;
	/**
	 * 通道上行
	 */
	private String channelMOSRCID;
	/**
	 * 手机号
	 */
	private String phoneNumber;
	
	/**
	 * 同一条信息的总条数,从1开始
	 */
	private int messageTotal = 1;
	
	/**
	 * 同一条信息的序号,从1开始
	 */
	private int messageNumber = 1;
	
	/**
	 * 长短信拆分后批号相同，SME(手机或者SP)把消息合并完之后，就重新记录，所以这个标志是否唯一并不是很 重要
	 */
	private int messageBatchNumber = 0;
	
	/**
	 * 上行短信内容
	 */
	private String messageContent;
	
	/**
	 * 上行通道ID
	 */
	private String channelID;
	
	/**
	 * 通道码号
	 */
	private String channelSRCID;
	
	/**
	 * 上行时间以协议层入库时间为准
	 */
	private String MOTime;
	
	/**
	 * 匹配上行的次数
	 */
	private int matchTimes;
	
	/**
	 * 唯一标识
	 */
	private String businessMessageID;
	
	
	public String getBusinessMessageID() {
		if(businessMessageID == null || businessMessageID.length() < 1) {
			setBusinessMessageID(UUIDUtil.get32UUID());
		}
		return businessMessageID;
	}

	public void setBusinessMessageID(String businessMessageID) {
		this.businessMessageID = businessMessageID;
	}

	private ChannelMO(){
		
	}

	public ChannelMO(String channelMOSRCID, String phoneNumber,
			String messageContent, String channelID, String channelSRCID) {
		super();
		this.channelMOSRCID = channelMOSRCID;
		this.phoneNumber = phoneNumber;
		this.messageContent = messageContent;
		this.channelID = channelID;
		this.channelSRCID = channelSRCID;
	}
	
	public ChannelMO(String channelMOSRCID, String phoneNumber,
			String messageContent, String channelID, String channelSRCID,String MOTime,long ID) {
		super();
		this.channelMOSRCID = channelMOSRCID;
		this.phoneNumber = phoneNumber;
		this.messageContent = messageContent;
		this.channelID = channelID;
		this.channelSRCID = channelSRCID;
		this.MOTime = MOTime;
		this.ID = ID;
	}
	
	public ChannelMO(String channelMOSRCID, String phoneNumber,
			String messageContent, String channelID, String channelSRCID,String MOTime,long ID,int matchTimes) {
		super();
		this.channelMOSRCID = channelMOSRCID;
		this.phoneNumber = phoneNumber;
		this.messageContent = messageContent;
		this.channelID = channelID;
		this.channelSRCID = channelSRCID;
		this.MOTime = MOTime;
		this.ID = ID;
		this.matchTimes = matchTimes;
	}
	

	public ChannelMO(String channelMOSRCID, String phoneNumber,
			int messageTotal, int messageNumber, int messageBatchNumber,
			String messageContent, String channelID, String channelSRCID) {
		super();
		this.channelMOSRCID = channelMOSRCID;
		this.phoneNumber = phoneNumber;
		this.messageTotal = messageTotal;
		this.messageNumber = messageNumber;
		this.messageBatchNumber = messageBatchNumber;
		this.messageContent = messageContent;
		this.channelID = channelID;
		this.channelSRCID = channelSRCID;
	}
	
	

	public String getChannelMOSRCID() {
		return channelMOSRCID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public int getMessageTotal() {
		return messageTotal;
	}

	public int getMessageNumber() {
		return messageNumber;
	}

	public int getMessageBatchNumber() {
		return messageBatchNumber;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public String getChannelID() {
		return channelID;
	}
	
	public String getMOTime() {
		return MOTime;
	}

	public void setMOTime(String mOTime) {
		MOTime = mOTime;
	}
	
	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
	
	public int getMatchTimes() {
		return matchTimes;
	}

	@Override
	public String toString() {
		return "ChannelMO [channelMOSRCID=" + channelMOSRCID + ", phoneNumber="
				+ phoneNumber + ", messageTotal=" + messageTotal
				+ ", messageNumber=" + messageNumber + ", messageBatchNumber="
				+ messageBatchNumber + ", messageContent=" + messageContent
				+ ", channelID=" + channelID + ", channelSRCID=" + channelSRCID
				+ "]";
	}

	public String getChannelSRCID() {
		return channelSRCID;
	}

	@Override
	public ChannelMO clone(){
		try {
			return (ChannelMO)super.clone();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ChannelMO();
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	
}


