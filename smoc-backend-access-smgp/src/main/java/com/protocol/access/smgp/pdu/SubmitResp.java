/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smgp.pdu;

import com.protocol.access.smgp.sms.ByteBuffer;
import com.protocol.access.smgp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smgp.sms.PDUException;
import com.protocol.access.smgp.SmgpConstant;

/**
 * @author lucien
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class SubmitResp extends Response {

	private byte[] msgId = new byte[10];
	private int status = 0;

	public SubmitResp() {
		super(SmgpConstant.RID_SUBMIT_RESP);
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

	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			msgId = buffer.removeBytes(10).getBuffer();
			status = buffer.removeInt();
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(msgId);
		buffer.appendInt(status);
		return buffer;
	}

	public byte[] getMsgId() {
		return msgId;
	}

	public void setMsgId(byte[] msgId) {
		this.msgId = msgId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String name() {
		return "SMGP SubmitResp";
	}

	public String dump() {
		String rt = "\r\nSubmitResp********************************" + "\r\nmsgId:	" + msgId + "\r\nstatus:	"
				+ status + "\r\n********************************SubmitResp";
		return rt;
	}

}
