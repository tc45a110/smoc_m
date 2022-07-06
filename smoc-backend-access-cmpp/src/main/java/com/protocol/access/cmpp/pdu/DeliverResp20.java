/**
 * @desc
 * @author ma
 * @date 2017年11月2日
 * 
 */
package com.protocol.access.cmpp.pdu;

import com.protocol.access.cmpp.sms.ByteBuffer;
import com.protocol.access.cmpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.cmpp.sms.PDUException;

public class DeliverResp20 extends DeliverResp{
	
	
	public void setBody(ByteBuffer buffer)
			throws PDUException {
			try {
				setMsgId(buffer.removeBytes(8).getBuffer());
				setResult(buffer.removeByte());
			} catch (NotEnoughDataInByteBufferException e) {
				logger.error(e.getMessage(),e);
				throw new PDUException(e);
			}
		}

	@Override
	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(getMsgId());
		buffer.appendByte((byte)getResult());
		return buffer;
	}
	
	
}


