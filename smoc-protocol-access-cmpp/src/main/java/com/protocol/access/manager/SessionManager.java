/**
 * @desc
 * @author ma
 * @date 2017��9��5��
 * 
 */
package com.protocol.access.manager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.protocol.access.cmpp.CmppConstant;
import com.protocol.access.util.DeliverUtil;
import com.protocol.access.vo.Report;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;

//维护每个session的版本
/**
 * session管理，维护
 */
public class SessionManager {
	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
	
	// client-session集合
	public Map<String, Set<IoSession>> clientMap = new ConcurrentHashMap<String, Set<IoSession>>();
	// session-版本号
	public Map<IoSession, PeportPullWorker> reportThreadMap = new ConcurrentHashMap<IoSession, PeportPullWorker>();

	public int getSessionVersion(IoSession session) {
		PeportPullWorker worker = reportThreadMap.get(session);
		if (worker != null) {
			return worker.getVersion();
		}
		return CmppConstant.VERSION2;
	}

	private static SessionManager manager = new SessionManager();

	private SessionManager() {
		new SessionMonitor("SessionMonitor").start();
	}

	static public SessionManager getInstance() {
		return manager;
	}

	private Object lock = new Object();
	
	/**
	 * 连接鉴权成功后：初始化数据
	 * @param clientId
	 * @param session
	 * @param version
	 */
	public void put(String clientId, IoSession session, byte version) {
		
		//加锁保证clientId和set的一致性
		synchronized (lock) {
			Set<IoSession> set = clientMap.get(clientId);
			if(set == null){
				set = new CopyOnWriteArraySet<IoSession>(); 
				clientMap.put(clientId, set);
			}
		}
		
		clientMap.get(clientId).add(session);
		PeportPullWorker worker = new PeportPullWorker(clientId, session,
				version == 48 ? CmppConstant.VERSION3 : CmppConstant.VERSION2);
		worker.setTimestamp(System.currentTimeMillis());
		worker.setReadBytes(session.getReadBytes());
		worker.setName("PeportPullWorker_" + clientId + "_" + session.getId());
		worker.start();
		reportThreadMap.put(session, worker);

	}

	/**
	 * 连接中断，移除session
	 * 
	 * @param session
	 */
	public void remove(IoSession session) {
		if(session == null){
			return;
		}
		PeportPullWorker worker = reportThreadMap.remove(session);
		ActiveManager.getInstance().remove(session);
		
		if(worker != null){
			String client = worker.getUsername();
			CategoryLog.connectionLogger.info("client={}连接中断", client);
			Set<IoSession> set = clientMap.get(client);
			if (set != null) {
				set.remove(session);
				CategoryLog.connectionLogger.info("client={}从session集合中删除{}", client, session);
			}
		}
	}

	// 随机获取一个有效连接,当随机指定的链接无效时，则返回空
	public IoSession getSession(String clientId) {
		Set<IoSession> sessions = clientMap.get(clientId);
		// 先通过随机值获取指定链接，若指定链接无效则遍历获取一个有效的
		if (sessions != null && sessions.size() > 0) {
			int size = sessions.size();
			int index = (int) (Math.random() * size);
			int count = 0;
			for (IoSession session : sessions) {
				if (count > index) {
					break;
				} else {
					if (count == index) {
						if (session.isConnected()) {
							return session;
						}
					}
				}
				count++;
			}
			return getSessionValid(clientId);
		}
		return null;
	}

	/**
	 * 获取一个有效session
	 * 
	 * @param clientId
	 * @return
	 */
	public IoSession getSessionValid(String clientId) {
		Set<IoSession> sessions = clientMap.get(clientId);
		if (sessions != null && sessions.size() > 0) {
			for (IoSession session : sessions) {
				if (session.isConnected())
					return session;
			}
		}
		return null;
	}

	public String getClient(IoSession session) {
		PeportPullWorker worker = reportThreadMap.get(session);
		if (worker != null) {
			return worker.getUsername();
		}
		return null;
	}

	/**
	 * 获取账号已连接数
	 * 
	 * @param clientId
	 * @return
	 */
	public int getSessionQuantity(String clientId) {
		Set<IoSession> sessions = clientMap.get(clientId);
		if (sessions == null) {
			return 0;
		}
		return sessions.size();
	}

	/**
	 * session超时监控
	 */
	class SessionMonitor extends Thread {

		public SessionMonitor(String name) {
			super(name);
		}

