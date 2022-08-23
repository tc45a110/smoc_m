/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.sgip.pdu;


import com.protocol.access.sgip.sms.ByteBuffer;
import com.protocol.access.sgip.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.sgip.sms.PDUException;
import com.protocol.access.sgip.SgipConstant;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 增加对cmpp2.0的兼�?
 */
public class ConnectResp extends Response {
	
	private byte result = (byte)0x00;
	private String reserve = "";
	
	public ConnectResp() {
		super(SgipConstant.SGIP_BIND_RESP);
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}



	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			setResult(buffer.removeByte());
			setReserve(buffer.removeStringEx(8));
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}
	}

	public ByteBuffer getBody() {
		//3.0 status�?4个字节，2.0staus�?1个字�?
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendByte(getResult());
		buffer.appendString(getReserve(), 8);
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
		header.setCommandLength(SgipConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}
	
	public String name() {
		return "SGIP ConnectResp";
	}
	
	public String dump() {
		String rt =  "\r\nBindResp******************************************"
					+"\r\nresult:		"+result
					+"\r\nreserve:	"+reserve
					+"\r\nBindResp******************************************";
		return rt;
	}
}
