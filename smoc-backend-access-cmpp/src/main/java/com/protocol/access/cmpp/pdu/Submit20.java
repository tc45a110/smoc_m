/**
 * @desc
 * @author ma
 * @date 2017年11月2日
 * 
 */
package com.protocol.access.cmpp.pdu;

import java.io.UnsupportedEncodingException;

import com.protocol.access.cmpp.sms.ByteBuffer;
import com.protocol.access.cmpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.cmpp.sms.PDUException;

public class Submit20 extends Submit{

	@Override
	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			setMsgId(buffer.removeBytes(8).getBuffer());
			setPkTotal(buffer.removeByte()); 
			setPkNumber(buffer.removeByte());
			setNeedReport(buffer.removeByte()); 
			setPriority(buffer.removeByte());
			setServiceId(buffer.removeStringEx(10));
			setFeeUserType(buffer.removeByte());
			setFeeTermId( buffer.removeStringEx(21));
			//feeTermType = buffer.removeByte();
			setTpPid(buffer.removeByte());
			setTpUdhi(buffer.removeByte());
			byte msgFormat = buffer.removeByte();
			setMsgSrc(buffer.removeStringEx(6));
			setFeeType( buffer.removeStringEx(2));
			setFeeCode(buffer.removeStringEx(6));
			setValidTime(buffer.removeStringEx(17));
			setAtTime(buffer.removeStringEx(17));
			setSrcId(buffer.removeStringEx(21));
			setDestTermIdCount(buffer.removeByte());
			setDestTermId(new String[getDestTermIdCount()]);
			for(int i = 0; i<getDestTermIdCount(); i++){
				String phoneNumber = buffer.removeStringEx(21);
				if (phoneNumber.length() > 11) {
					phoneNumber = phoneNumber.substring(phoneNumber
							.length() - 11);
				}
				getDestTermId()[i] = phoneNumber;
			}
			//destTermIdType = buffer.removeByte();
			getSm().setMsgFormat(msgFormat);
			byte signbyte = buffer.removeByte();
			int msgLength = signbyte < 0 ? signbyte + 256 : signbyte;
			setMsgLength(msgLength);
			if(msgLength == 0) {
				getSm().setData("".getBytes(getSm().getEncoding()));
				logger.info("SequenceNumber="+getSequenceNumber()+",解析内容长度为0,解析编码为"+getSm().getEncoding());
			}else {
				getSm().setData(buffer.removeBuffer(msgLength));
			}
			
			//长短信标识
			getSm().setTpUdhi(getTpUdhi());
			getSm().setPk_total(getPkTotal());
			setLinkId(buffer.removeStringEx(8));
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	protected Response createResponse() {
		return new SubmitResp20();
	}
}


