/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.sgip.pdu;


import com.protocol.access.sgip.sms.ByteBuffer;
import com.protocol.access.sgip.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.sgip.sms.PDUException;
import com.protocol.access.sgip.sms.ShortMessage;

import org.apache.commons.codec.binary.Hex;

import com.base.common.constant.FixedConstant;
import com.protocol.access.sgip.SgipConstant;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Deliver extends Request {

	private String userNumber = "";
	private String spNumber = "";
	private byte tpPid = 0;
	private byte tpUdhi = 0;
	private byte messageCoding = 0;
	private int messageLength = 0;
	private ShortMessage sm = new ShortMessage();
	private String reserve = "";
	
	public Deliver() {
		super(SgipConstant.SGIP_DELIVER);
	}

	protected Response createResponse() {
		return new DeliverResp();
	}

	public void setBody(ByteBuffer buffer)
		throws PDUException {
		try {
			userNumber = buffer.removeStringEx(21);
			spNumber = buffer.removeStringEx(21);
			tpPid = buffer.removeByte();
			tpUdhi = buffer.removeByte();	
			messageCoding = buffer.removeByte();
			messageLength = buffer.removeInt();
			sm.setData(buffer.removeBytes(messageLength).getBuffer());
			sm.setMsgFormat(messageCoding);
			reserve = buffer.removeStringEx(8);
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		} 
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(userNumber, 21);
		buffer.appendString(spNumber, 21);
		buffer.appendByte(tpPid);
		buffer.appendByte(tpUdhi);
		buffer.appendByte(messageCoding);
		buffer.appendInt(sm.getLength());
		buffer.appendBuffer(sm.getData());
		buffer.appendString(reserve, 8);
		return buffer;
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(SgipConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public byte getMessageCoding() {
		return messageCoding;
	}

	public void setMessageCoding(byte messageCoding) {
		this.messageCoding = messageCoding;
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
	
	public ShortMessage getSm() {
		return sm;
	}

	public void setSm(ShortMessage sm) {
		this.sm = sm;
	}
	
	public String toString() {
		return new StringBuilder().append("sequenceNumber=").append(Hex.encodeHex(header.getSequenceNumber()))
				  .append(FixedConstant.LOG_SEPARATOR).append("userNumber=").append(getUserNumber())
				  .append(FixedConstant.LOG_SEPARATOR).append("spNumber=").append(getSpNumber())
				  .append(FixedConstant.LOG_SEPARATOR).append("tpPid=").append(getTpPid())
				  .append(FixedConstant.LOG_SEPARATOR).append("tpUdhi=").append(getTpUdhi())
				  .append(FixedConstant.LOG_SEPARATOR).append("messageCoding=").append(getMessageCoding())
				  .append(FixedConstant.LOG_SEPARATOR).append("messageLength=").append(getMessageLength())
				  .append(FixedConstant.LOG_SEPARATOR).append("messageContent=").append(sm.getMessage())
				  .append(FixedConstant.LOG_SEPARATOR).append("reserve=").append(getReserve()).toString();
	}

	public String dump() {
		String rt =  "\r\nDeliver.dump***************************************"
					+"\r\nuserNumber:		"+userNumber
					+"\r\nmsgId:		"+spNumber
					+"\r\ntpPid:		"+getTpPid()
					+"\r\ntpUdhi:		"+getTpUdhi()
					+"\r\nMessageCoding:	"+getMessageCoding()
					+"\r\nMessageLength:	"+sm.getData().length()
					+"\r\nMessageContent:		"+ new String(sm.getData().getBuffer())
					+"\r\nReserve:	" + getReserve()
					+"\r\n***************************************Deliver.dump";
		return rt;
	}
	
	public String name() {
		return "CMPP Deliver";
	}
}
