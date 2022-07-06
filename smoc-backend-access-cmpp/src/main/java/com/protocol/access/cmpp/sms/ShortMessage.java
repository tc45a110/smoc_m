package com.protocol.access.cmpp.sms;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import com.protocol.access.util.Tools;


public class ShortMessage extends ByteData {
	private int pk_total =1;
	private int total = 1;
	private int number = 1;
	private int longsmsid = 1;
	private int tpUdhi =0;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	

	public int getLongsmsid() {
		return longsmsid;
	}

	public void setLongsmsid(int longsmsid) {
		this.longsmsid = longsmsid;
	}


	byte msgFormat = 0;

	byte[] messageData = null;
	
	String encoding = "US-ASCII";

	public ByteBuffer getData() {
		ByteBuffer buffer = null;
		buffer = new ByteBuffer(messageData);
		return buffer;
	}

	public void setMessage(byte[] messageData, byte msgFormat) {
		this.messageData = messageData;
		this.msgFormat = msgFormat;
		setMsgFormat(msgFormat);
	}

	public void setMessage(String msg, byte msgFormat) {
		setMsgFormat(msgFormat);
		try {
			this.messageData = msg.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			logger.warn("unsupportted msgFormat!", e);
		}
	}

	public String getMessage() {
		String str = "";
		try {
			//str = new String(messageData, encoding);
//			if(msgFormat == 8){
				//代表长短信
				if(tpUdhi == 1 || pk_total >1){
					byte len = messageData[0];
					//增加对7字头的区分
					byte[] head = null;
					if(len==6){
						head = new byte[7];
					}else{
						head = new byte[6];
					}
				
					byte[] body = new byte[messageData.length - head.length];
					System.arraycopy(messageData, 0, head, 0, head.length);
					System.arraycopy(messageData, head.length, body, 0, body.length);
					str = parseToString(body);
					logger.debug("str8:"+str+",length:"+str.length());
					logger.debug(msgFormat);
					logger.debug(Arrays.toString(head));
					logger.debug(Arrays.toString(body));
					logger.debug(Arrays.toString(messageData));
					if(head.length == 6){
						total = head[4];
						number = head[5];
						longsmsid = head[3];
					}else {
						total = head[5];
						number = head[6];
						byte[] array = new byte[]{ head[3], head[4],(byte)0,(byte)0};
						longsmsid = Tools.byte2int(array);
					}
				
				}else{
					str = parseToString(messageData);
				}

//			}else if(msgFormat == 4) {
//				str = Base64.encodeBase64String(messageData);
//			}else{
//				str = new String(messageData, encoding);
//			}
		}  catch (Exception e) {
			//
			logger.error(e.getMessage(),e);
		}
		
		logger.debug("编码格式:"+msgFormat+",内容:"+str+",总条数:"+total+",本条是第:"+number+"条");
		return str;
	}

	private String parseToString(byte [] body) throws UnsupportedEncodingException {
		String str = "";
		//编码为4 base64 
		if (msgFormat == 4) {
			str = Base64.encodeBase64String(messageData);
		} else {
			str = new String(body, encoding);
		}
		return str;
	}

	public int getLength() {
		return messageData == null ? 0 : messageData.length;
	}


	public String dump() {
		String rt =  "\r\nShortMessage: "
					+"\r\nmsgFormat: 	"+msgFormat
					+"\r\nmsg: 			"+getMessage();
		return rt;
	}


	public byte getMsgFormat() {
		return msgFormat;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		this.messageData = buffer.getBuffer();
	}

	public void setData(byte[] data) {
		this.messageData = data;
	}
	
	public void setMsgFormat(byte msgFormat) {
		this.msgFormat = msgFormat;
		if (msgFormat == 0) {
			encoding = "US-ASCII";
		}else if (msgFormat == 8) {
			encoding = "UnicodeBigUnmarked";
		} else if (msgFormat == 15) {
			encoding = "GBK";
		}
	}
	
	public void setSm(String msg, byte msgFormat) {
		setMsgFormat(msgFormat);
		try {
			setData(msg.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			logger.warn("msgFormat unsupportted!", e);
		}
	}

	public int getTpUdhi() {
		return tpUdhi;
	}

	public void setTpUdhi(int tpUdhi) {
		this.tpUdhi = tpUdhi;
	}

	public int getPk_total() {
		return pk_total;
	}

	public void setPk_total(int pk_total) {
		this.pk_total = pk_total;
	}
	
	
}

