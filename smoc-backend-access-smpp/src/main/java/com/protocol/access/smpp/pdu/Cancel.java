package com.protocol.access.smpp.pdu;

import com.protocol.access.smpp.SmppConstant;
import com.protocol.access.smpp.sms.ByteBuffer;
import com.protocol.access.smpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smpp.sms.PDUException;

public class Cancel extends Request {

	private String serviceType = "";
	private byte sourceAddressTon = 0;
	private byte sourceAddressNpi = 0;
	private String sourceAddress = "";
	private byte destAddressTon =0;
	private byte destAddressNpi = 0;
	private String destinationAddress = "";

	protected Response createResponse() {
		return new CancelResp();
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

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(serviceType, 6);
		buffer.appendByte(sourceAddressTon);
		buffer.appendByte(sourceAddressNpi);
		buffer.appendString(sourceAddress,21);
		buffer.appendByte(destAddressTon);
		buffer.appendByte(destAddressNpi);
		buffer.appendString(destinationAddress,21);
		return buffer;
	}
	
	public void setBody(ByteBuffer buffer) 
	throws PDUException {
		try {
			serviceType = buffer.removeStringEx(6);
			sourceAddressTon = buffer.removeByte();
			sourceAddressNpi = buffer.removeByte();
			sourceAddress = buffer.removeStringEx(21);
			destAddressTon = buffer.removeByte();
			destAddressNpi = buffer.removeByte();
			destinationAddress = buffer.removeStringEx(21);
		} catch (NotEnoughDataInByteBufferException e) {
			logger.error(e.getMessage(),e);
			throw new PDUException(e);
		}
	}
	
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public byte getSourceAddressTon() {
		return sourceAddressTon;
	}

	public void setSourceAddressTon(byte sourceAddressTon) {
		this.sourceAddressTon = sourceAddressTon;
	}

	public byte getSourceAddressNpi() {
		return sourceAddressNpi;
	}

	public void setSourceAddressNpi(byte sourceAddressNpi) {
		this.sourceAddressNpi = sourceAddressNpi;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public byte getDestAddressTon() {
		return destAddressTon;
	}

	public void setDestAddressTon(byte destAddressTon) {
		this.destAddressTon = destAddressTon;
	}

	public byte getDestAddressNpi() {
		return destAddressNpi;
	}

	public void setDestAddressNpi(byte destAddressNpi) {
		this.destAddressNpi = destAddressNpi;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String name() {
		return "SMPP Cancel";
	}
}
