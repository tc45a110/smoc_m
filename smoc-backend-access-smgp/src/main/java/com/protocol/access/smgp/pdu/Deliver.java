/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smgp.pdu;


import com.protocol.access.smgp.SmgpConstant;
import com.protocol.access.smgp.sms.ByteBuffer;
import com.protocol.access.smgp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smgp.sms.PDUException;
import com.protocol.access.smgp.sms.ShortMessage;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Deliver extends Request {

	private byte [] msgID = new byte[10];
	private byte isReport = 0;
	private byte msgFormat = 0;
	private String recvTime = "";
	private String srcTermID = "";
	private String destTermID = "";
	private ShortMessage sm = new ShortMessage();
	private String reserve = "";
	
	private byte [] tlv;
	public Deliver() {
		super(SmgpConstant.RID_DELIVER);
	}

	protected Response createResponse() {
		return new DeliverResp();
	}

	public void setBody(ByteBuffer buffer)
		throws PDUException {
		try {
			msgID = buffer.removeBytes(10).getBuffer();
			isReport = buffer.removeByte();
			msgFormat = buffer.removeByte();
			recvTime = buffer.removeStringEx(14);
			srcTermID = buffer.removeStringEx(21);
			destTermID = buffer.removeStringEx(21);
			
			byte signbyte = buffer.removeByte();
			int msgLength = signbyte < 0 ? signbyte + 256 : signbyte;
			if (msgLength>0)
				sm.setData(buffer.removeBuffer(msgLength));
			sm.setMsgFormat(msgFormat);
			reserve = buffer.removeStringEx(8);
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		} 
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(msgID, 10);
		buffer.appendByte(isReport);
		buffer.appendByte(msgFormat);
		buffer.appendString(recvTime, 14);
		buffer.appendString(srcTermID, 21);
		buffer.appendString(destTermID, 21);
		
		buffer.appendByte((byte)sm.getLength());
		buffer.appendBuffer(sm.getData());
		buffer.appendString(reserve, 8);
		if(tlv != null && tlv.length > 0) {
			buffer.appendBytes(tlv);
		}
		return buffer;
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setPacketLength(SmgpConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
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

	public byte getIsReport() {
		return isReport;
	}

	public void setIsReport(byte isReport) {
		this.isReport = isReport;
	}

	public String getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(String recvTime) {
		this.recvTime = recvTime;
	}

	public String getSrcTermID() {
		return srcTermID;
	}

	public void setSrcTermID(String srcTermID) {
		this.srcTermID = srcTermID;
	}

	public String getDestTermID() {
		return destTermID;
	}

	public void setDestTermID(String destTermID) {
		this.destTermID = destTermID;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public void setMsgFormat(byte msgFormat) {
		this.msgFormat = msgFormat;
	}

	public String getMsgContent() {
		return sm.getMessage();
	}

	public byte getMsgFormat() {
		return sm.getMsgFormat();
	}
	
	public byte getMsgLength() {
		return (byte)sm.getLength();
	}
	
	public ShortMessage getSm() {
		return sm;
	}

	public void setSm(ShortMessage sm) {
		this.sm = sm;
	}

	public String dump() {
		String rt =  "\r\nDeliver.dump***************************************"
					+"\r\nseqNo:		"+this.getPacketLength()
					+"\r\nmsgId:		"+getMsgID()
					+"\r\nisReport:		"+getIsReport()
					+"\r\nmsgFormat:	"+getMsgFormat()
					+"\r\nrecvTime:		"+getRecvTime()
					+"\r\nsrcTermID:	"+getSrcTermID()
					+"\r\ndestTermID:	"+getDestTermID()
					+"\r\nmsgLength:	"+getMsgLength()
					+"\r\nmsgContent:	"+getMsgContent()
					+"\r\nreserve:		"+getReserve()
					+"\r\n***************************************Deliver.dump";
		return rt;
	}
	
	public String name() {
		return "SMGP Deliver";
	}
}
