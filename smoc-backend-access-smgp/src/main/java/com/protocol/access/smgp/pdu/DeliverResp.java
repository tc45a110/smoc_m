/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smgp.pdu;


import com.protocol.access.smgp.SmgpConstant;
import com.protocol.access.smgp.sms.ByteBuffer;
import com.protocol.access.smgp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smgp.sms.PDUException;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeliverResp extends Response {

	private byte[] msgID = new byte[10];
	private int status = 0;

	public DeliverResp() {
		super(SmgpConstant.RID_DELIVER_RESP);
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setPacketLength(SmgpConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}
	
	public void setBody(ByteBuffer buffer)
		throws PDUException {
		try {
			msgID = buffer.removeBytes(10).getBuffer();
			status = buffer.removeInt();
		} catch (NotEnoughDataInByteBufferException e) {
			logger.error(e.getMessage(),e);
			throw new PDUException(e);
		}
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(msgID);
		buffer.appendInt(status);
		return buffer;
	}

	/**
	 * @return Returns the msgId.
	 */
	public byte[] getMsgID() {
		return msgID;
	}
	/**
	 * @param msgId The msgId to set.
	 */
	public void setMsgID(byte[] msgId) {
		this.msgID = msgId;
	}
	/**
	 * @return Returns the result.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param result The result to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public String name() {
		return "SMGP DeliverResp";
	}
	
	public String dump() {
		String rt =  "\r\nDeliverResp*****************************"
					+"\r\nmsgId:	"+msgID
					+"\r\nresult:	"+status
					+"\r\n*****************************DeliverResp";
		return rt;
	}
}
