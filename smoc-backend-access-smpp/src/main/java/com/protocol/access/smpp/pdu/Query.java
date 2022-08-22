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
public class Query extends Request {

	private String messageId = "";
	private byte ton = 0x00;
	private byte npi = 0x00;
	private String address = "";
	
	protected Response createResponse() {
		return new QueryResp();
	}

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
	
	private ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(messageId, 9);
		buffer.appendByte(ton);
		buffer.appendByte(npi);
		buffer.appendString(address, 21);
		return buffer;
	}

	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			messageId = buffer.removeStringEx(9);
			ton = buffer.removeByte();
			npi = buffer.removeByte();
			address = buffer.removeStringEx(21);
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

	public byte getTon() {
		return ton;
	}

	public void setTon(byte ton) {
		this.ton = ton;
	}

	public byte getNpi() {
		return npi;
	}

	public void setNpi(byte npi) {
		this.npi = npi;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String name() {
		return "SMPP Query";
	}
}
