/*
 * Created on 2005-5-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smgp.pdu;


import com.protocol.access.smgp.SmgpConstant;
import com.protocol.access.smgp.sms.ByteBuffer;
import com.protocol.access.smgp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smgp.sms.PDUException;


/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Query extends Request {

	private String time = "";
	private byte queryType = 0x00;
	private String queryCode = "";
	
	protected Response createResponse() {
		return new QueryResp();
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setPacketLength(SmgpConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}
	
	private ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendString(time, 8);
		buffer.appendByte(queryType);
		buffer.appendString(queryCode, 10);
		return buffer;
	}

	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			time = buffer.removeStringEx(8);
			queryType = buffer.removeByte();
			queryCode = buffer.removeStringEx(10);
		} catch (NotEnoughDataInByteBufferException e) {
			logger.error(e.getMessage(),e);
			throw new PDUException(e);
		}
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(byte queryType) {
		this.queryType = queryType;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String name() {
		return "SMGP Query";
	}
}
