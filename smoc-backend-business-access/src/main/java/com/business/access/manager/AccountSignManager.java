package com.business.access.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.manager.BusinessDataManager;
import com.base.common.worker.SuperMapWorker;

import com.business.access.manager.AccountSignManager.AccountSignRegisterForFile;

public class AccountSignManager extends SuperMapWorker<String, AccountSignRegisterForFile>{

	
	private static AccountSignManager accountSignManager = new AccountSignManager();
	
	public static AccountSignManager getInstance() {
		return accountSignManager;
	}
	
	private AccountSignManager() {
		loadData();
		super.start();
	}
	
	public String getSignRegisterExtendNumber(String accountID,String channelID,String registerSign,String accountExtendCode) {
		String key = new StringBuilder().append(accountID).append(FixedConstant.SPLICER)
				.append(channelID).append(FixedConstant.SPLICER)
				.append(registerSign).toString();
		AccountSignRegisterForFile accountSignRegisterForFile = get(key);
		if(accountSignRegisterForFile == null) {
			return null;
		}
		if(accountSignRegisterForFile.getAlreadyRegisterExtendNumberSet().size() > 0) {
			//已报备的签名
			return getRegisterExtendNumber(accountSignRegisterForFile.getAlreadyRegisterExtendNumberSet(),accountExtendCode);
		}else {
			//未报备或者报备中的签名
			String accountSignRegisterOperationType = BusinessDataManager.getInstance().getAccountSignRegisterOperationType(accountID);
			// 账号配置允许未报备或者正在报备的签名下发
			if (FixedConstant.COMMON_SIMPLE_LOGIC_JUDGE_STRING_FLAG.equals(accountSignRegisterOperationType)) {
				return getRegisterExtendNumber(accountSignRegisterForFile.getBeingRegisterExtendNumberSet(),accountExtendCode);
			}
		}
		return null;
	}
	
	private String getRegisterExtendNumber(Set<String> extendNumberSet,String accountExtendCode) {
		if(extendNumberSet.contains(accountExtendCode)) {
			return accountExtendCode;
		}
		return getRandomRegisterExtendNumber(extendNumberSet);
	}
	
	private String getRandomRegisterExtendNumber(Set<String> registerExtendNumberSet) {
		List<String> list= new ArrayList<String>(registerExtendNumberSet);
		int randomNum = (int) (Math.random() * list.size());
		return list.get(randomNum);
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
				"SELECT ACCOUNT,CHANNEL_ID,REGISTER_EXTEND_NUMBER,REGISTER_SIGN,REGISTER_STATUS FROM smoc.account_sign_register_for_file ");
		sql.append("WHERE REGISTER_STATUS != '0'");
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String accountID = rs.getString("ACCOUNT");
				String channelID = rs.getString("CHANNEL_ID");
				String registerExtendNumber = rs.getString("REGISTER_EXTEND_NUMBER");
				String registerSign = rs.getString("REGISTER_SIGN");
				String registerStatus = rs.getString("REGISTER_STATUS");
				
				String key = new StringBuilder().append(accountID).append(FixedConstant.SPLICER)
												.append(channelID).append(FixedConstant.SPLICER)
												.append(registerSign).toString();
				AccountSignRegisterForFile accountSignRegisterForFile = resultMap.get(key);
				if(accountSignRegisterForFile == null) {
					accountSignRegisterForFile = new AccountSignRegisterForFile();
					accountSignRegisterForFile.setAccountID(accountID);
					accountSignRegisterForFile.setChannelID(channelID);
					accountSignRegisterForFile.setRegisterSign(registerSign);
					accountSignRegisterForFile.setBeingRegisterExtendNumberSet(new HashSet<String>());
					accountSignRegisterForFile.setAlreadyRegisterExtendNumberSet(new HashSet<String>());
					resultMap.put(key, accountSignRegisterForFile);
				}
				if(String.valueOf(FixedConstant.SignRegisterStatus.ALREADY_REGISTER.ordinal()).equals(registerStatus)) {
					accountSignRegisterForFile.getAlreadyRegisterExtendNumberSet().add(registerExtendNumber);
				}else {
					accountSignRegisterForFile.getBeingRegisterExtendNumberSet().add(registerExtendNumber);
				}
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
		
		private Set<String> alreadyRegisterExtendNumberSet;
		
		private Set<String> beingRegisterExtendNumberSet;
		
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

		public Set<String> getAlreadyRegisterExtendNumberSet() {
			return alreadyRegisterExtendNumberSet;
		}

		public void setAlreadyRegisterExtendNumberSet(Set<String> alreadyRegisterExtendNumberSet) {
			this.alreadyRegisterExtendNumberSet = alreadyRegisterExtendNumberSet;
		}

		public Set<String> getBeingRegisterExtendNumberSet() {
			return beingRegisterExtendNumberSet;
		}

		public void setBeingRegisterExtendNumberSet(Set<String> beingRegisterExtendNumberSet) {
			this.beingRegisterExtendNumberSet = beingRegisterExtendNumberSet;
		}

		@Override
		public String toString() {
			return "AccountSignRegisterForFile [accountID=" + accountID + ", channelID=" + channelID + ", registerSign="
					+ registerSign + ", alreadyRegisterExtendNumberSet=" + alreadyRegisterExtendNumberSet
					+ ", beingRegisterExtendNumberSet=" + beingRegisterExtendNumberSet + "]";
		}
	}
}
