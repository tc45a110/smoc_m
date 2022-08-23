/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smpp.pdu;


import com.protocol.access.smpp.SmppConstant;
import com.protocol.access.smpp.sms.ByteBuffer;
import com.protocol.access.smpp.sms.PDUException;

/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ActiveTest extends Request {

	public ActiveTest() {
		super(SmppConstant.CID_ENQUIRE_LINK);
	}

	protected Response createResponse() {
		return new ActiveTestResp();
	}

	public void setData(ByteBuffer buffer) throws PDUException {
		header.setData(buffer);
	}

	public ByteBuffer getData() {
		return header.getData();
	}
	
	public String name() {
		return "SMPP ActiveTest";
	}
	
	
}
