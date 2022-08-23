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
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Connect extends Request  {
	
	private byte loginType = (byte)0x00;
	private String loginName = "";
	private String loginPassword = "";
	private String reserve = "";

	public Connect() {
		super(SgipConstant.SGIP_BIND);
	}

	public void setBody(ByteBuffer buffer) throws PDUException {
		try {
			setLoginType(buffer.removeByte());
			setLoginName(buffer.removeStringEx(16));
			setLoginPassword(buffer.removeStringEx(16));
			setReserve(buffer.removeStringEx(8));
		} catch (NotEnoughDataInByteBufferException e) {
			logger.error(e.getMessage(),e);
			throw new PDUException(e);
		}
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendByte(getLoginType());
		buffer.appendString(getLoginName(), 16);
		buffer.appendString(getLoginPassword(), 16);
		buffer.appendString(getReserve(), 8);
		return buffer;
	}

	public static String getHexDump(byte[] data) {
		String dump = "";
		try {
			int dataLen = data.length;
			for (int i = 0; i < dataLen; i++) {
				dump += Character.forDigit((data[i] >> 4) & 0x0f, 16);
				dump += Character.forDigit(data[i] & 0x0f, 16);
			}
		} catch (Throwable t) {
			// catch everything as this is for debug
			dump = "Throwable caught when dumping = " + t;
		}
		return dump;
	}

	public byte getLoginType() {
		return loginType;
	}


	public void setLoginType(byte loginType) {
		this.loginType = loginType;
	}


	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public String getLoginPassword() {
		return loginPassword;
	}


	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}


	public String getReserve() {
		return reserve;
	}


	public void setReserve(String reserve) {
		this.reserve = reserve;
	}


	protected Response createResponse() {
		return new ConnectResp();
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
				.setCommandLength(SgipConstant.PDU_HEADER_SIZE
						+ bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public String name() {
		return "SGIP Connect";
	}

//	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
//		out.write(this.getData().getBuffer());
//	}
//
//	private void readObject(java.io.ObjectInputStream in) throws IOException,
//			ClassNotFoundException {
//		int length = in.readInt();
//		byte[] bytemsg = new byte[length - 4];
//		byte[] bytes = new byte[length];
//		in.read(bytemsg);
//		System.arraycopy(bytes, 0, length, 0, 4); 
//		System.arraycopy(bytes, 4, bytemsg, 0, bytemsg.length);
//		try {
//			this.setData(new ByteBuffer(bytes));
//		} catch (PDUException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public String dump() {
		String rt = "\r\nBind************************************"
				+ "\r\nloginType:		" + loginType + "\r\nloginName:	"
				+ loginName + "\r\nloginPassword:		" + loginPassword
				+ "\r\nreserve:	" + reserve
				+ "\r\n************************************Bind";
		return rt;
	}
}
