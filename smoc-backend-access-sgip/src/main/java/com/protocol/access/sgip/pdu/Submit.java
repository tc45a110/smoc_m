/**
 * @desc
 * @author ma
 * @date 2017�?11�?2�?
 * 
 */
package com.protocol.access.sgip.pdu;

import com.protocol.access.sgip.sms.ByteBuffer;
import com.protocol.access.sgip.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.sgip.sms.PDUException;
import com.protocol.access.sgip.sms.ShortMessage;

import org.apache.commons.codec.binary.Hex;

import com.base.common.constant.FixedConstant;
import com.protocol.access.sgip.SgipConstant;

public class Submit extends Request{

	private String msgId = "";

	private String spNumber = "";
	private String chargeNumber = "";
	private byte userCount = 0;
	private String userNumber[] = new String[0];
	
	private String corpId = "";
	private String serviceType = "";
	private byte feeType = 0;
	private String feeValue = "";
	private String givenValue = "";
	private byte agentFlag = 0;
	private byte morelatetoMtFlag = 0;
	private byte priority = 0;
	private String expireTime = "";
	private String scheduleTime = "";
	private byte reportFlag = 0;
	private byte tpPid = 0;
	private byte tpUdhi = 0;
	private byte messageCoding = 0;
	private byte messageType = 0;
	private int messageLength = 0;
	private ShortMessage sm = new ShortMessage();
	
	private String reserve = "";

	public Submit() {
		super(SgipConstant.SGIP_SUBMIT);
	}

	protected Response createResponse() {
		return new SubmitResp();
	}

	public void setBody(ByteBuffer buffer)
	throws PDUException {
		try {
			spNumber = buffer.removeStringEx(21);
			chargeNumber = buffer.removeStringEx(21);
			userCount = buffer.removeByte();
			userNumber = new String[userCount];
			for(int i = 0; i<userCount; i++){
				String phoneNumber = buffer.removeStringEx(21);
				if (phoneNumber.length() > 11) {
					phoneNumber = phoneNumber.substring(phoneNumber
							.length() - 11);
				}
				userNumber[i] = phoneNumber;
			}		
			corpId = buffer.removeStringEx(21);
			serviceType = buffer.removeStringEx(10);
			feeType = buffer.removeByte();
			feeValue = buffer.removeStringEx(6);
			givenValue = buffer.removeStringEx(6);
			agentFlag = buffer.removeByte();
			morelatetoMtFlag = buffer.removeByte();
			priority = buffer.removeByte();
			expireTime = buffer.removeStringEx(16);
			reportFlag = buffer.removeByte();
			tpPid = buffer.removeByte();
			tpUdhi = buffer.removeByte();
			messageCoding = buffer.removeByte();
			messageType = buffer.removeByte();
			messageLength = buffer.removeInt();
			int msgLength = messageLength < 0 ? messageLength + 256 : messageLength;
			sm.setData(buffer.removeBuffer(msgLength));
			sm.setMsgFormat(messageCoding);
			//长短信标�?
			sm.setTpUdhi(tpUdhi);
			reserve = buffer.removeStringEx(8);
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}
	}
	
	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(spNumber,21);
		buffer.appendString(chargeNumber,21);
		buffer.appendByte(userCount);
		for(int i = 0; i < userNumber.length; i++)
			buffer.appendString(userNumber[i],21);
		
