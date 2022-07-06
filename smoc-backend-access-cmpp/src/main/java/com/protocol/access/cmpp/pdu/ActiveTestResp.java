/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.cmpp.pdu;


import com.protocol.access.cmpp.CmppConstant;
import com.protocol.access.cmpp.sms.ByteBuffer;
import com.protocol.access.cmpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.cmpp.sms.PDUException;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ActiveTestResp extends Response {

	private byte reserve = 0x7f;
	
	public ActiveTestResp() {
		super(CmppConstant.CMD_ACTIVE_TEST_RESP);
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);		
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(CmppConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}
	
	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			reserve = buffer.removeByte();
		} catch (NotEnoughDataInByteBufferException e) {
			logger.error(e.getMessage(),e);
			throw new PDUException(e);
		}
	}
	
	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendByte(reserve);
		return buffer;
	}
	
	public String name() {
		return "CMPP ActiveTestResp";
	}
}
