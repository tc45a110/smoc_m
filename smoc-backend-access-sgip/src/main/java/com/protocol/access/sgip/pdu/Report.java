/**
 * @desc
 * @author ma
 * @date 2017�?11�?2�?
 * 
 */
package com.protocol.access.sgip.pdu;

import com.protocol.access.sgip.sms.ByteBuffer;
import com.protocol.access.sgip.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.sgip.sms.PDUException;

import org.apache.commons.codec.binary.Hex;

import com.base.common.constant.FixedConstant;
import com.protocol.access.sgip.SgipConstant;


public class Report extends Request {
	private byte[] submitSequenceNum = null;
	private byte reportType = 0;
	private String userNumber = "";
	private byte state = 0;
	private byte errorCode = 0;
	private String reserve = "";
	
	private com.protocol.access.vo.Report report;
	
	public Report() {
		super(SgipConstant.SGIP_REPORT);
	}

	protected Response createResponse() {
		return new DeliverResp();
	}

	public void setBody(ByteBuffer buffer)
		throws PDUException {
		try {
			submitSequenceNum = buffer.removeBytes(12).getBuffer();
			reportType = buffer.removeByte();
			userNumber = buffer.removeStringEx(21);
			state = buffer.removeByte();
			errorCode = buffer.removeByte();
			reserve = buffer.removeStringEx(8);
		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		} 
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(submitSequenceNum, 12);
		buffer.appendByte(reportType);
		buffer.appendString(userNumber,21);
		buffer.appendByte(state);
		buffer.appendByte(errorCode);
		buffer.appendString(reserve, 8);
		return buffer;
	}

	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(SgipConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}
	
	public com.protocol.access.vo.Report getReport() {
		return report;
	}

	public void setReport(com.protocol.access.vo.Report report) {
		this.report = report;
	}

	public byte[] getSubmitSequenceNum() {
		return submitSequenceNum;
	}

	public void setSubmitSequenceNum(byte[] submitSequenceNum) {
		this.submitSequenceNum = submitSequenceNum;
	}

	public byte getReportType() {
		return reportType;
	}

	public void setReportType(byte reportType) {
		this.reportType = reportType;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public byte getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(byte errorCode) {
		this.errorCode = errorCode;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	
	public String toString() {
		return new StringBuilder().append("Sequence_Id=").append(Hex.encodeHex(header.getSequenceNumber()))
				  .append(FixedConstant.LOG_SEPARATOR).append("submitSequenceNum=").append(Hex.encodeHex(getSubmitSequenceNum()))
				  .append(FixedConstant.LOG_SEPARATOR).append("reportType=").append(getReportType())
				  .append(FixedConstant.LOG_SEPARATOR).append("userNumber=").append(getUserNumber())
				  .append(FixedConstant.LOG_SEPARATOR).append("state=").append(getState())
				  .append(FixedConstant.LOG_SEPARATOR).append("errorCode=").append(getErrorCode())
				  .append(FixedConstant.LOG_SEPARATOR).append("reserve=").append(getReserve())
				  .toString();
	}

	public String dump() {
		String rt =  "\r\nReport.dump***************************************"
					+"\r\nsubmitSequenceNum:		"+Hex.encodeHexString(submitSequenceNum)
					+"\r\nreportType:		"+reportType
					+"\r\nuserNumber:		"+userNumber
					+"\r\nstate:	"+state
					+"\r\nerrorCode:		"+errorCode
					+"\r\nreserve:		"+reserve
					+"\r\n***************************************Report.dump";
		return rt;
	}
	
	public String name() {
		return "SGIP Report";
	}

}


