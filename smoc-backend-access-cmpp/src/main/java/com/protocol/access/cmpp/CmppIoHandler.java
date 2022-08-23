package com.protocol.access.cmpp;

import static com.protocol.access.cmpp.MinaCmpp.OPEN;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
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
import com.protocol.access.cmpp.pdu.CmppPDU;
import com.protocol.access.cmpp.sms.ByteBuffer;
import com.protocol.access.manager.ActiveManager;
import com.protocol.access.manager.AuthCheckerManager;
import com.protocol.access.manager.SessionManager;
import com.protocol.access.manager.AuthSubmitMessageManager;
import com.protocol.access.manager.ReportTimerTaskWorkerManager;
import com.protocol.access.util.Tools;
import com.protocol.access.util.TypeConvert;


public class CmppIoHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(CmppIoHandler.class);
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
	public void messageReceived(IoSession session, Object message) throws Exception {
		long start = System.currentTimeMillis();
		InetSocketAddress sa = (InetSocketAddress) session.getRemoteAddress();
		String ip = sa.getAddress().getHostAddress();
		String sid = String.valueOf(session.getId());
		CmppPDU pdu = (CmppPDU) message;
		String sequenceNumber = String.valueOf(pdu.header.getSequenceNumber());
		logger.debug(
				new StringBuilder().append("message:")
				.append("commandID={}")
				.append("{}sequenceID={}")
				.append("{}sessionID={}")
				.append("{}ip={}").toString()
				,
				String.valueOf(pdu.header.getCommandId()),
				FixedConstant.LOG_SEPARATOR,sequenceNumber,
				FixedConstant.LOG_SEPARATOR,sid,
				FixedConstant.LOG_SEPARATOR,session,
				FixedConstant.LOG_SEPARATOR,ip
				);

		switch (pdu.header.getCommandId()) {
		// 建立连接
		case CmppConstant.CMD_CONNECT:
			com.protocol.access.cmpp.pdu.Connect con = (com.protocol.access.cmpp.pdu.Connect) pdu;
			com.protocol.access.cmpp.pdu.ConnectResp conresp = (com.protocol.access.cmpp.pdu.ConnectResp) con.getResponse();
			
			int status = AuthCheckerManager.getInstance().authClient(sa.getAddress().getHostAddress(), con.getClientId(),
					con.getAuthClient(), con.getVersion(), con.getTimeStamp());
			if (status == 0) {
				if (ActiveManager.getInstance().exist(con.getClientId())) {
					ActiveManager.getInstance().put(session, new ActiveThread(session, con.getClientId()));
				}
				SessionManager.getInstance().put(con.getClientId(), session, con.getVersion());
			}
			// 构建响应参数
			conresp.setStatus(status);
			if (status == 0) {
				ByteBuffer buffer = new ByteBuffer();
				if (con.getVersion() >= 48) {
					buffer.appendInt(status);
				} else {
					buffer.appendByte((byte) status);
				}
				buffer.appendBytes(con.getAuthClient());
				buffer.appendString(con.getSharedSecret(), con.getSharedSecret().length());
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] result = md.digest(buffer.getBuffer());
				conresp.setAuthServer(new String(result));
			}

			conresp.setVersion(con.getVersion());
			// 应答响应
			CategoryLog.connectionLogger.info(
					new StringBuilder(DateUtil.getCurDateTime()).append("ConnectResp:")
					.append("sequenceID={}")
					.append("{}version={}")
					.append("{}client={}")
					.append("{}session={}")
					.append("{}status={}").toString()
					,
					sequenceNumber,
					FixedConstant.LOG_SEPARATOR,con.getVersion(),
					FixedConstant.LOG_SEPARATOR,con.getClientId(),
					FixedConstant.LOG_SEPARATOR,session,
					FixedConstant.LOG_SEPARATOR,status
					);
			session.write(conresp);
//			CategoryLog.connectionLogger.info(
//					new StringBuilder(DateUtil.getCurDateTime()).append("ConnectResp:")
//					.append("sequenceID={}")
//					.append("{}version={}")
//					.append("{}client={}")
//					.append("{}session={}")
//					.append("{}status={}").toString()
//					,
//					SequenceNumber,
//					FixedConstant.LOG_SEPARATOR,con.getVersion(),
//					FixedConstant.LOG_SEPARATOR,con.getClientId(),
//					FixedConstant.LOG_SEPARATOR,session,
//					FixedConstant.LOG_SEPARATOR,status
//					);
			
			if (status != 0) {
				session.closeOnFlush();
			}

			break;
		case CmppConstant.CMD_ACTIVE_TEST:
			com.protocol.access.cmpp.pdu.ActiveTest activeTest = (com.protocol.access.cmpp.pdu.ActiveTest) pdu;
			com.protocol.access.cmpp.pdu.ActiveTestResp activeTestResp = (com.protocol.access.cmpp.pdu.ActiveTestResp) activeTest.getResponse();
			logger.debug(
					new StringBuilder(DateUtil.getCurDateTime()).append("response(active_test):")
					.append("commandID={}")
					.append("{}sessionID={}")
					.append("{}body={}").toString()
					,
					CmppConstant.CMD_ACTIVE_TEST,
					FixedConstant.LOG_SEPARATOR,session.getId(),
					FixedConstant.LOG_SEPARATOR,session.getId(),
					FixedConstant.LOG_SEPARATOR,Arrays.toString(activeTestResp.getData().getBuffer())
					);
			session.write(activeTestResp);
			break;
		case CmppConstant.CMD_ACTIVE_TEST_RESP:
			com.protocol.access.cmpp.pdu.ActiveTestResp activeTestRsp = (com.protocol.access.cmpp.pdu.ActiveTestResp) pdu;
			activeTestRsp.dump();
			ActiveThread.lastActiveTime = System.currentTimeMillis();
			break;
		// 短信提交
		case CmppConstant.CMD_SUBMIT:
			com.protocol.access.cmpp.pdu.Submit submit = (com.protocol.access.cmpp.pdu.Submit) pdu;

			byte[] msgid = Tools.getStandardMsgID();
			submit.setMsgId(msgid);
			String accountId = SessionManager.getInstance().getClient(session);	
			int subStatus = AuthSubmitMessageManager.getInstance().authSubmitMessage(session,sequenceNumber,accountId,submit);
			com.protocol.access.cmpp.pdu.SubmitResp subresp = (com.protocol.access.cmpp.pdu.SubmitResp) submit.getResponse();

			subresp.setResult(subStatus);
			subresp.setMsgId(msgid);
			// 应答响应
			WriteFuture future = session.write(subresp);
			//future.awaitUninterruptibly();
			boolean result = future.isWritten();
			
			logger.info(
					new StringBuilder().append("SubmitResp:")
					.append("client={}")
					.append("{}msgid={}")
					.append("{}serviceId={}")
					.append("{}status={}")
					.append("{}result={}")
					.append("{}sequenceID={}")
					.append("{}响应耗时{}毫秒").toString()
					,
					accountId,
					FixedConstant.LOG_SEPARATOR,String.valueOf(TypeConvert.byte2long(msgid)),
					FixedConstant.LOG_SEPARATOR,submit.getServiceId(),
					FixedConstant.LOG_SEPARATOR,String.valueOf(subStatus),
					FixedConstant.LOG_SEPARATOR,String.valueOf(result),
					FixedConstant.LOG_SEPARATOR,sequenceNumber,
					FixedConstant.LOG_SEPARATOR,(System.currentTimeMillis() - start)
					);
			
			//记录提交参数
			submitLog(submit);
			break;
		case CmppConstant.CMD_DELIVER_RESP:
			com.protocol.access.cmpp.pdu.DeliverResp delresp = (com.protocol.access.cmpp.pdu.DeliverResp) pdu;
			String messageID = String.valueOf(TypeConvert.byte2long(delresp.getMsgId()));
			ReportTimerTaskWorkerManager.getInstance().removeReportTaskByResponse(messageID);
			
			logger.info(
					new StringBuilder().append("DeliverResp")
					.append("{}sequenceID={}")
					.append("{}session={}")
					.append("{}msgid={}").toString()
					,
					FixedConstant.LOG_SEPARATOR,pdu.header.getSequenceNumber(),
					FixedConstant.LOG_SEPARATOR,session.getId(),
					FixedConstant.LOG_SEPARATOR,messageID
					);
			break;
		default:
			CategoryLog.connectionLogger.warn("Unexpected PDU received! PDU Header: {}", pdu.header.getData().getHexDump());
			break;
		}
	}
	
	private void submitLog(com.protocol.access.cmpp.pdu.Submit submit) {
		CategoryLog.messageLogger.info(
				new StringBuilder().append("CMPP_SUBMIT")
				.append(":command_Id={}")
				.append("{}Sequence_Id={}")
				.append("{}msg_id={}")
				.append("{}pk_Total={}")
				.append("{}pk_Number={}")
				.append("{}registered_Delivery={}")
				.append("{}msg_Level={}")
				.append("{}service_Id={}")
				.append("{}fee_UserType={}")
				.append("{}fee_Terminal_Id={}")
				.append("{}fee_Terminal_type={}")
				.append("{}tp_Pid={}")
				.append("{}tp_Udhi={}")
				.append("{}msg_Fmt={}")
				.append("{}msg_Src={}")
				.append("{}fee_Type={}")
				.append("{}fee_Code={}")
				.append("{}valid_Time={}")
				.append("{}at_Time={}")
				.append("{}src_Terminal_Id={}")
				.append("{}destusr_Tl={}")
				.append("{}dest_Terminal_Id={}")
				.append("{}dest_Terminal_type={}")
				.append("{}msg_Length={}")
				.append("{}msg_Content_base64={}")
				.append("{}LinkID={}")
				.toString(),
				submit.getCommandId(),
				FixedConstant.LOG_SEPARATOR,submit.getSequenceNumber(),
				FixedConstant.LOG_SEPARATOR,String.valueOf(TypeConvert.byte2long(submit.getMsgId())),
				FixedConstant.LOG_SEPARATOR,submit.getPkTotal(),
				FixedConstant.LOG_SEPARATOR,submit.getPkNumber(),
				FixedConstant.LOG_SEPARATOR,submit.getNeedReport(),
				FixedConstant.LOG_SEPARATOR,submit.getPriority(),
				FixedConstant.LOG_SEPARATOR,submit.getServiceId(),
				FixedConstant.LOG_SEPARATOR,submit.getFeeUserType(),
				FixedConstant.LOG_SEPARATOR,submit.getFeeTermId(),
				FixedConstant.LOG_SEPARATOR,submit.getFeeTermType(),
				FixedConstant.LOG_SEPARATOR,submit.getTpPid(),
				FixedConstant.LOG_SEPARATOR,submit.getTpUdhi(),
				FixedConstant.LOG_SEPARATOR,submit.getMsgFormat(),
				FixedConstant.LOG_SEPARATOR,submit.getMsgSrc(),
				FixedConstant.LOG_SEPARATOR,submit.getFeeType(),
				FixedConstant.LOG_SEPARATOR,submit.getFeeCode(),
				FixedConstant.LOG_SEPARATOR,submit.getValidTime(),
				FixedConstant.LOG_SEPARATOR,submit.getAtTime(),
				FixedConstant.LOG_SEPARATOR,submit.getSrcId(),
				FixedConstant.LOG_SEPARATOR,submit.getDestTermIdCount(),
				FixedConstant.LOG_SEPARATOR,Arrays.toString(submit.getDestTermId()),
				FixedConstant.LOG_SEPARATOR,submit.getDestTermIdType(),
				FixedConstant.LOG_SEPARATOR,submit.getSm().getLength(),
				FixedConstant.LOG_SEPARATOR,submit.getSm().getMessage(),
				FixedConstant.LOG_SEPARATOR,submit.getLinkId()
				);
	}

}
