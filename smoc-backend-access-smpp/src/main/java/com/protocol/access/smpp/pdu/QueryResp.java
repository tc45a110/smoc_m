/*
 * Created on 2005-5-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smpp.pdu;


import com.protocol.access.smpp.SmppConstant;
import com.protocol.access.smpp.sms.ByteBuffer;
import com.protocol.access.smpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smpp.sms.PDUException;

/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QueryResp extends Response {

	private String messageId = "";
	private String finalDate = "";	
	private byte messageStatus = 0;	
	private byte gsmCode = 0;	

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(SmppConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}
	
	
	
	protected ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(getMessageId(),9);
		buffer.appendString(getFinalDate(), 17);
		buffer.appendByte(getMessageStatus());
		buffer.appendByte(getGsmCode());
		return buffer;
	}

	public void setBody(ByteBuffer buffer)
	throws PDUException {
		try {
			setMessageId(buffer.removeStringEx(9));
			setFinalDate(buffer.removeStringEx(17));
			setMessageStatus(buffer.removeByte());
			setGsmCode(buffer.removeByte());
		} catch (NotEnoughDataInByteBufferException e) {
			logger.error(e.getMessage(),e);
			throw new PDUException(e);
		}
	}
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}

	public byte getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(byte messageStatus) {
		this.messageStatus = messageStatus;
	}
	
	public byte getGsmCode() {
		return gsmCode;
	}

	public void setGsmCode(byte gsmCode) {
		this.gsmCode = gsmCode;
	}

	public String name() {
		return "SMPP QueryResp";
	}
}