		buffer.appendString(corpId,5);
		buffer.appendString(serviceType,10);
		buffer.appendInt(feeType);
		buffer.appendString(feeValue, 6);
		buffer.appendString(givenValue,6);
		buffer.appendByte(agentFlag);
		buffer.appendByte(morelatetoMtFlag);
		buffer.appendByte(priority);
		buffer.appendString(expireTime,16);
		buffer.appendString(scheduleTime,16);
		buffer.appendByte(reportFlag);
		buffer.appendByte(tpPid);
		buffer.appendByte(tpUdhi);
		buffer.appendByte(messageCoding);
		buffer.appendByte(messageType);
		buffer.appendInt(messageLength);
		buffer.appendBuffer(sm.getData());
		buffer.appendString(reserve, 8);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(SgipConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public String getChargeNumber() {
		return chargeNumber;
	}

	public void setChargeNumber(String chargeNumber) {
		this.chargeNumber = chargeNumber;
	}

	public byte getUserCount() {
		return userCount;
	}

	public void setUserCount(byte userCount) {
		this.userCount = userCount;
	}

	public String[] getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String[] userNumber) {
		this.userNumber = userNumber;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public byte getFeeType() {
		return feeType;
	}

	public void setFeeType(byte feeType) {
		this.feeType = feeType;
	}

	public String getFeeValue() {
		return feeValue;
	}

	public void setFeeValue(String feeValue) {
		this.feeValue = feeValue;
	}

	public String getGivenValue() {
		return givenValue;
	}

	public void setGivenValue(String givenValue) {
		this.givenValue = givenValue;
	}

	public byte getAgentFlag() {
		return agentFlag;
	}

	public void setAgentFlag(byte agentFlag) {
		this.agentFlag = agentFlag;
	}

	public byte getMorelatetoMtFlag() {
		return morelatetoMtFlag;
	}

	public void setMorelatetoMtFlag(byte morelatetoMtFlag) {
		this.morelatetoMtFlag = morelatetoMtFlag;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public byte getReportFlag() {
		return reportFlag;
	}

	public void setReportFlag(byte reportFlag) {
		this.reportFlag = reportFlag;
	}

	public byte getMessageCoding() {
		return messageCoding;
	}

	public void setMessageCoding(byte messageCoding) {
		this.messageCoding = messageCoding;
	}

	public byte getMessageType() {
		return messageType;
	}

	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}

	public int getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public byte getTpPid() {
		return tpPid;
	}

	public void setTpPid(byte tpPid) {
		this.tpPid = tpPid;
	}

	public byte getTpUdhi() {
		return tpUdhi;
	}

	public void setTpUdhi(byte tpUdhi) {
		this.tpUdhi = tpUdhi;
	}

	public String getMsgContent() {
		return sm.getMessage();
	}

	public byte getMsgFormat() {
		return sm.getMsgFormat();
	}
	
	public byte getMsgLength() {
		return (byte)sm.getLength();
	}
	
	public void setSm(ShortMessage sm) {
		this.sm = sm;
	}
	
	public ShortMessage getSm() {
		return this.sm;
	}
	
	public String toString() {
		return new StringBuilder().append("Sequence_Id=").append(Hex.encodeHex(header.getSequenceNumber()))
				  .append(FixedConstant.LOG_SEPARATOR).append("spNumber=").append(getSpNumber())
				  .append(FixedConstant.LOG_SEPARATOR).append("chargeNumber=").append(getChargeNumber())
				  .append(FixedConstant.LOG_SEPARATOR).append("userCount=").append(getUserCount())
				  .append(FixedConstant.LOG_SEPARATOR).append("userNumber=").append(getUserNumber()[0])
				  .append(FixedConstant.LOG_SEPARATOR).append("corpId=").append(getCorpId())
				  .append(FixedConstant.LOG_SEPARATOR).append("serviceType=").append(getServiceType())
				  .append(FixedConstant.LOG_SEPARATOR).append("feeType=").append(getFeeType())
				  .append(FixedConstant.LOG_SEPARATOR).append("feeValue=").append(getFeeValue())
				  .append(FixedConstant.LOG_SEPARATOR).append("givenValue=").append(getGivenValue())
				  .append(FixedConstant.LOG_SEPARATOR).append("agentFlag=").append(getAgentFlag())
				  .append(FixedConstant.LOG_SEPARATOR).append("morelatetoMtFlag=").append(getMorelatetoMtFlag())
				  .append(FixedConstant.LOG_SEPARATOR).append("priority=").append(getPriority())
				  .append(FixedConstant.LOG_SEPARATOR).append("expireTime=").append(getExpireTime())
				  .append(FixedConstant.LOG_SEPARATOR).append("scheduleTime=").append(getScheduleTime())
				  .append(FixedConstant.LOG_SEPARATOR).append("reportFlag=").append(getReportFlag())
				  .append(FixedConstant.LOG_SEPARATOR).append("tpPid=").append(getTpPid())
				  .append(FixedConstant.LOG_SEPARATOR).append("tpUdhi=").append(getTpUdhi())
				  .append(FixedConstant.LOG_SEPARATOR).append("messageCoding=").append(getMessageCoding())
				  .append(FixedConstant.LOG_SEPARATOR).append("messageType=").append(getMessageType())
				  .append(FixedConstant.LOG_SEPARATOR).append("messageLength=").append(getMessageLength())
				  .append(FixedConstant.LOG_SEPARATOR).append("messageContent=").append(sm.getMessage())
				  .append(FixedConstant.LOG_SEPARATOR).append("reserve=").append(getReserve())
				  .toString();
	}
	
	public String dump() {
		String rt = "spNumber:			"+getSpNumber()
					+",chargeNumber:			"+getChargeNumber()
					+",userCount:		"+getUserCount()
					
					+",UserNumber:			"+getUserNumber()[0]
					+",CorpId:		"+getCorpId()
			
					+",ServiceType:		"+getServiceType()
					+",FeeType:		"+getFeeType()
					+",FeeValue:		"+getFeeValue()
					
					+",GivenValue:			"+getGivenValue()
					+",AgentFlag:			"+getAgentFlag()
					
					+",MorelatetoMtFlag:		"+getMorelatetoMtFlag()
					+",Priority:			"+getPriority()
					
					+",ExpireTime:			"+getExpireTime()
					+",ScheduleTime:			"+getScheduleTime()
					
					+",ReportFlag:			"+getReportFlag()
					+",TpPid:			"+getTpPid()
					
					+",TpUdhi:			"+getTpUdhi()
					+",MessageCoding:		"+getMessageCoding()
					+",MessageType:		"+getMessageType()
				
					+",MessageLength:		"+getMessageLength()
				
					+",MessageContent:		"+new String(sm.getData().getBuffer())
					+",reserve:		"+getReserve();
		return rt;
	}

	public String name() {
		return "CMPP Submit";
	}

	private String clientid;

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	
	//private String content;
	
	private int channelid;

	public int getChannelid() {
		return channelid;
	}

	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}
}


