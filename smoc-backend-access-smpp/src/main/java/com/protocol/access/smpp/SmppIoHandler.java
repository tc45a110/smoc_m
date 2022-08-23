package com.protocol.access.smpp;

import static com.protocol.access.smpp.MinaSmpp.OPEN;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.util.DateUtil;
import com.protocol.access.manager.ActiveManager;
import com.protocol.access.manager.AuthCheckerManager;
import com.protocol.access.manager.AuthSubmitMessageManager;
import com.protocol.access.manager.ReportTimerTaskWorkerManager;
import com.protocol.access.manager.SessionManager;

import com.protocol.access.smpp.pdu.SmppPDU;
import com.protocol.access.smpp.util.HexUtil;
import com.protocol.access.util.Tools;


/**
 * TODO: Document me !
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * 
 */
public class SmppIoHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(SmppIoHandler.class);
	public static AtomicInteger received = new AtomicInteger(0);
	public static AtomicInteger closed = new AtomicInteger(0);
	public static boolean Connect = false;
	public static boolean Firstmsg = true;


	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		if (!(cause instanceof IOException)) {
			logger.error("Exception: {}", cause);
		} else {
			logger.debug("I/O error: {}", cause.getMessage());
		}
		session.closeNow();
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.debug("Session {} is opened",session.getId());
		session.resumeRead();

	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.debug("Creation of session {}", session.getId());
		session.setAttribute(OPEN);
		session.suspendRead();
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		session.removeAttribute(OPEN);
		session.closeNow();
		logger.debug("{}> Session closed", session.getId());
		SessionManager.getInstance().remove(session,"CLOSED");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		session.closeNow();
		SessionManager.getInstance().remove(session,"IDLE");
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		session.closeNow();
		SessionManager.getInstance().remove(session,"INPUT_CLOSED");
	}


	/**
	 * 处理建立连接、提交短信应用层逻辑
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		long start = System.currentTimeMillis();
		InetSocketAddress sa = (InetSocketAddress)session.getRemoteAddress();
		String ip  = sa.getAddress().getHostAddress();
		String sid = String.valueOf(session.getId());
		SmppPDU pdu = (SmppPDU) message;
		String sequenceNumber = String.valueOf(pdu.header.getSequenceNo());
		logger.debug("MESSAGE:CommandId={},CommandStatus={},SequenceNumber={},sessionid={},ip={}" ,String.valueOf(pdu.header.getCommandId()),String.valueOf(pdu.header.getCommandStatus()),sequenceNumber,sid,ip);

		switch (pdu.header.getCommandId()) {
		// 建立连接
		case SmppConstant.CID_BIND_RECEIVER:
		case SmppConstant.CID_BIND_TRANSCEIVER:
		case SmppConstant.CID_BIND_TRANSMITTER:
			com.protocol.access.smpp.pdu.Connect con = (com.protocol.access.smpp.pdu.Connect) pdu;
			com.protocol.access.smpp.pdu.ConnectResp conresp = (com.protocol.access.smpp.pdu.ConnectResp) con
					.getResponse();

			String clientID = con.getSystemId();

			int status = AuthCheckerManager.getInstance().authClient(sa.getAddress().getHostAddress(), clientID,
					con.getPassword());
			if (status == 0) {
				if (ActiveManager.getInstance().exist(clientID)) {
					ActiveManager.getInstance().put(session, new ActiveThread(session, clientID));
				}
				SessionManager.getInstance().put(clientID, session, con.getInterfaceVersion());
			}

			if (pdu.header.getCommandId() == SmppConstant.CID_BIND_RECEIVER) {
				conresp.setCommandId(SmppConstant.CID_BIND_RECEIVER_RESP);
			} else if (pdu.header.getCommandId() == SmppConstant.CID_BIND_TRANSCEIVER) {
				conresp.setCommandId(SmppConstant.CID_BIND_TRANSCEIVER_RESP);
			} else if (pdu.header.getCommandId() == SmppConstant.CID_BIND_TRANSMITTER) {
				conresp.setCommandId(SmppConstant.CID_BIND_TRANSMITTER_RESP);
			}

			// 构建消息头状态
			conresp.setCommandStatus(status);
			conresp.setSystemId(con.getSystemId());
			// 应答响应
			session.write(conresp);
			CategoryLog.connectionLogger.info(
					new StringBuilder(DateUtil.getCurDateTime()).append("ConnectResp:")
					.append("{}sequenceID={}")
					.append("{}version={}")
					.append("{}client={}")
					.append("{}session={}")
					.append("{}status={}").toString(),
					FixedConstant.LOG_SEPARATOR, sequenceNumber, 
					FixedConstant.LOG_SEPARATOR, con.getInterfaceVersion(), 
					FixedConstant.LOG_SEPARATOR, clientID, 
					FixedConstant.LOG_SEPARATOR, session, 
					FixedConstant.LOG_SEPARATOR, status);

			break;
		case SmppConstant.CID_ENQUIRE_LINK:
			com.protocol.access.smpp.pdu.ActiveTest activeTest = (com.protocol.access.smpp.pdu.ActiveTest) pdu;
			com.protocol.access.smpp.pdu.ActiveTestResp activeTestResp = (com.protocol.access.smpp.pdu.ActiveTestResp) activeTest
					.getResponse();

			CategoryLog.connectionLogger.debug(
					new StringBuilder(DateUtil.getCurDateTime()).append("ActiveTestResp")
					.append("{}commandID={}")
					.append("{}sessionID={}")
					.append("{}body={}").toString(),
					FixedConstant.LOG_SEPARATOR,SmppConstant.CID_ENQUIRE_LINK,
					FixedConstant.LOG_SEPARATOR, session.getId(),
					FixedConstant.LOG_SEPARATOR, Arrays.toString(activeTestResp.getData().getBuffer()));
			session.write(activeTestResp);
			break;
		case SmppConstant.CID_ENQUIRE_LINK_RESP:
			com.protocol.access.smpp.pdu.ActiveTestResp activeTestRsp = (com.protocol.access.smpp.pdu.ActiveTestResp) pdu;
			activeTestRsp.dump();
			ActiveThread.lastActiveTime = System.currentTimeMillis();
			break;
		// 短信提交
		case SmppConstant.CID_SUBMIT_SM:
			com.protocol.access.smpp.pdu.Submit submit = (com.protocol.access.smpp.pdu.Submit) pdu;
			byte[] msgid = Tools.getStandardMsgID();
			submit.setMsgid(msgid);
			String accountId = SessionManager.getInstance().getClient(session);
			int subStatus = AuthSubmitMessageManager.getInstance().authSubmitMessage(session, sequenceNumber, accountId,
					submit);
			com.protocol.access.smpp.pdu.SubmitResp subresp = (com.protocol.access.smpp.pdu.SubmitResp) submit
					.getResponse();

			// 构建消息头
			subresp.setCommandStatus(subStatus);
			subresp.setMessageId(msgid);

			// 应答响应
			WriteFuture future = session.write(subresp);
			future.awaitUninterruptibly();
			boolean result = future.isWritten();
			CategoryLog.messageLogger.info("SMPP_SUBMIT{}{}",FixedConstant.LOG_SEPARATOR,submit.toString(accountId));
			CategoryLog.connectionLogger.info(
					new StringBuilder().append("SubmitResp")
					.append("{}client={}")
					.append("{}msgid={}")
					.append("{}status={}")
					.append("{}result={}")
					.append("{}sequenceID={}")
					.append("{}响应耗时{}毫秒").toString(),
					FixedConstant.LOG_SEPARATOR, accountId, 
					FixedConstant.LOG_SEPARATOR, HexUtil.convertBytesToHexString(msgid), 
					FixedConstant.LOG_SEPARATOR, String.valueOf(subStatus), 
					FixedConstant.LOG_SEPARATOR, String.valueOf(result),
					FixedConstant.LOG_SEPARATOR, sequenceNumber, 
					FixedConstant.LOG_SEPARATOR, (System.currentTimeMillis() - start));
			break;
		case SmppConstant.CID_DELIVER_SM_RESP:
			//com.protocol.access.smpp.pdu.DeliverResp delresp = (com.protocol.access.smpp.pdu.DeliverResp) pdu;
			ReportTimerTaskWorkerManager.getInstance().removeReportTaskByResponse(sequenceNumber);

			CategoryLog.connectionLogger.info(
					new StringBuilder().append("DeliverResp")
					.append("{}session={}")
					.append("{}sequenceNumber={}").toString(),
					FixedConstant.LOG_SEPARATOR, session.getId(),
					FixedConstant.LOG_SEPARATOR, sequenceNumber);
			break;
		default:
			CategoryLog.connectionLogger.warn("Unexpected PDU received! PDU Header: {}",
					pdu.header.getData().getHexDump());
			break;
		}
	}


}
