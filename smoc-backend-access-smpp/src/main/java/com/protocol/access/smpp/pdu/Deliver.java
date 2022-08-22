/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smpp.pdu;


import com.base.common.constant.FixedConstant;
import com.protocol.access.smpp.SmppConstant;
import com.protocol.access.smpp.sms.ByteBuffer;
import com.protocol.access.smpp.sms.NotEnoughDataInByteBufferException;
import com.protocol.access.smpp.sms.PDUException;
import com.protocol.access.smpp.sms.TerminatingZeroNotFoundException;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Deliver extends Request {

	private String serviceType = "";
	private byte sourceAddressTon = 0;
	private byte sourceAddressNpi = 0;
	private String sourceAddress = "";
	private byte destAddressTon =0;
	private byte destAddressNpi = 0;
	private String destinationAddress = "";
	
	private byte esmClass = 0;
	private byte protocolId = 0;
	private byte priorityFlag = 0;
	private String scheduleDeliveryTime = "";
	private String validityPeroid = "";

	private byte registeredDeliveryFlag = 0;
	private byte replaceIfPresentFlag = 0;
	private byte dataCoding = 0;
	private byte smDefaultMsgId = 0;
	private byte smLength = 0;
	private String shortMessageText = "";
	
	public Deliver() {
		super(SmppConstant.CID_DELIVER_SM);
	}

	protected Response createResponse() {
		return new DeliverResp();
	}

	public void setBody(ByteBuffer buffer)
		throws PDUException {
		try {
			serviceType = buffer.removeCString();
			sourceAddressTon = buffer.removeByte();
			sourceAddressNpi = buffer.removeByte();
			sourceAddress = buffer.removeCString();
			destAddressTon = buffer.removeByte();
			destAddressNpi = buffer.removeByte();
			destinationAddress = buffer.removeCString();
			esmClass = buffer.removeByte();
			
			protocolId = buffer.removeByte();
			priorityFlag = buffer.removeByte();
			scheduleDeliveryTime = buffer.removeCString();
			validityPeroid = buffer.removeCString();
			registeredDeliveryFlag = buffer.removeByte();
			replaceIfPresentFlag = buffer.removeByte();
			dataCoding = buffer.removeByte();
			smDefaultMsgId = buffer.removeByte();
			logger.info("pdu smDefaultMsgId :"+smDefaultMsgId);
			smLength = buffer.removeByte();
			
			int msgLength = smLength < 0 ? smLength + 256 : smLength;
			shortMessageText = buffer.removeStringEx(msgLength);

		} catch (NotEnoughDataInByteBufferException e) {
			throw new PDUException(e);
		} catch (TerminatingZeroNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
	}

	public ByteBuffer getBody() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendCString(serviceType);
		buffer.appendByte(sourceAddressTon);
		buffer.appendByte(sourceAddressNpi);
		buffer.appendCString(sourceAddress);
		buffer.appendByte(destAddressTon);
		buffer.appendByte(destAddressNpi);
		buffer.appendCString(destinationAddress);
		buffer.appendByte(esmClass);
		buffer.appendByte(protocolId);
		buffer.appendByte(priorityFlag);
		buffer.appendCString(scheduleDeliveryTime);
		buffer.appendCString(validityPeroid);
		buffer.appendByte(registeredDeliveryFlag);
		buffer.appendByte(replaceIfPresentFlag);
		buffer.appendByte(dataCoding);
		buffer.appendByte(smDefaultMsgId);
		buffer.appendByte(smLength);
		buffer.appendString(shortMessageText,shortMessageText.getBytes().length);
		return buffer;
	}
	
	public ByteBuffer getData() {
		ByteBuffer bodyBuf = getBody();
		header.setCommandLength(SmppConstant.PDU_HEADER_SIZE + bodyBuf.length());
		ByteBuffer buffer = header.getData();
		buffer.appendBuffer(bodyBuf);
		return buffer;
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
		setBody(buffer);
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public byte getSourceAddressTon() {
		return sourceAddressTon;
	}

	public void setSourceAddressTon(byte sourceAddressTon) {
		this.sourceAddressTon = sourceAddressTon;
	}

	public byte getSourceAddressNpi() {
		return sourceAddressNpi;
	}

	public void setSourceAddressNpi(byte sourceAddressNpi) {
		this.sourceAddressNpi = sourceAddressNpi;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public byte getDestAddressTon() {
		return destAddressTon;
	}

	public void setDestAddressTon(byte destAddressTon) {
		this.destAddressTon = destAddressTon;
	}

	public byte getDestAddressNpi() {
		return destAddressNpi;
	}

	public void setDestAddressNpi(byte destAddressNpi) {
		this.destAddressNpi = destAddressNpi;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public byte getEsmClass() {
		return esmClass;
	}

	public void setEsmClass(byte esmClass) {
		this.esmClass = esmClass;
	}

	public byte getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(byte protocolId) {
		this.protocolId = protocolId;
	}

	public byte getPriorityFlag() {
		return priorityFlag;
	}

	public void setPriorityFlag(byte priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

	public String getScheduleDeliveryTime() {
		return scheduleDeliveryTime;
	}

	public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
		this.scheduleDeliveryTime = scheduleDeliveryTime;
	}

	public String getValidityPeroid() {
		return validityPeroid;
	}

	public void setValidityPeroid(String validityPeroid) {
		this.validityPeroid = validityPeroid;
	}

	public byte getRegisteredDeliveryFlag() {
		return registeredDeliveryFlag;
	}

	public void setRegisteredDeliveryFlag(byte registeredDeliveryFlag) {
		this.registeredDeliveryFlag = registeredDeliveryFlag;
	}

	public byte getReplaceIfPresentFlag() {
		return replaceIfPresentFlag;
	}

	public void setReplaceIfPresentFlag(byte replaceIfPresentFlag) {
		this.replaceIfPresentFlag = replaceIfPresentFlag;
	}

	public byte getDataCoding() {
		return dataCoding;
	}

	public void setDataCoding(byte dataCoding) {
		this.dataCoding = dataCoding;
	}

	public byte getSmDefaultMsgId() {
		return smDefaultMsgId;
	}

	public void setSmDefaultMsgId(byte smDefaultMsgId) {
		this.smDefaultMsgId = smDefaultMsgId;
	}

	public byte getSmLength() {
		return smLength;
	}

	public void setSmLength(byte smLength) {
		this.smLength = smLength;
	}

	public String getShortMessageText() {
		return shortMessageText;
	}

	public void setShortMessageText(String shortMessageText) {
		this.shortMessageText = shortMessageText;
	}
	
	public String toDeliverStr(String messageID) {
		return new StringBuilder().append("Sequence_Id=").append(header.getSequenceNo())
				  .append(FixedConstant.LOG_SEPARATOR).append("MsgId=").append(messageID)
				  .append(FixedConstant.LOG_SEPARATOR).append("serviceType=").append(getServiceType())
				  .append(FixedConstant.LOG_SEPARATOR).append("sourceAddressTon=").append(getSourceAddressTon())
				  .append(FixedConstant.LOG_SEPARATOR).append("sourceAddressNpi=").append(getSourceAddressNpi())
				  .append(FixedConstant.LOG_SEPARATOR).append("sourceAddress=").append(getSourceAddress())
				  .append(FixedConstant.LOG_SEPARATOR).append("destAddressTon=").append(getDestAddressTon())
				  .append(FixedConstant.LOG_SEPARATOR).append("destAddressNpi=").append(getDestAddressNpi())
				  .append(FixedConstant.LOG_SEPARATOR).append("destinationAddress=").append(getDestinationAddress())
				  .append(FixedConstant.LOG_SEPARATOR).append("esmClass=").append(getEsmClass())
				  .append(FixedConstant.LOG_SEPARATOR).append("protocolId=").append(getProtocolId())
				  .append(FixedConstant.LOG_SEPARATOR).append("priorityFlag=").append(getPriorityFlag())
				  .append(FixedConstant.LOG_SEPARATOR).append("scheduleDeliveryTime=").append(getScheduleDeliveryTime())
				  .append(FixedConstant.LOG_SEPARATOR).append("validityPeroid=").append(getValidityPeroid())
				  .append(FixedConstant.LOG_SEPARATOR).append("registeredDeliveryFlag=").append(getRegisteredDeliveryFlag())
				  .append(FixedConstant.LOG_SEPARATOR).append("replaceIfPresentFlag=").append(getReplaceIfPresentFlag())
				  .append(FixedConstant.LOG_SEPARATOR).append("dataCoding=").append(getDataCoding())
				  .append(FixedConstant.LOG_SEPARATOR).append("smDefaultMsgId=").append(getSmDefaultMsgId())
				  .append(FixedConstant.LOG_SEPARATOR).append("smLength=").append(getSmLength())
				  .append(FixedConstant.LOG_SEPARATOR).append("shortMessageText=").append(getShortMessageText())
				  .toString();
	}
	
	public String toMOStr() {
		return new StringBuilder().append("Sequence_Id=").append(header.getSequenceNo())
				  .append(FixedConstant.LOG_SEPARATOR).append("serviceType=").append(getServiceType())
				  .append(FixedConstant.LOG_SEPARATOR).append("sourceAddressTon=").append(getSourceAddressTon())
				  .append(FixedConstant.LOG_SEPARATOR).append("sourceAddressNpi=").append(getSourceAddressNpi())
				  .append(FixedConstant.LOG_SEPARATOR).append("sourceAddress=").append(getSourceAddress())
				  .append(FixedConstant.LOG_SEPARATOR).append("destAddressTon=").append(getDestAddressTon())
				  .append(FixedConstant.LOG_SEPARATOR).append("destAddressNpi=").append(getDestAddressNpi())
				  .append(FixedConstant.LOG_SEPARATOR).append("destinationAddress=").append(getDestinationAddress())
				  .append(FixedConstant.LOG_SEPARATOR).append("esmClass=").append(getEsmClass())
				  .append(FixedConstant.LOG_SEPARATOR).append("protocolId=").append(getProtocolId())
				  .append(FixedConstant.LOG_SEPARATOR).append("priorityFlag=").append(getPriorityFlag())
				  .append(FixedConstant.LOG_SEPARATOR).append("scheduleDeliveryTime=").append(getScheduleDeliveryTime())
				  .append(FixedConstant.LOG_SEPARATOR).append("validityPeroid=").append(getValidityPeroid())
				  .append(FixedConstant.LOG_SEPARATOR).append("registeredDeliveryFlag=").append(getRegisteredDeliveryFlag())
				  .append(FixedConstant.LOG_SEPARATOR).append("replaceIfPresentFlag=").append(getReplaceIfPresentFlag())
				  .append(FixedConstant.LOG_SEPARATOR).append("dataCoding=").append(getDataCoding())
				  .append(FixedConstant.LOG_SEPARATOR).append("smDefaultMsgId=").append(getSmDefaultMsgId())
				  .append(FixedConstant.LOG_SEPARATOR).append("smLength=").append(getSmLength())
				  .append(FixedConstant.LOG_SEPARATOR).append("shortMessageText=").append(getShortMessageText())
				  .toString();
	}

	public String dump() {
		String rt =  "serviceType:			"+getServiceType()
					+",sourceAddressTon:			"+getSourceAddressTon()
					+",sourceAddressNpi:			"+getSourceAddressNpi()
					+",sourceAddress:		"+getSourceAddress()
					
					+",destAddressTon:			"+getDestAddressTon()
					+",destAddressNpi:		"+getDestAddressNpi()
			
					+",destinationAddress:		"+getDestinationAddress()
					+",esmClass:		"+getEsmClass()
					+",protocolId:		"+getProtocolId()
					
					+",priorityFlag:			"+getPriorityFlag()
					+",scheduleDeliveryTime:			"+getScheduleDeliveryTime()
					
					+",validityPeroid:		"+getValidityPeroid()
					+",registeredDeliveryFlag:			"+getRegisteredDeliveryFlag()
					
					+",replaceIfPresentFlag:			"+getReplaceIfPresentFlag()
					+",dataCoding:			"+getDataCoding()
					
					+",smDefaultMsgId:			"+getSmDefaultMsgId()
					+",smLength:			"+getSmLength()
					
					+",shortMessageText:			"+getShortMessageText();
		return rt;
	}
	
	public String name() {
		return "SMPP Deliver";
	}
}
