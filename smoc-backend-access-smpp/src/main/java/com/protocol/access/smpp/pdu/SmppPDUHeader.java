/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smpp.pdu;


import com.protocol.access.smpp.SmppConstant;
import com.protocol.access.smpp.sms.ByteBuffer;
import com.protocol.access.smpp.sms.ByteData;
import com.protocol.access.smpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smpp.sms.PDUException;


/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SmppPDUHeader extends ByteData {
	private int commandLength = SmppConstant.PDU_HEADER_SIZE;
	private int commandId = 0;
	private int commandStatus = 0;
	private int sequenceNo = 0;

	public ByteBuffer getData() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendInt(getCommandLength());
		buffer.appendInt(getCommandId());
		buffer.appendInt(getCommandStatus());
		buffer.appendInt(getSequenceNo());
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		try {
			commandLength = buffer.removeInt();
			commandId = buffer.removeInt();
			commandStatus = buffer.removeInt();
			sequenceNo = buffer.removeInt();
		} catch(NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}
	}

	public int getCommandLength() {
		return commandLength;
	}

	public void setCommandLength(int commandLength) {
		this.commandLength = commandLength;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public int getCommandStatus() {
		return commandStatus;
	}

	public void setCommandStatus(int commandStatus) {
		this.commandStatus = commandStatus;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	
	
	
}
