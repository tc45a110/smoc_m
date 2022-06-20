/*
 * Created on 2005-5-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.cmpp.pdu;

import com.protocol.access.cmpp.CmppConstant;
import com.protocol.access.cmpp.sms.ByteBuffer;
import com.protocol.access.cmpp.sms.PDUException;
import com.protocol.access.cmpp.sms.SmsObject;


/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CmppPDUParser extends SmsObject {
	
	public static CmppPDU createPDUFromBuffer(ByteBuffer buffer,int version) {
		CmppPDU pdu = null;
		CmppPDUHeader pduHeader = new CmppPDUHeader();
		try {
			pduHeader.setData(buffer);
			switch(pduHeader.getCommandId()) {
			case CmppConstant.CMD_SUBMIT_RESP:
				SubmitResp submitResp;
				if(version == CmppConstant.VERSION3){
					submitResp = new SubmitResp();
				}else{
					submitResp = new SubmitResp20();
				}
			
				submitResp.header = pduHeader;
				submitResp.setBody(buffer);
				pdu = submitResp;
				break;
			case CmppConstant.CMD_DELIVER:
				Deliver deliver = new Deliver();
				deliver.header = pduHeader;
				deliver.setBody(buffer);
				pdu = deliver;
				break;
			case CmppConstant.CMD_ACTIVE_TEST:
				ActiveTest activeTest = new ActiveTest();
				activeTest.header = pduHeader;
				pdu = activeTest;
				break;
			case CmppConstant.CMD_ACTIVE_TEST_RESP:
				ActiveTestResp activeTestResp = new ActiveTestResp();
				activeTestResp.header = pduHeader;
				pdu = activeTestResp;
				break;
			case CmppConstant.CMD_DELIVER_RESP:
				DeliverResp deliverResp;
				if(version == CmppConstant.VERSION3){
					deliverResp = new DeliverResp();
				}else{
					deliverResp = new DeliverResp20();
				}
			
				deliverResp.header = pduHeader;
				deliverResp.setBody(buffer);
				pdu = deliverResp;
				break;
			case CmppConstant.CMD_SUBMIT:
				Submit submit ;
				if(version == CmppConstant.VERSION3){
					submit = new Submit();
				}else{
					submit = new Submit20();
				}
				
				submit.header = pduHeader;
				submit.setBody(buffer);
				pdu = submit;
				break;
			case CmppConstant.CMD_QUERY:
				Query query  = new Query();
				query.header = pduHeader;
				query.setBody(buffer);
				pdu = query;
				break;
			case CmppConstant.CMD_QUERY_RESP:
				QueryResp queryResp  = new QueryResp();
				queryResp.header = pduHeader;
				queryResp.setBody(buffer);
				pdu = queryResp;
				break;
			case CmppConstant.CMD_CONNECT:
				Connect login = new Connect();
				login.header = pduHeader;
				login.setBody(buffer);
				pdu = login;
				break;
			case CmppConstant.CMD_CONNECT_RESP:
				ConnectResp loginResp = new ConnectResp();
				loginResp.header = pduHeader;
				loginResp.setBody(buffer);
				pdu = loginResp;
				break;
			case CmppConstant.CMD_CANCEL:
				Cancel cancel = new Cancel();
				cancel.header = pduHeader;
				cancel.setBody(buffer);
				pdu = cancel;
				break;
			case CmppConstant.CMD_CANCEL_RESP:
				CancelResp cancelResp = new CancelResp();
				cancelResp.header = pduHeader;
				cancelResp.setBody(buffer);
				pdu = cancelResp;
				break;
			default:
				logger.error("Unknown Command! PDU Header: "+ pduHeader.getData().getHexDump());
				break;
			}
		} catch(PDUException e) {
			logger.error("Error parsing PDU: ", e);
		}
		return pdu;
	}
}
