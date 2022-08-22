/*
 * Created on 2005-5-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.protocol.access.sgip.pdu;

import com.protocol.access.sgip.sms.ByteBuffer;
import com.protocol.access.sgip.sms.PDUException;
import com.protocol.access.sgip.sms.SmsObject;
import com.protocol.access.sgip.SgipConstant;

/**
 * @author intermax
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SgipPDUParser extends SmsObject {
	
	public static SgipPDU createPDUFromBuffer(ByteBuffer buffer) {
		SgipPDU pdu = null;
		SgipPDUHeader pduHeader = new SgipPDUHeader();
		try {
			pduHeader.setData(buffer);
			switch(pduHeader.getCommandId()) {
			case SgipConstant.SGIP_SUBMIT_RESP:
				SubmitResp submitResp = new SubmitResp();
				submitResp.header = pduHeader;
				submitResp.setBody(buffer);
				pdu = submitResp;
				break;
			case SgipConstant.SGIP_DELIVER:
				Deliver deliver = new Deliver();
				deliver.header = pduHeader;
				deliver.setBody(buffer);
				pdu = deliver;
				break;
			case SgipConstant.SGIP_UNBIND:
				Terminate terminate = new Terminate();
				terminate.header = pduHeader;
				pdu = terminate;
				break;
			case SgipConstant.SGIP_UNBIND_RESP:
				TerminateResp terminateResp = new TerminateResp();
				terminateResp.header = pduHeader;
				pdu = terminateResp;
				break;
			case SgipConstant.SGIP_DELIVER_RESP:
				DeliverResp deliverResp = new DeliverResp();
				deliverResp.header = pduHeader;
				deliverResp.setBody(buffer);
				pdu = deliverResp;
				break;
			case SgipConstant.SGIP_SUBMIT:
				Submit submit = new Submit();
				submit.header = pduHeader;
				submit.setBody(buffer);
				pdu = submit;
				break;
//			case CmppConstant.CMD_QUERY:
//				Query query  = new Query();
//				query.header = pduHeader;
//				query.setBody(buffer);
//				pdu = query;
//				break;
//			case CmppConstant.CMD_QUERY_RESP:
//				QueryResp queryResp  = new QueryResp();
//				queryResp.header = pduHeader;
//				queryResp.setBody(buffer);
//				pdu = queryResp;
//				break;
			case SgipConstant.SGIP_BIND:
				Connect login = new Connect();
				login.header = pduHeader;
				login.setBody(buffer);
				pdu = login;
				break;
			case SgipConstant.SGIP_BIND_RESP:
				ConnectResp loginResp = new ConnectResp();
				loginResp.header = pduHeader;
				loginResp.setBody(buffer);
				pdu = loginResp;
				break;
			case SgipConstant.SGIP_REPORT:
				Report report = new Report();
				report.header = pduHeader;
				report.setBody(buffer);
				pdu = report;
				break;
			case SgipConstant.SGIP_REPORT_RESP:
				ReportResp reportResp = new ReportResp();
				reportResp.header = pduHeader;
				reportResp.setBody(buffer);
				pdu = reportResp;
				break;
			default:
				logger.error("Unknown Command! Command: " + pduHeader.getCommandId() + "! PDU Header: "+ pduHeader.getData().getHexDump());
				break;
			}
		} catch(PDUException e) {
			logger.error("Error parsing PDU: ", e);
		}
		return pdu;
	}
}
