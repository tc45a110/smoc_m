package com.protocol.access.sgip;


import static com.protocol.access.sgip.MinaSgip.OPEN;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.util.DateUtil;
import com.protocol.access.manager.AuthCheckerManager;
import com.protocol.access.manager.AuthSubmitMessageManager;
import com.protocol.access.manager.ReportTimerTaskWorkerManager;
import com.protocol.access.manager.SessionManager;
import com.protocol.access.sgip.pdu.SgipPDU;


public class SgipIoHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(SgipIoHandler.class);
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
		SgipPDU pdu = (SgipPDU) message;
		String sequenceNumber = Hex.encodeHexString(pdu.header.getSequenceNumber());
		CategoryLog.connectionLogger.info("MESSAGE:CommandId={},SequenceNumber={},sessionid={},ip={}" ,String.valueOf(pdu.header.getCommandId()),sequenceNumber,sid,ip);
			switch (pdu.header.getCommandId()) {
			//建立连接
			case SgipConstant.SGIP_BIND:
				com.protocol.access.sgip.pdu.Connect con = (com.protocol.access.sgip.pdu.Connect) pdu;
				com.protocol.access.sgip.pdu.ConnectResp conresp = (com.protocol.access.sgip.pdu.ConnectResp) con.getResponse();
				logger.debug("CONNECT:LoginType={},loginName={},loginPassword={}",con.getLoginType(),con.getLoginName(),con.getLoginPassword());
				
				int status = AuthCheckerManager.getInstance().authClient(sa.getAddress().getHostAddress(), con.getLoginName(),
						con.getLoginPassword(), con.getLoginType());
				if(status == 0){
					SessionManager.getInstance().put(con.getLoginName(), session, (byte)1);
				}
				conresp.setResult((byte)status);
				conresp.setReserve("");
				//应答响应
				session.write(conresp);
				CategoryLog.connectionLogger.info(
						new StringBuilder(DateUtil.getCurDateTime()).append("ConnectResp")
						.append("{}sequenceID={}")
						.append("{}loginName={}")
						.append("{}loginPassword={}")
						.append("{}loginType={}")
						.append("{}session={}")
						.append("{}status={}").toString()
						,
						FixedConstant.LOG_SEPARATOR,sequenceNumber,
						FixedConstant.LOG_SEPARATOR,con.getLoginName(),
						FixedConstant.LOG_SEPARATOR,con.getLoginPassword(),
						FixedConstant.LOG_SEPARATOR,con.getLoginType(),
						FixedConstant.LOG_SEPARATOR,session.getId(),
						FixedConstant.LOG_SEPARATOR,status
						);
				if(status != 0){
					session.closeOnFlush();
				}
				break;
			case SgipConstant.SGIP_UNBIND:
				com.protocol.access.sgip.pdu.Terminate terminate = (com.protocol.access.sgip.pdu.Terminate) pdu;
				com.protocol.access.sgip.pdu.TerminateResp terminateResp = (com.protocol.access.sgip.pdu.TerminateResp) terminate
						.getResponse();
				CategoryLog.connectionLogger.info(
						new StringBuilder(DateUtil.getCurDateTime()).append("RESPONSE(sgip_unbind)")
						.append("{}commandID={}")
						.append("{}sessionID={}").toString()
						,
						FixedConstant.LOG_SEPARATOR,SgipConstant.SGIP_UNBIND_RESP,
						FixedConstant.LOG_SEPARATOR,session.getId()
						);
				session.write(terminateResp);
				//释放连接
				SessionManager.getInstance().remove(session, "UNBIND");
				session.closeNow();
				break;
			//短信提交
			case SgipConstant.SGIP_SUBMIT:
				com.protocol.access.sgip.pdu.Submit submit = (com.protocol.access.sgip.pdu.Submit) pdu;
				com.protocol.access.sgip.pdu.SubmitResp subresp = (com.protocol.access.sgip.pdu.SubmitResp) submit.getResponse();
				//sgip没有消息ID
				submit.setMsgId(sequenceNumber);
				String accountId = SessionManager.getInstance().getClient(session);	
				int subStatus = AuthSubmitMessageManager.getInstance().authSubmitMessage(session,sequenceNumber,accountId,submit);
				
				subresp.setResult((byte)subStatus);
				subresp.setReserve("");
				//应答响应
				WriteFuture future = session.write(subresp);
				future.awaitUninterruptibly();
				boolean result = future.isWritten();
				CategoryLog.connectionLogger.info(
						new StringBuilder().append("SubmitResp")
						.append("{}client={}")
						.append("{}msgid={}")
						.append("{}status={}")
						.append("{}result={}")
						.append("{}sequenceID={}")
						.append("{}响应耗时{}毫秒").toString(),
						FixedConstant.LOG_SEPARATOR,accountId,
						FixedConstant.LOG_SEPARATOR,sequenceNumber,
						FixedConstant.LOG_SEPARATOR,String.valueOf(subStatus),
						FixedConstant.LOG_SEPARATOR,String.valueOf(result),
						FixedConstant.LOG_SEPARATOR,sequenceNumber,
						FixedConstant.LOG_SEPARATOR,(System.currentTimeMillis() - start)
						);
				//记录提交参数
				CategoryLog.messageLogger.info("SGIP_SUBMIT{}{}",FixedConstant.LOG_SEPARATOR,submit.toString());		
				break;
			case SgipConstant.SGIP_DELIVER_RESP:
				com.protocol.access.sgip.pdu.DeliverResp delresp = (com.protocol.access.sgip.pdu.DeliverResp) pdu;
				ReportTimerTaskWorkerManager.getInstance().removeReportTaskByResponse(sequenceNumber);
				CategoryLog.connectionLogger.info(
						new StringBuilder().append("DeliverResp")
						.append("{}sequenceID={}")
						.append("{}result={}")
						.append("{}sessionID={}").toString()
						,
						FixedConstant.LOG_SEPARATOR,sequenceNumber,
						FixedConstant.LOG_SEPARATOR,delresp.getResult(),
						FixedConstant.LOG_SEPARATOR,session.getId()
						);
				break;
			default:
				logger.warn("Unexpected PDU received! PDU Header: "
						+ pdu.header.getData().getHexDump());
				break;
			}
	}


}
