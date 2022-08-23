package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperMapWorker;

import com.base.common.manager.AccountSignManager.AccountSignRegisterForFile;

public class AccountSignManager extends SuperMapWorker<String, AccountSignRegisterForFile>{

	
	private static AccountSignManager accountSignManager = new AccountSignManager();
	
	public static AccountSignManager getInstance() {
		return accountSignManager;
	}
	
	private AccountSignManager() {
		loadData();
		super.start();
	}
	
	public int getSignRegisterStatus(String accountID,String channelID,String registerSign) {
		String key = new StringBuilder().append(accountID).append(FixedConstant.SPLICER)
				.append(channelID).append(FixedConstant.SPLICER)
				.append(registerSign).toString();
		AccountSignRegisterForFile accountSignRegisterForFile = superMap.get(key);
		if(accountSignRegisterForFile != null) {
			return Integer.valueOf(accountSignRegisterForFile.getRegisterStatus());
		}
		return FixedConstant.SignRegisterStatus.NO.ordinal();
	}
	
	public String getSignRegisterExtendNumber(String accountID,String channelID,String registerSign) {
		String key = new StringBuilder().append(accountID).append(FixedConstant.SPLICER)
				.append(channelID).append(FixedConstant.SPLICER)
				.append(registerSign).toString();
		AccountSignRegisterForFile accountSignRegisterForFile = superMap.get(key);
		String registerExtendNumber = null;
		if(accountSignRegisterForFile != null) {
			registerExtendNumber = accountSignRegisterForFile.getRegisterExtendNumber();
		}
		return registerExtendNumber;
	}

	@Override
	protected void doRun() throws Exception {
		//发送时涉及财务需要指定单独的加载时间
		Thread.sleep(super.INTERVAL);
		loadData();
	}
	
	/**
	 * 加载数据
	 */
	private void loadData() {
		long startTime = System.currentTimeMillis();
		Map<String, AccountSignRegisterForFile> resultMap = loadAccountSignRegisterForFile();
		if(resultMap != null){
			superMap = resultMap;
		}
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
	}
	
	/**
	 * 加载账号签名子端口数据
	 */
	private Map<String, AccountSignRegisterForFile> loadAccountSignRegisterForFile() {
		Map<String, AccountSignRegisterForFile> resultMap = new HashMap<String, AccountSignRegisterForFile>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append(
				"SELECT ACCOUNT,CHANNEL_ID,REGISTER_EXTEND_NUMBER,REGISTER_SIGN,REGISTER_STATUS FROM smoc.account_sign_register_for_file");
		AccountSignRegisterForFile accountSignRegisterForFile = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				accountSignRegisterForFile = new AccountSignRegisterForFile();
				String accountID = rs.getString("ACCOUNT");
				String channelID = rs.getString("CHANNEL_ID");
				String registerExtendNumber = rs.getString("REGISTER_EXTEND_NUMBER");
				String registerSign = rs.getString("REGISTER_SIGN");
				String registerStatus = rs.getString("REGISTER_STATUS");
				
				String key = new StringBuilder().append(accountID).append(FixedConstant.SPLICER)
												.append(channelID).append(FixedConstant.SPLICER)
												.append(registerSign).toString();
				
				accountSignRegisterForFile.setAccountID(accountID);
				accountSignRegisterForFile.setChannelID(channelID);
				accountSignRegisterForFile.setRegisterExtendNumber(registerExtendNumber);
				accountSignRegisterForFile.setRegisterSign(registerSign);
				accountSignRegisterForFile.setRegisterStatus(registerStatus);
				
				resultMap.put(key, accountSignRegisterForFile);
			}
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}
	
	class AccountSignRegisterForFile{
		
		private String accountID;
		
		private String channelID;
		
		private String registerSign;
		
		private String registerExtendNumber;
		
		private String registerStatus;

		public String getAccountID() {
			return accountID;
		}

		public void setAccountID(String accountID) {
			this.accountID = accountID;
		}

		public String getChannelID() {
			return channelID;
		}

		public void setChannelID(String channelID) {
			this.channelID = channelID;
		}

		public String getRegisterSign() {
			return registerSign;
		}

		public void setRegisterSign(String registerSign) {
			this.registerSign = registerSign;
		}

		public String getRegisterExtendNumber() {
			return registerExtendNumber;
		}

		public void setRegisterExtendNumber(String registerExtendNumber) {
			this.registerExtendNumber = registerExtendNumber;
		}
		
		public String getRegisterStatus() {
			return registerStatus;
		}

		public void setRegisterStatus(String registerStatus) {
			this.registerStatus = registerStatus;
		}

		@Override
		public String toString() {
			return "AccountSignRegisterForFile [accountID=" + accountID + ", channelID=" + channelID + ", registerSign="
					+ registerSign + ", registerExtendNumber=" + registerExtendNumber + ", registerStatus="
					+ registerStatus + "]";
		}
	}
}
