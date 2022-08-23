/*
 * Created on 2005-5-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smpp.pdu;

import com.protocol.access.smpp.pdu.SmppPDU;
import com.protocol.access.smpp.sms.ByteBuffer;
import com.protocol.access.smpp.sms.PDUException;
import com.protocol.access.smpp.sms.SmsObject;
import com.protocol.access.smpp.SmppConstant;

/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SmppPDUParser extends SmsObject {
	
	public static SmppPDU createPDUFromBuffer(ByteBuffer buffer,int version) {
		SmppPDU pdu = null;
		SmppPDUHeader pduHeader = new SmppPDUHeader();
		try {
			pduHeader.setData(buffer);
			logger.debug("pdu commonId :"+pduHeader.getCommandId());
			logger.debug("pdu :"+pduHeader.getCommandStatus());
			switch(pduHeader.getCommandId()) {
			case SmppConstant.CID_BIND_RECEIVER:case SmppConstant.CID_BIND_TRANSCEIVER:case SmppConstant.CID_BIND_TRANSMITTER:
				Connect login = new Connect();
				login.header = pduHeader;
				login.setBody(buffer);
				pdu = login;
				break;
			case SmppConstant.CID_BIND_RECEIVER_RESP:case SmppConstant.CID_BIND_TRANSCEIVER_RESP:case SmppConstant.CID_BIND_TRANSMITTER_RESP:
				ConnectResp loginResp = new ConnectResp();
				loginResp.header = pduHeader;
				loginResp.setBody(buffer);
				pdu = loginResp;
				break;
			case SmppConstant.CID_SUBMIT_SM:
				Submit submit = new Submit();
				submit.header = pduHeader;
				submit.setBody(buffer);
				pdu = submit;
				break;
			case SmppConstant.CID_SUBMIT_SM_RESP:
				SubmitResp submitResp = new SubmitResp();
				submitResp.header = pduHeader;
				submitResp.setBody(buffer);
				pdu = submitResp;
				break;
			case SmppConstant.CID_DELIVER_SM:
				Deliver deliver = new Deliver();
				deliver.header = pduHeader;
				deliver.setBody(buffer);
				pdu = deliver;
				break;
			case SmppConstant.CID_DELIVER_SM_RESP:
				DeliverResp deliverResp = new DeliverResp();
				deliverResp.header = pduHeader;
				deliverResp.setBody(buffer);
				pdu = deliverResp;
				break;
			case SmppConstant.CID_QUERY_SM:
				Query query  = new Query();
				query.header = pduHeader;
				query.setBody(buffer);
				pdu = query;
				break;
			case SmppConstant.CID_QUERY_SM_RESP:
				QueryResp queryResp  = new QueryResp();
				queryResp.header = pduHeader;
				queryResp.setBody(buffer);
				pdu = queryResp;
				break;
			case SmppConstant.CID_CANCEL_SM:
				Cancel cancel = new Cancel();
				cancel.header = pduHeader;
				cancel.setBody(buffer);
				pdu = cancel;
				break;
			case SmppConstant.CID_CANCEL_SM_RESP:
				//没有消息体
				CancelResp cancelResp = new CancelResp();
				cancelResp.header = pduHeader;
				cancelResp.setBody(buffer);
				pdu = cancelResp;
				break;
				
			case SmppConstant.CID_ENQUIRE_LINK:
				//没有消息体
				ActiveTest activeTest = new ActiveTest();
				activeTest.header = pduHeader;
				pdu = activeTest;
				break;
			case SmppConstant.CID_ENQUIRE_LINK_RESP:
				//没有消息体
				ActiveTestResp activeTestResp = new ActiveTestResp();
				activeTestResp.header = pduHeader;
				pdu = activeTestResp;
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
