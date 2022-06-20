/**
 * @desc
 * @author ma
 * @date 2017年11月2日
 * 
 */
package com.protocol.access.cmpp.pdu;

import com.protocol.access.cmpp.sms.ByteBuffer;


public class Deliver20 extends Deliver{
	protected Response createResponse() {
		return new DeliverResp20();
	}
	
	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(getMsgId(), 8);
		buffer.appendString(getDstId(), 21);
		buffer.appendString(getServiceId(), 10);
		buffer.appendByte(getTpPid());
		buffer.appendByte(getTpUdhi());
		buffer.appendByte(getSm().getMsgFormat());
		buffer.appendString(getSrcTermId(),21);
		//buffer.appendByte(srcTermType);
		buffer.appendByte(getIsReport());
		buffer.appendByte((byte)(getSm().getLength()));
		buffer.appendBuffer(getSm().getData());
		buffer.appendString(getLinkId(), 8);
		return buffer;
	}

}


