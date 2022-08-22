/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smpp.pdu;

import com.protocol.access.smpp.SmppConstant;
import com.protocol.access.smpp.sms.ByteBuffer;
import com.protocol.access.smpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smpp.sms.PDUException;
import com.protocol.access.smpp.sms.TerminatingZeroNotFoundException;



/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubmitResp extends Response {

	private byte [] messageId;

	public SubmitResp() {
		super(SmppConstant.CID_SUBMIT_SM_RESP);
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(SmppConstant.PDU_HEADER_SIZE + bodyBuf.length());
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
			messageId = buffer.removeCString().getBytes();
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		} catch (TerminatingZeroNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}		
	}
	
	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(messageId);
		buffer.appendByte((byte)0);
		return buffer;
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public void setMessageId(byte[] messageId) {
		this.messageId = messageId;
	}

	public String name() {
		return "SMPP SubmitResp";
	}
	
	public String dump() {
		String rt =  "\r\nSubmitResp********************************"
					+"\r\nmessageId:	"+messageId 
					+"\r\n********************************SubmitResp";
		return rt;
	}
	
}
