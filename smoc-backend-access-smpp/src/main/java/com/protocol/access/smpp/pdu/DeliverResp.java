/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smpp.pdu;


import org.apache.commons.codec.binary.Hex;

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
public class DeliverResp extends Response {


	private String messageId = "";

	public DeliverResp() {
		super(SmppConstant.CID_DELIVER_SM_RESP);
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
			messageId = Hex.encodeHexString(buffer.removeCString().getBytes());
		} catch (NotEnoughDataInByteBufferException | TerminatingZeroNotFoundException e) {
			throw new PDUException(e);
		}		
	}
	
	
	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(messageId,messageId.getBytes().length);
		return buffer;
	}
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
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
