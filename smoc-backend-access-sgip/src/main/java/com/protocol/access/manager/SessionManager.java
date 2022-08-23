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

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.BusinessDataManager;
import com.base.common.worker.SuperCacheWorker;


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
		if(worker != null){
			return worker.getVersion();
		}
		return 1;
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
	
		PeportPullWorker worker = new PeportPullWorker(clientId, session, version);
		worker.setTimestamp(System.currentTimeMillis());
		worker.setReadBytes(session.getReadBytes());
		worker.setName("PeportPullWorker-" + clientId + "-" + session.getId());
		worker.start();
		reportThreadMap.put(session, worker);
		//加锁保证clientId和set的一致性
		synchronized (lock) {
			Set<IoSession> set = clientMap.get(clientId);
			if(set == null){
				set = new CopyOnWriteArraySet<IoSession>(); 
				clientMap.put(clientId, set);
			}
		}
		clientMap.get(clientId).add(session);
		CategoryLog.connectionLogger.info("维护session,clientId={},session={}",clientId,session);
	}

	/**
	 * 连接中断，移除session
	 * 
	 * @param session
	 */
	public synchronized void remove(IoSession session,String Trigger) {
		try {
			if(session == null){
				return;
			}
			PeportPullWorker worker = reportThreadMap.remove(session);
			
			if(worker != null){
				String client = worker.getUsername();
				worker.exit();
				CategoryLog.connectionLogger.info("client={}session={},Trigger={},isClosing={},isConnected={}", client,session,Trigger,session.isClosing(),session.isConnected());
				Set<IoSession> set = clientMap.get(client);
				if (set != null) {
					set.remove(session);
					CategoryLog.connectionLogger.info("client={}从集合中删除session={},Trigger={},isClosing={},isConnected={}", client, session,Trigger,session.isClosing(),session.isConnected());
				}
			}
		} catch (Exception e) {
			CategoryLog.connectionLogger.error(e.getMessage(),e);
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
						if (!session.isClosing() && session.isConnected()) {
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
				if (!session.isClosing() && session.isConnected())
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
					int sessionSize = 0;
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
							sessionSize += set.size();
							CategoryLog.connectionLogger.info(new StringBuffer("session monitor:")
									.append("client={}")
									.append("{}session size={}")
									.append("{}ips={}")
									.append("{}sessions={}").toString()
									,
									entry.getKey(),
									FixedConstant.LOG_SEPARATOR,set.size(),
									FixedConstant.LOG_SEPARATOR,ips.toString(),
									FixedConstant.LOG_SEPARATOR,sessions);

						}
						CategoryLog.connectionLogger.info("总链接数:{}",sessionSize);
						for (Map.Entry<IoSession, PeportPullWorker> entry : reportThreadMap.entrySet()) {

							IoSession session = entry.getKey();
							PeportPullWorker worker = entry.getValue();
							String client = worker.getName();

							// 间隔时间超过指定值则需校验readbytes
							if( (System.currentTimeMillis() - worker.getTimestamp()) > 1000 * 90 ){
								if(session.getReadBytes() > worker.getReadBytes()){
									worker.setTimestamp(System.currentTimeMillis());
									worker.setReadBytes(session.getReadBytes());
								}else{
									CategoryLog.connectionLogger.warn("超时会话:client={},session={},isClosing={},isConnected={}", client,session,session.isClosing(),session.isConnected());
									temp.add(session);
								}
							}
						}
						
						CategoryLog.connectionLogger.info("会话过期数量:{}", temp.size());
						for (IoSession session : temp) {
							CloseFuture closeFuture  = session.closeNow();
							closeFuture.awaitUninterruptibly();
							CategoryLog.connectionLogger.info("关闭会话session={},isClosing={},isConnected={},isDone={}",session,session.isClosing(),session.isConnected(),closeFuture.isDone());
						}
		
						for (IoSession session : temp) {
							remove(session,"TIMEOUT");
							CategoryLog.connectionLogger.info("清理会话session={},isClosing={},isConnected={}",session,session.isClosing(),session.isConnected());
						}
						CategoryLog.connectionLogger.info("有效总链接数:{}",sessionSize-temp.size());
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
	class PeportPullWorker extends SuperCacheWorker {
		private String clientId;
		private IoSession session;
		private int version;
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
		protected void doRun() throws Exception {
			sleep(FixedConstant.COMMON_EFFECTIVE_TIME);
		}
	}
}
