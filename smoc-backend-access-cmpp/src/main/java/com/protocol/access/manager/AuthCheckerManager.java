/**
 * @desc
 * @author ma
 * @date 2017��9��5��
 * 
 */
package com.protocol.access.manager;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.base.common.constant.DynamicConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.util.DateUtil;
import com.base.common.worker.SuperMapWorker;
import com.protocol.access.cmpp.CmppConstant;
import com.protocol.access.util.DES;
import com.protocol.access.vo.AuthClient;

public class AuthCheckerManager extends SuperMapWorker<String, AuthClient>{
	
	private static AuthCheckerManager manager = new AuthCheckerManager();

	public AuthClient getAuthClient(String accountID) {
		return get(accountID);
	}

	private AuthCheckerManager() {
		loaData();
		if (superMap == null) {
			logger.error("client鉴权加载失败");
		}
		this.start();
		
	}
	
	public Map<String, AuthClient> getMap(){
		return superMap;
	}

	public static AuthCheckerManager getInstance() {
		return manager;
	}
	
	@Override
	protected void doRun() throws Exception {
		Thread.sleep(FixedConstant.COMMON_EFFECTIVE_TIME);
		loaData();
	}

	public synchronized int authClient(String ip, String clientID, byte[] authClient, byte version, int timeStamp) {
		CategoryLog.connectionLogger.info(new StringBuilder(DateUtil.getCurDateTime())
				.append("开始鉴权:")
				.append("client={}")
				.append("{}authClient={}")
				.append("{}version={}")
				.append("{}timeStamp={}")
				.append("{}ip={}").toString()
				,
				clientID,
				FixedConstant.LOG_SEPARATOR,Arrays.toString(authClient),
				FixedConstant.LOG_SEPARATOR,String.valueOf(version),
				FixedConstant.LOG_SEPARATOR,String.valueOf(timeStamp),
				FixedConstant.LOG_SEPARATOR,ip
				);
		

		/*if ((int) version != 48 && (int) version != 32 && (int) version != 0) {
			logger.error("版本错误:client={}", clientID);
			return 4;
		}*/
		if (String.valueOf(timeStamp).length() > 10 || String.valueOf(timeStamp).length() < 7) {
			logger.error(new StringBuilder(DateUtil.getCurDateTime())
					.append("时间格式错误:")
					.append("client={}")
					.append("{}version={}").toString()
					,clientID,
					FixedConstant.LOG_SEPARATOR,version >= 48 ? CmppConstant.VERSION3 : CmppConstant.VERSION2);
			return 3;
		}

		AuthClient accountInterfaceInfo = getAuthClient(clientID);
		if (accountInterfaceInfo == null) {
			logger.error("账号非法:client={}", clientID);
			return 2;
		}
		if (accountInterfaceInfo.isAuthFlag() && !accountInterfaceInfo.getIdentifyIP().contains(ip)) {
			logger.error("账号非法:client={}", clientID);
			return 2;
		}
		String time = String.valueOf(timeStamp);

		if (time.length() != 10) {
			if (time.length() == 9) {
				time = "0".concat(time);
			} else if (time.length() == 8) {
				time = "00".concat(time);
			} else if (time.length() == 7) {
				time = "000".concat(time);
			}

		}
		byte[] sourceAuthClient = genAuthClient(accountInterfaceInfo.getAccountID(),
				DES.decrypt(accountInterfaceInfo.getAccountPassword()), time);
		CategoryLog.connectionLogger.info(new StringBuilder(DateUtil.getCurDateTime())
				.append("匹配MD5:")
				.append("client={}")
				.append("{}source={}")
				.toString()
				,accountInterfaceInfo.getAccountID(),
				FixedConstant.LOG_SEPARATOR,Arrays.toString(sourceAuthClient));
		// 增加一个原始timeStamp的比较方式
		boolean result = Arrays.equals(sourceAuthClient, authClient);

		if (!result) {
			logger.error("MD5匹配错误:client={}", clientID);
			return 3;
		}
		
		int count = SessionManager.getInstance().getSessionQuantity(clientID);
		if (count >= accountInterfaceInfo.getMaxConnect()) {
			logger.error(
					new StringBuilder(DateUtil.getCurDateTime())
					.append("超过最大连接:")
					.append("client={}")
					.append("{}connNum={}")
					.toString()
					,clientID,
					FixedConstant.LOG_SEPARATOR,accountInterfaceInfo.getMaxConnect());
			return 5;
		}
		CategoryLog.connectionLogger.info(
				new StringBuilder(DateUtil.getCurDateTime())
				.append("连接成功:")
				.append("client={}")
				.toString()
				,clientID);
		return 0;
	}

