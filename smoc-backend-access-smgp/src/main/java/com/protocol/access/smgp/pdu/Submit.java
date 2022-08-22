/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smgp.pdu;

import com.protocol.access.smgp.sms.ByteBuffer;
import com.protocol.access.smgp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smgp.sms.PDUException;
import com.protocol.access.smgp.sms.ShortMessage;
import com.protocol.access.smgp.SmgpConstant;


/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Submit extends Request {

	private byte [] msgID = new byte[8];
	private byte msgType = 0;
	private byte needReprot = 0;
	private byte priority = 0;
	private String serviceID = "";
	private String feeType = "";
	private String feeCode = "";
	private String fixedFee = "";
	private byte msgFormat = 0;
	private String validTime = "";
	private String atTime = "";
	private String srcTermID = "";
	private String chargeTermID = "";
	private byte destTermIdCount = 0;
	private String destTermId[] = new String[0];
	private ShortMessage sm = new ShortMessage();
	private String reserve = "";
	private int msgLength = 0 ;
	
	private byte [] tlv;
	

	public Submit() {
		super(SmgpConstant.RID_SUBMIT);
	}

	protected Response createResponse() {
		return new SubmitResp();
	}

	public void setBody(ByteBuffer buffer)
	throws PDUException {
		try {
			msgType = buffer.removeByte();
			needReprot = buffer.removeByte();
			priority = buffer.removeByte();
			serviceID = buffer.removeStringEx(10);
			feeType = buffer.removeStringEx(2);
			feeCode = buffer.removeStringEx(6);
			fixedFee = buffer.removeStringEx(6);
			msgFormat = buffer.removeByte();
			validTime = buffer.removeStringEx(17);
			atTime = buffer.removeStringEx(17);
			srcTermID = buffer.removeStringEx(21);
			chargeTermID = buffer.removeStringEx(21);
			destTermIdCount = buffer.removeByte();
			destTermId = new String[destTermIdCount];
			for(int i = 0; i<destTermIdCount; i++){
				String phoneNumber = buffer.removeStringEx(21);
				if (phoneNumber.length() > 11) {
					phoneNumber = phoneNumber.substring(phoneNumber
							.length() - 11);
				}
				destTermId[i] = phoneNumber;
			}	
			byte length = buffer.removeByte();
			
			//logger.info("length:"+length);
			msgLength = length < 0 ? length + 256 : length;
			sm.setData(buffer.removeBuffer(msgLength));
			sm.setMsgFormat(msgFormat);
			
			reserve = buffer.removeStringEx(8);
			
			//logger.info(dump());
			
			if(buffer.length() > 0) {
				sm.setTpUdhi(1);
				tlv = buffer.getBuffer();
			}
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		}
	}
	
	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendByte(msgType);
		buffer.appendByte(needReprot);
		buffer.appendByte(priority);
		buffer.appendString(serviceID, 10);
		buffer.appendString(feeType, 2);
		buffer.appendString(feeCode, 6);
		buffer.appendString(fixedFee, 6);
		buffer.appendByte(msgFormat);
		buffer.appendString(validTime, 17);
		buffer.appendString(atTime, 17);
		buffer.appendString(srcTermID, 21);
		buffer.appendString(chargeTermID, 21);
		buffer.appendByte(destTermIdCount);
		buffer.appendByte((byte)destTermId.length);
		for(int i = 0; i < destTermId.length; i++)
			buffer.appendString(destTermId[i],21);
		
		buffer.appendByte((byte)sm.getLength());
		buffer.appendBuffer(sm.getData());
		buffer.appendString(reserve, 8);
		if(tlv.length > 0) {
			buffer.appendBytes(tlv);
		}
		return buffer;
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
	
	public byte getMsgType() {
		return msgType;
	}

	public void setMsgType(byte msgType) {
		this.msgType = msgType;
	}

	public byte getNeedReprot() {
		return needReprot;
	}

	public void setNeedReprot(byte needReprot) {
		this.needReprot = needReprot;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getFixedFee() {
		return fixedFee;
	}

	public void setFixedFee(String fixedFee) {
		this.fixedFee = fixedFee;
	}

	public byte getMsgFormat() {
		return msgFormat;
	}

	public void setMsgFormat(byte msgFormat) {
		this.msgFormat = msgFormat;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getAtTime() {
		return atTime;
	}

	public void setAtTime(String atTime) {
		this.atTime = atTime;
	}

	public String getSrcTermID() {
		return srcTermID;
	}

	public void setSrcTermID(String srcTermID) {
		this.srcTermID = srcTermID;
	}

	public String getChargeTermID() {
		return chargeTermID;
	}

	public void setChargeTermID(String chargeTermID) {
		this.chargeTermID = chargeTermID;
	}

	public byte getDestTermIdCount() {
		return destTermIdCount;
	}

	public void setDestTermIdCount(byte destTermIdCount) {
		this.destTermIdCount = destTermIdCount;
	}

	public String[] getDestTermId() {
		return destTermId;
	}

	public void setDestTermId(String[] destTermId) {
		this.destTermId = destTermId;
	}

	public ShortMessage getSm() {
		return sm;
	}

	public void setSm(ShortMessage sm) {
		this.sm = sm;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public byte[] getTlv() {
		return tlv;
	}

	public void setTlv(byte[] tlv) {
		this.tlv = tlv;
	}

	public byte[] getMsgID() {
		return msgID;
	}

	public void setMsgID(byte[] msgID) {
		this.msgID = msgID;
	}

	public String dump() {
		String rt =  "msgType:			"+getMsgType()
					+",needReprot:			"+getNeedReprot()
					+",priority:			"+getPriority()
					+",serviceID:		"+getServiceID()
					
					+",feeType:			"+getFeeType()
					+",feeCode:		"+getFeeCode()
			
					+",fixedFee:		"+getFixedFee()
					+",msgFormat:		"+getMsgFormat()
					+",validTime:		"+getValidTime()
					
					+",atTime:			"+getAtTime()
					+",srcTermID:			"+getSrcTermID()
					
					+",chargeTermId:		"+getChargeTermID()
					+",destTermIdCount:			"+getDestTermIdCount()
					
					+",destTermId:			"+getDestTermId()[0]

					+",msgLength:		"+sm.getLength()
					+",msgContent:		"+sm.getMessage();
		return rt;
	}

	public String name() {
		return "SMGP Submit";
	}
	
	private String clientid;

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	
	//private String content;
	
	private int channelid;

	public int getChannelid() {
		return channelid;
	}

	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}
	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}
	
	public int getMsgContentLength() {
		return msgLength;
	}
	
}
