/**
 * @desc
 * @author ma
 * @date 2017��9��5��
 * 
 */
package com.protocol.access.manager;

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
import com.protocol.access.util.ConnectionArgCache;
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

	public synchronized int authClient(String ip, String loginName, String loginPassword,byte loginType) {
		CategoryLog.connectionLogger.info(new StringBuilder(DateUtil.getCurDateTime())
				.append("开始鉴权")
				.append("{}loginName={}")
				.append("{}loginPassword={}")
				.append("{}loginType={}")
				.append("{}ip={}").toString()
				,
				FixedConstant.LOG_SEPARATOR,loginName,
				FixedConstant.LOG_SEPARATOR,loginPassword,
				FixedConstant.LOG_SEPARATOR,loginType,
				FixedConstant.LOG_SEPARATOR,ip
				);
		

		AuthClient authClient = getAuthClient(loginName);
		if (authClient == null) {
			logger.warn("loginName={},账号非法", loginName);
			return 29;
		}
		
		if (authClient.isAuthFlag() && !authClient.getIdentifyIP().contains(ip)) {
			logger.warn("IP非法:loginName={},ip={}", loginName, ip);
			return 21;
		}
		
		if(!authClient.getAccountPassword().equals(loginPassword)) {
			logger.warn("用户认证失败:loginName={},loginPassword={}",loginName,loginPassword);
			return 1;
		}
		
		int count = SessionManager.getInstance().getSessionQuantity(loginName);
		logger.info("loginName={},连接数={}", loginName, count+1);
		
		CategoryLog.connectionLogger.info(
				new StringBuilder(DateUtil.getCurDateTime())
				.append("连接成功:")
				.append("loginName={}")
				.toString()
				,loginName);
		return 0;
	}
	
	/**
	 * 加载数据
	 */
	private void loaData() {
		Map<String, AuthClient> newmap = loadClientAuth(DynamicConstant.PLATFORM_IDENTIFIER);
		if (newmap != null) {
			superMap = newmap;
			updateConnectionArg(superMap);
		}
	}
	
	private void updateConnectionArg(Map<String, AuthClient> map){
		Map<String, String> paramMap = null;
		AuthClient authClient = null;
		for(String client : map.keySet()){
			authClient = map.get(client);
			paramMap = new HashMap<String, String>();
			String reportUrl = authClient.getStatusReportUrl();
			if(authClient.getStatusReportUrl() != null){
				String [] arrStr = reportUrl.split(":");
				if(arrStr.length != 2) {
					continue;
				}
				paramMap.put("host", arrStr[0]);
				paramMap.put("port", arrStr[1]);
				paramMap.put("loginName", client);
				paramMap.put("loginPassword", authClient.getAccountPassword());
				ConnectionArgCache.putArgMap(client, paramMap);
			}
			logger.info("{}上行参数{}",client,paramMap);
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
		sql.append("SELECT a.ACCOUNT_ID,a.PROTOCOL,a.ACCOUNT_PASSWORD,a.MAX_SUBMIT_SECOND,a.MAX_SEND_SECOND,a.SRC_ID,a.IDENTIFY_IP,a.MAX_CONNECT,a.EXECUTE_CHECK,a.MO_URL,a.STATUS_REPORT_URL FROM smoc.account_interface_info a ");
		sql.append("LEFT JOIN smoc.account_base_info u on u.ACCOUNT_ID = a.ACCOUNT_ID ");
		sql.append("WHERE a.PROTOCOL = ? and u.ACCOUNT_STATUS IN (1,3)");
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
				vo.setAccountPassword(DES.decrypt(rs.getString("ACCOUNT_PASSWORD")));
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