	public byte[] genAuthClient(String clientId, String sharedSecret, String timeStamp) {
		byte[] result = new byte[16];
		try {
			byte[] source = new byte[clientId.length() + sharedSecret.length() + 9 + timeStamp.length()];
			System.arraycopy(clientId.getBytes(), 0, source, 0, clientId.length());
			System.arraycopy(sharedSecret.getBytes(), 0, source, clientId.length() + 9, sharedSecret.length());
			System.arraycopy(timeStamp.getBytes(), 0, source, clientId.length() + 9 + sharedSecret.length(),
					timeStamp.length());
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = md.digest(source);
		} catch (Exception ex) {

		}
		return result;
	}
	
	/**
	 * 加载数据
	 */
	private void loaData() {
		Map<String, AuthClient> newmap = loadClientAuth(DynamicConstant.PLATFORM_IDENTIFIER);
		if (newmap != null) {
			superMap = newmap;
		}
	}
	
	/**
	 * 获取账号的配置信息
	 * @param interType
	 * @return
	 */
	private Map<String, AuthClient> loadClientAuth(String interType) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append(
				"select a.ACCOUNT_ID,a.PROTOCOL,a.ACCOUNT_PASSWORD,a.MAX_SUBMIT_SECOND,a.MAX_SEND_SECOND,a.SRC_ID,a.IDENTIFY_IP,a.MAX_CONNECT,a.EXECUTE_CHECK,a.MO_URL,a.STATUS_REPORT_URL FROM smoc.account_interface_info a ");
		sql.append("LEFT JOIN smoc.account_base_info u on u.ACCOUNT_ID = a.ACCOUNT_ID ");
		sql.append(" WHERE a.PROTOCOL = ? and u.ACCOUNT_STATUS IN (1,3)");
		Map<String, AuthClient> map = new HashMap<String, AuthClient>();
		AuthClient vo = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, interType.toUpperCase());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				vo = new AuthClient();
				vo.setAccountID(rs.getString("ACCOUNT_ID"));
				vo.setProtocol(rs.getString("PROTOCOL"));
				vo.setAccountPassword(rs.getString("ACCOUNT_PASSWORD"));
				vo.setMaxSubmitSecond(rs.getInt("MAX_SUBMIT_SECOND"));
				vo.setMaxSendSecond(rs.getInt("MAX_SEND_SECOND"));
				vo.setSrcId(rs.getString("SRC_ID"));
				vo.setMaxConnect(rs.getInt("MAX_CONNECT"));
				vo.setExecuteCheck(rs.getString("EXECUTE_CHECK"));
				vo.setMoUrl(rs.getString("MO_URL"));
				vo.setStatusReportUrl(rs.getString("STATUS_REPORT_URL"));

				String ips = rs.getString("IDENTIFY_IP");
				if (ips == null || ips.trim().length() == 0) {
					vo.setAuthFlag(false);
				} else {
					vo.setIdentifyIP(new HashSet<String>(Arrays.asList(ips.split(","))));
				}
				map.put(vo.getAccountID(), vo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return map;
	}
}
