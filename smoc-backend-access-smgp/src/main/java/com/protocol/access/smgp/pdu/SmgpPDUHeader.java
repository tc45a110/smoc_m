/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smgp.pdu;


import com.protocol.access.smgp.SmgpConstant;
import com.protocol.access.smgp.sms.ByteBuffer;
import com.protocol.access.smgp.sms.ByteData;
import com.protocol.access.smgp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smgp.sms.PDUException;


/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SmgpPDUHeader extends ByteData {
	private int packetLength = SmgpConstant.PDU_HEADER_SIZE;
	private int requestID = 0;
	private int sequenceID = 0;

	public ByteBuffer getData() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendInt(getPacketLength());
		buffer.appendInt(getRequestID());
		buffer.appendInt(getSequenceID());
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		try {
			packetLength = buffer.removeInt();
			requestID = buffer.removeInt();
			sequenceID = buffer.removeInt();
		} catch(NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}
	}

	public int getPacketLength() {
		return packetLength;
	}

	public void setPacketLength(int packetLength) {
		this.packetLength = packetLength;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public int getSequenceID() {
		return sequenceID;
	}

	public void setSequenceID(int sequenceID) {
		this.sequenceID = sequenceID;
	}

	
}
