/*
 * Created on 2005-5-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.smgp.pdu;

import com.protocol.access.smgp.SmgpConstant;
import com.protocol.access.smgp.sms.ByteBuffer;
import com.protocol.access.smgp.sms.PDUException;
import com.protocol.access.smgp.sms.SmsObject;


/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SmgpPDUParser extends SmsObject {
	
	public static SmgpPDU createPDUFromBuffer(ByteBuffer buffer,int version) {
		SmgpPDU pdu = null;
		SmgpPDUHeader pduHeader = new SmgpPDUHeader();
		try {
			pduHeader.setData(buffer);
			switch(pduHeader.getRequestID()) {
			case SmgpConstant.RID_SUBMIT_RESP:
				SubmitResp submitResp = new SubmitResp();
				submitResp.header = pduHeader;
				submitResp.setBody(buffer);
				pdu = submitResp;
				break;
			case SmgpConstant.RID_DELIVER:
				Deliver deliver = new Deliver();
				deliver.header = pduHeader;
				deliver.setBody(buffer);
				pdu = deliver;
				break;
			case SmgpConstant.RID_ACTIVE_TEST:
				ActiveTest activeTest = new ActiveTest();
				activeTest.header = pduHeader;
				pdu = activeTest;
				break;
			case SmgpConstant.RID_ACTIVE_TEST_RESP:
				ActiveTestResp activeTestResp = new ActiveTestResp();
				activeTestResp.header = pduHeader;
				pdu = activeTestResp;
				break;
			case SmgpConstant.RID_DELIVER_RESP:
				DeliverResp deliverResp;
				deliverResp = new DeliverResp();
				deliverResp.header = pduHeader;
				deliverResp.setBody(buffer);
				pdu = deliverResp;
				break;
			case SmgpConstant.RID_SUBMIT:
				Submit submit ;
				submit = new Submit();
				submit.header = pduHeader;
				submit.setBody(buffer);
				pdu = submit;
				break;
			case SmgpConstant.RID_QUERY:
				Query query  = new Query();
				query.header = pduHeader;
				query.setBody(buffer);
				pdu = query;
				break;
			case SmgpConstant.RID_QUERY_RESP:
				QueryResp queryResp  = new QueryResp();
				queryResp.header = pduHeader;
				queryResp.setBody(buffer);
				pdu = queryResp;
				break;
			case SmgpConstant.RID_LOGIN:
				Connect login = new Connect();
				login.header = pduHeader;
				login.setBody(buffer);
				pdu = login;
				break;
			case SmgpConstant.RID_LOGIN_RESP:
				ConnectResp loginResp = new ConnectResp();
				loginResp.header = pduHeader;
				loginResp.setBody(buffer);
				pdu = loginResp;
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
