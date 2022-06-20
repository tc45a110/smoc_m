package com.protocol.access.cmpp.pdu;

import com.protocol.access.cmpp.CmppConstant;
import com.protocol.access.cmpp.sms.ByteBuffer;
import com.protocol.access.cmpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.cmpp.sms.PDUException;

public class Cancel extends Request {

	private String msgId = "";

	protected Response createResponse() {
		return new CancelResp();
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(CmppConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(msgId, 8);
		return buffer;
	}
	
	public void setBody(ByteBuffer buffer) 
	throws PDUException {
		try {
			msgId = buffer.removeStringEx(8);
		} catch (NotEnoughDataInByteBufferException e) {
			logger.error(e.getMessage(),e);
			throw new PDUException(e);
		}
	}
	
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	public String name() {
		return "CMPP Cancel";
	}
}
