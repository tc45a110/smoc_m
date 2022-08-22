/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smpp.pdu;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.protocol.access.smpp.SmppConstant;
import com.protocol.access.smpp.sms.ByteBuffer;
import com.protocol.access.smpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smpp.sms.PDUException;
import com.protocol.access.smpp.sms.TerminatingZeroNotFoundException;

/**
 * @author lucien
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Connect extends Request  {
	private String systemId = "";
	private String password = "";
	private String systemType = "";
	private byte interfaceVersion = (byte) 0x00;
	private byte Ton = 0;
	private byte Npi = 0;
	private String addressRange = "";
	
	
	
//	private String clientId = "";
//	private byte[] authClient = new byte[16];
//	private byte version = (byte) 0x00;
//	private int timeStamp = 0;
//	private String sharedSecret = "";

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public byte getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(byte interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public byte getTon() {
		return Ton;
	}

	public void setTon(byte ton) {
		Ton = ton;
	}

	public byte getNpi() {
		return Npi;
	}

	public void setNpi(byte npi) {
		Npi = npi;
	}

	public String getAddressRange() {
		return addressRange;
	}

	public void setAddressRange(String addressRange) {
		this.addressRange = addressRange;
	}

	public Connect() {
		super(SmppConstant.CID_BIND_TRANSCEIVER);
	}

	public Connect(byte version) {
		super(SmppConstant.CID_BIND_TRANSCEIVER_RESP);
		setInterfaceVersion(interfaceVersion);
	}

	protected Response createResponse() {
		return new ConnectResp();
	}
	
	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			setSystemId(buffer.removeCString());
			setPassword(buffer.removeCString());
			setSystemType(buffer.removeCString());
			setInterfaceVersion(buffer.removeByte());
			setTon(buffer.removeByte());
			setNpi(buffer.removeByte());
			setAddressRange(buffer.removeCString());
		} catch (NotEnoughDataInByteBufferException e) {
			logger.error(e.getMessage(),e);
			throw new PDUException(e);
		} catch (TerminatingZeroNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
		}
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendCString(getSystemId());
		buffer.appendCString(getPassword());
		buffer.appendCString(getSystemType());
		buffer.appendByte(getInterfaceVersion());
		buffer.appendByte(getTon());
		buffer.appendByte(getNpi());
		buffer.appendCString(getAddressRange());
		return buffer;
	}

	public int genTimeStamp() {
		Date date = new Date();
		Format formatter = new SimpleDateFormat("MMddHHmmss");
		int timeStamp = Integer.parseInt(formatter.format(date), 10);
		return timeStamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cmpp.sms.ByteData#setData(cmpp.sms.util.ByteBuffer)
	 */
	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cmpp.sms.ByteData#getData()
	 */
	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header
				.setCommandLength(SmppConstant.PDU_HEADER_SIZE
						+ bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public String name() {
		return "SMPP Connect";
	}

	public String dump() {
		String rt = "\r\nLogin************************************"
				+ "\r\nsystemId:		" + systemId + "\r\npassword:	"
				+ password + "\r\nsystemType:		" + systemType
				+ "\r\ninterfaceVersion:	" + interfaceVersion + "\r\nTon:	"
				+ Ton + "\r\nNpi:	" + Npi + "\r\naddressRange:	" + addressRange
				+ "\r\n************************************Login";
		return rt;
	}
}
