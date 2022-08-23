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
 * 澧炲姞瀵筩mpp2.0鐨勫吋瀹�
 */
public class ConnectResp extends Response {
	
	private String systemId = "";

	public ConnectResp() {
		super(SmppConstant.CID_BIND_TRANSCEIVER_RESP);
	}

	
	/**
	 * @return systemId
	 */
	public String getSystemId() {
		return systemId;
	}
	
	/**
	 * @return setSystemId
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			setSystemId(buffer.removeCString());
		} catch (NotEnoughDataInByteBufferException | TerminatingZeroNotFoundException e) {
			throw new PDUException(e);
		}
	}

	public ByteBuffer getBody() {
		//3.0 status涓�4涓瓧鑺傦紝2.0staus涓�1涓瓧鑺�
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendCString(getSystemId());
		return buffer;
	}

	/* (non-Javadoc)
	 * @see cmpp.sms.ByteData#setData(cmpp.sms.util.ByteBuffer)
	 */
	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	/* (non-Javadoc)
	 * @see cmpp.sms.ByteData#getData()
	 */
	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(SmppConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}
	
	public String name() {
		return "SMPP ConnectResp";
	}
	
	public String dump() {
		String rt =  "\r\nnConnectResp******************************************"
					+"\r\nsystemId:		"+systemId
					+"\r\nConnectResp******************************************";
		return rt;
	}
}
