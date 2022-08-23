/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smgp.pdu;

import com.protocol.access.smgp.sms.PDU;


/**
 * @author lucien
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class SmgpPDU extends PDU  {

	private static int sequenceNumber = 0;

	private boolean sequenceNumberChanged = false;

	public SmgpPDUHeader header = null;

	public SmgpPDU() {
		header = new SmgpPDUHeader();
	}

	public SmgpPDU(int commandId) {
		header = new SmgpPDUHeader();
		header.setRequestID(commandId);
	}

	/** Checks if the header field is null and if not, creates it. */
	private void checkHeader() {
		if (header == null) {
			header = new SmgpPDUHeader();
		}
	}

	public int getPacketLength() {
		checkHeader();
		return header.getPacketLength();
	}

	public int getRequestID() {
		checkHeader();
		return header.getRequestID();
	}

	public int getSequenceID() {
		checkHeader();
		return header.getSequenceID();
	}

	public void setPacketLength(int cmdLen) {
		checkHeader();
		header.setPacketLength(cmdLen);
	}

	public void setRequestID(int cmdId) {
		checkHeader();
		header.setRequestID(cmdId);
	}

	public void setSequenceID(int seqNr) {
		checkHeader();
		header.setSequenceID(seqNr);
	}

	public void assignSequenceNumber() {
		assignSequenceNumber(false);
	}

	public void assignSequenceNumber(boolean always) {
		if ((!sequenceNumberChanged) || always) {
			synchronized (this) {
				setSequenceID(++sequenceNumber);
			}
			sequenceNumberChanged = true;
		}
	}

	public void resetSequenceNumber() {
		setSequenceID(0);
		sequenceNumberChanged = false;
	}

	public boolean equals(Object object) {
		if ((object != null) && (object instanceof SmgpPDU)) {
			SmgpPDU pdu = (SmgpPDU) object;
			return pdu.getPacketLength() == getPacketLength();
		} else {
			return false;
		}
	}

	public String getSequenceNumberAsString() {
		int data = header.getSequenceID();
		byte[] intBuf = new byte[4];
		intBuf[3] = (byte) (data & 0xff);
		intBuf[2] = (byte) ((data >>> 8) & 0xff);
		intBuf[1] = (byte) ((data >>> 16) & 0xff);
		intBuf[0] = (byte) ((data >>> 24) & 0xff);
		return new String(intBuf);
	}

	public abstract boolean isRequest();

	public abstract boolean isResponse();

	public String dump() {
		return name() + " dump() unimplemented";
	}


}