		@Override
		public void run() {
			while (true) {
				try {
					if (clientMap.size() == 0) {
						CategoryLog.connectionLogger.info("session monitor,sessions=0");
					} else {
						// 会话过期session
						Set<IoSession> temp = new HashSet<IoSession>();
						for (Map.Entry<String, Set<IoSession>> entry : clientMap.entrySet()) {
							Set<IoSession> set = entry.getValue();
							StringBuffer ips = new StringBuffer();
							StringBuffer sessions = new StringBuffer();
							for (IoSession session : set) {
								if (session != null) {
									sessions.append(session);
									sessions.append("|");
									InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getRemoteAddress();
									if (inetSocketAddress != null) {
										InetAddress inetAddress = inetSocketAddress.getAddress();
										if (inetAddress != null) {
											ips.append(inetAddress.getHostAddress());
											ips.append("|");
										}
									}
								}
							}
							CategoryLog.connectionLogger.info(new StringBuffer("session monitor:")
									.append("client={}")
									.append("{}session size={}")
									.append("{}ips={}")
									.append("{}sessions={}").toString()
									,
									entry.getKey(),
									FixedConstant.LOG_SEPARATOR,entry.getValue().size(),
									FixedConstant.LOG_SEPARATOR,ips.toString(),
									FixedConstant.LOG_SEPARATOR,sessions);

						}

						for (Map.Entry<IoSession, PeportPullWorker> entry : reportThreadMap.entrySet()) {

							IoSession session = entry.getKey();
							PeportPullWorker worker = entry.getValue();
							String client = worker.getName();

							if (ActiveManager.getInstance().exist(client)) {
								CategoryLog.connectionLogger.info("client={},由心跳线程检测心跳", client);
							} else {
								// 间隔时间超过指定值则需校验readbytes
								if( (System.currentTimeMillis() - worker.getTimestamp()) > 1000 * 90 ){
									if(session.getReadBytes() > worker.getReadBytes()){
										worker.setTimestamp(System.currentTimeMillis());
										worker.setReadBytes(session.getReadBytes());
									}else{
										CategoryLog.connectionLogger.warn("超时会话:session={},client={}", session, client);
										temp.add(session);
									}
								}
							}
						}

						for (IoSession session : temp) {
							session.closeNow();
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				try {
					Thread.sleep(BusinessDataManager.getInstance().getSessionMonitorIntervalTime());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

		}

	}

	/**
	 * 从redis中获取状态报告线程
	 *
	 */
	class PeportPullWorker extends Thread {
		private String clientId;
		private IoSession session;
		private int version;
		private boolean flag = true;
		//时间戳
		private long timestamp;
		//读取的字节数
		private long readBytes;

		public PeportPullWorker(String username, IoSession session, int version) {
			this.clientId = username;
			this.session = session;
			this.version = version;
		}

		public String getUsername() {
			return clientId;
		}

		public IoSession getSession() {
			return session;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		public int getVersion() {
			return version;
		}
		
		
		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public long getReadBytes() {
			return readBytes;
		}

		public void setReadBytes(long readBytes) {
			this.readBytes = readBytes;
		}

		@Override
		public void run() {
			logger.info(this.getName() + "启动!");
			while (flag) {
				try {
					BusinessRouteValue value = CacheBaseService.getReportFromMiddlewareCache(clientId);
					if (value != null) {
						logger.info("{}获取状态报告{}{}",DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI),FixedConstant.SPLICER,value.toString());
						long start = System.currentTimeMillis();
						if (FixedConstant.RouteLable.MR.toString().equals(value.getRouteLabel())) {
							if (value.getAccountReportFlag() != 1) {
								logger.info("{}发送{}不需要返回状态报告", value.getAccountID(), value.getPhoneNumber());
								continue;
							}
							String accountMessageIDs = value.getAccountMessageIDs();

							Report report = new Report();

							report.setAccountId(value.getAccountID());
							report.setPhoneNumber(value.getPhoneNumber());
							report.setReportTime(value.getChannelReportTime());
							report.setSubmitTime(value.getAccountSubmitTime());
							report.setStatusCode(value.getStatusCode());
							report.setTemplateId(value.getAccountTemplateID());
							report.setAccountSrcId(value.getAccountSubmitSRCID());
							report.setAccountBusinessCode(value.getAccountBusinessCode());
							report.setMessageTotal(value.getMessageTotal());
							report.setMessageIndex(value.getMessageIndex());
							report.setOptionParam(value.getOptionParam());
							for(String msgId : accountMessageIDs.split(FixedConstant.SPLICER)) {
								report.setMessageId(msgId);
								DeliverUtil.sendReport(session, report);
							}
						}
						logger.info("推送状态报告耗时{}",(System.currentTimeMillis() - start));
					} else {
						sleep(BusinessDataManager.getInstance().getReportRedisPopIntervalTime());
					}

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

			logger.info(this.getName() + "退出!");
		}
	}
}
