/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.sgip.sms;


/**
 * @author lucien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class PDU extends ByteData {

	public abstract ByteBuffer getData();
	
	public abstract void assignSequenceNumber();
	
	public abstract boolean isRequest();
	
	public abstract boolean isResponse();
	
	public abstract boolean equals(Object object);
	
	public abstract byte[] getSequenceNumber();
	
	public abstract String name();
	
	public abstract String dump();
	
	public long timeStamp = 0;
	
	public String printByteArray(byte[] bs){
		int count = 0;
		StringBuffer sb = new StringBuffer();
		for(byte b : bs){
			if(count != 0){
				sb.append(",");	
			}
			sb.append(b);	
			count++;
		}
		return sb.toString();
	}
}
