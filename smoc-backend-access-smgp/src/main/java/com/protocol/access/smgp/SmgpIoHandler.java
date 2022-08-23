package com.protocol.access.smgp;
import static com.protocol.access.smgp.MinaSmgp.OPEN;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
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
import com.protocol.access.smgp.pdu.SmgpPDU;
import com.protocol.access.smgp.sms.ByteBuffer;
import com.protocol.access.util.Tools;

/**
 * TODO: Document me !
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * 
 */
public class SmgpIoHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(SmgpIoHandler.class);
	public static AtomicInteger received = new AtomicInteger(0);
	public static AtomicInteger closed = new AtomicInteger(0);
	
	public static boolean Connect = false;
	public static boolean Firstmsg = true;


	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		if (!(cause instanceof IOException)) {
			logger.error("Exception: ", cause);
		} else {
			logger.debug("I/O error: " + cause.getMessage());
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
		logger.debug("Creation of session " + session.getId());
		session.setAttribute(OPEN);
		session.suspendRead();
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		session.removeAttribute(OPEN);
		logger.debug("{}> Session closed", session.getId());
		SessionManager.getInstance().remove(session,"CLOSED");
		
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
		SmgpPDU pdu = (SmgpPDU) message;
		String sequenceNumber = String.valueOf(pdu.header.getSequenceID());
		logger.debug("MESSAGE:RequestId={},SequenceNumber={},sessionid={},ip={}" ,String.valueOf(pdu.header.getRequestID()),sequenceNumber,sid,ip);
		
		switch (pdu.header.getRequestID()) {
		// 建立连接
		case SmgpConstant.RID_LOGIN:// 客户端登录
			com.protocol.access.smgp.pdu.Connect con = (com.protocol.access.smgp.pdu.Connect) pdu;
			com.protocol.access.smgp.pdu.ConnectResp conresp = (com.protocol.access.smgp.pdu.ConnectResp) con
					.getResponse();
			int status = AuthCheckerManager.getInstance().authClient(sa.getAddress().getHostAddress(),
					con.getClientId(), con.getAuthClient(), con.getVersion(), con.getTimeStamp());
			if (status == 0) {
				if (ActiveManager.getInstance().exist(con.getClientId())) {
					ActiveManager.getInstance().put(session, new ActiveThread(session, con.getClientId()));
				}
				SessionManager.getInstance().put(con.getClientId(), session, con.getVersion());
			}
			conresp.setStatus(status);
			if (status == 0) {
				ByteBuffer buffer = new ByteBuffer();
				buffer.appendInt(status);
				buffer.appendBytes(con.getAuthClient());
				buffer.appendString(con.getSharedSecret(), con.getSharedSecret().length());
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] result = md.digest(buffer.getBuffer());
				conresp.setAuthServer(new String(result));
			}

			conresp.setVersion(con.getVersion());
			// 应答响应
			CategoryLog.connectionLogger.debug(
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

			if (status != 0) {
				session.closeOnFlush();
			}
			break;
		case SmgpConstant.RID_ACTIVE_TEST:// 检测
			com.protocol.access.smgp.pdu.ActiveTest activeTest = (com.protocol.access.smgp.pdu.ActiveTest) pdu;
			com.protocol.access.smgp.pdu.ActiveTestResp activeTestResp = (com.protocol.access.smgp.pdu.ActiveTestResp) activeTest
					.getResponse();
			
			CategoryLog.connectionLogger.debug(
					new StringBuilder(DateUtil.getCurDateTime()).append("heartbeatResponse")
					.append("{}commandID={}")
					.append("{}sessionID={}")
					.append("{}body={}").toString(),
					FixedConstant.LOG_SEPARATOR, SmgpConstant.RID_ACTIVE_TEST, 
					FixedConstant.LOG_SEPARATOR, session.getId(),
					FixedConstant.LOG_SEPARATOR,Arrays.toString(activeTestResp.getData().getBuffer()));
			session.write(activeTestResp);
			break;
		case SmgpConstant.RID_ACTIVE_TEST_RESP:// 链路检测应答
			com.protocol.access.smgp.pdu.ActiveTestResp activeTestRsp = (com.protocol.access.smgp.pdu.ActiveTestResp) pdu;
			activeTestRsp.dump();
			ActiveThread.lastActiveTime = System.currentTimeMillis();
			break;
		// 短信提交
		case SmgpConstant.RID_SUBMIT:// 提交短消息
			com.protocol.access.smgp.pdu.Submit submit = (com.protocol.access.smgp.pdu.Submit) pdu;

			byte[] msgid = Tools.getMsgId("111111");

			submit.setMsgID(msgid);
			String accountId = SessionManager.getInstance().getClient(session);
			int subStatus = AuthSubmitMessageManager.getInstance().authSubmitMessage(session, sequenceNumber, accountId,
					submit);
			com.protocol.access.smgp.pdu.SubmitResp subresp = (com.protocol.access.smgp.pdu.SubmitResp) submit
					.getResponse();

			subresp.setStatus(subStatus);
			subresp.setMsgId(msgid);
			// 应答响应
			WriteFuture future = session.write(subresp);
			boolean result = future.isWritten();

			CategoryLog.connectionLogger.info(
					new StringBuilder().append("SubmitResp")
					.append("{}client={}")
					.append("{}msgid={}")
					.append("{}serviceId={}")
					.append("{}status={}")
					.append("{}result={}")
					.append("{}sequenceID={}")
					.append("{}响应耗时{}毫秒").toString(),
					FixedConstant.LOG_SEPARATOR, accountId, 
					FixedConstant.LOG_SEPARATOR, Hex.encodeHexString(msgid), 
					FixedConstant.LOG_SEPARATOR,submit.getServiceID(), 
					FixedConstant.LOG_SEPARATOR, String.valueOf(subStatus),
					FixedConstant.LOG_SEPARATOR, String.valueOf(result), 
					FixedConstant.LOG_SEPARATOR, sequenceNumber,
					FixedConstant.LOG_SEPARATOR, (System.currentTimeMillis() - start));

			// 记录提交参数
			submitLog(submit);

			break;
		case SmgpConstant.RID_DELIVER_RESP:// 下发短消息应答
			com.protocol.access.smgp.pdu.DeliverResp delresp = (com.protocol.access.smgp.pdu.DeliverResp) pdu;
			String messageID = Hex.encodeHexString(delresp.getMsgID());
			ReportTimerTaskWorkerManager.getInstance().removeReportTaskByResponse(messageID);
			
			CategoryLog.connectionLogger.info(
					new StringBuilder().append("DeliverResp")
					.append("{}sequenceID={}")
					.append("{}session={}")
					.append("{}msgid={}").toString()
					,
					FixedConstant.LOG_SEPARATOR,pdu.header.getSequenceID(),
					FixedConstant.LOG_SEPARATOR,session.getId(),
					FixedConstant.LOG_SEPARATOR,messageID
					);
			break;
		default:
			logger.warn("Unexpected PDU received! PDU Header: " + pdu.header.getData().getHexDump());
			break;
		}
	}
	

	private void submitLog(com.protocol.access.smgp.pdu.Submit submit){
				
		CategoryLog.messageLogger.info(
				new StringBuilder().append("SMGP_SUBMIT")
				.append(":RequestId={}")
				.append("{}Sequence_Id={}")				
				.append("{}msgType={}")
				.append("{}needReprot={}")
				.append("{}priority={}")
				.append("{}serviceID ={}")	
				.append("{}feeType={}")
				.append("{}feeCode={}")
				.append("{}fixedFee={}")	
				
				.append("{}msgFormat={}")
				.append("{}validTime={}")				
				.append("{}atTime={}")
				.append("{}srcTermID={}")
				.append("{}chargeTermID={}")
				.append("{}destTermIdCoun={}")
				.append("{}destTermId={}")
				.append("{}msg_Length={}")
				.append("{}msg_Content_base64={}")
				.append("{}reserve={}")
				.toString(),
			    submit.header.getRequestID(),
				FixedConstant.LOG_SEPARATOR,submit.header.getSequenceID(),
				FixedConstant.LOG_SEPARATOR,submit.getMsgType(),
				FixedConstant.LOG_SEPARATOR,submit.getNeedReprot(),
				FixedConstant.LOG_SEPARATOR,submit.getPriority(),
				FixedConstant.LOG_SEPARATOR,submit.getServiceID(),
				FixedConstant.LOG_SEPARATOR,submit.getFeeType(),
				FixedConstant.LOG_SEPARATOR,submit.getFeeCode(),				
				FixedConstant.LOG_SEPARATOR,submit.getFixedFee(),
				
				
				FixedConstant.LOG_SEPARATOR,submit.getMsgFormat(),
				FixedConstant.LOG_SEPARATOR,submit.getValidTime(),
				FixedConstant.LOG_SEPARATOR,submit.getAtTime(),
				FixedConstant.LOG_SEPARATOR,submit.getSrcTermID(),
				FixedConstant.LOG_SEPARATOR,submit.getChargeTermID(),
				FixedConstant.LOG_SEPARATOR,submit.getDestTermIdCount(),
				FixedConstant.LOG_SEPARATOR,submit.getDestTermId(),
				FixedConstant.LOG_SEPARATOR,submit.getSm().getLength(),
				FixedConstant.LOG_SEPARATOR,submit.getSm().getMessage(),		
				FixedConstant.LOG_SEPARATOR,submit.getReserve());

					
	}
}
