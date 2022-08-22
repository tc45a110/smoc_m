/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.sgip.pdu;


import com.protocol.access.sgip.sms.ByteBuffer;
import com.protocol.access.sgip.sms.ByteData;
import com.protocol.access.sgip.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.sgip.sms.PDUException;

import com.base.common.util.DateUtil;
import com.protocol.access.sgip.SgipConstant;


public class SgipPDUHeader extends ByteData {
	private int commandLength = SgipConstant.PDU_HEADER_SIZE;
	private int commandId = 0;
	private byte[] sequenceNumber;

	public ByteBuffer getData() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendInt(getCommandLength());
		buffer.appendInt(getCommandId());
		buffer.appendBytes(sequenceNumber);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		try {
			commandLength = buffer.removeInt();
			commandId = buffer.removeInt();
			sequenceNumber = buffer.removeBytes(12).getBuffer();
		} catch(NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}
	}

	public int getCommandLength() {
		return commandLength;
	}

	public int getCommandId() {
		return commandId;
	}
	
	public void setSequenceId(int sequenceId) {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendInt(0);
		buffer.appendInt(Integer.valueOf(DateUtil.getCurDateTime("MMddhhmmss")));
		buffer.appendInt(sequenceId);
		sequenceNumber = buffer.getBuffer();
	}

	public byte[] getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(byte[] sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public void setCommandLength(int cmdLen) {
		commandLength = cmdLen;
	}

	public void setCommandId(int cmdId) {
		commandId = cmdId;
	}

}
