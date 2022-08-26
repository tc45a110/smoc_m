package com.inse.manager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.worker.SuperMapWorker;
import com.inse.manager.AccountChanelTemplateInfoManager.AccountChanelTemplateInfo;

public class AccountChanelTemplateInfoManager extends SuperMapWorker<String, AccountChanelTemplateInfo> {
	private static AccountChanelTemplateInfoManager manager = new AccountChanelTemplateInfoManager();

	private AccountChanelTemplateInfoManager() {
		loadData();
		this.start();
	}

	public static AccountChanelTemplateInfoManager getInstance() {
		return manager;
	}
	
	@Override
	protected void doRun() throws Exception {
		loadData();
		Thread.sleep(INTERVAL);
	}
	
	/**
	 * 获取通道模板id
	 * @param templateID
	 * @return
	 */
	public String getChannelTemplateID(String templateID) {
		AccountChanelTemplateInfo templateInfo = get(templateID);
		if (templateInfo != null) {
			return templateInfo.getChannelTemplateID();
		}
		return null;
	}
	
	/**
	 * 获取模板标识
	 * @param templateID
	 * @return
	 */
	public String getTemplateFlag(String templateID) {
		AccountChanelTemplateInfo templateInfo = get(templateID);
		if (templateInfo != null) {
			return templateInfo.getTemplateFlag();
		}
		return null;
	}
	
	
	/**
	 * 获取通道模板变量格式
	 * @param templateID
	 * @return
	 */
	public String getChannelTemplateVariableFormat(String templateID) {
		AccountChanelTemplateInfo templateInfo = get(templateID);
		if (templateInfo != null) {
			return templateInfo.getChannelTemplateVariableFormat();
		}
		return null;
	}
	
	public void loadData() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("select TEMPLATE_ID,CHANNEL_TEMPLATE_ID,CHANNEL_TEMPLATE_VARIABLE_FORMAT,TEMPLATE_FLAG ");
		sql.append(" from smoc.account_channel_template_info where TEMPLATE_STATUS ='2'");
		
		
		Map<String, AccountChanelTemplateInfo> templateMap = new HashMap<String, AccountChanelTemplateInfo>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AccountChanelTemplateInfo accountchanneltemplateinfo=new AccountChanelTemplateInfo();
				String templateID=rs.getString("TEMPLATE_ID");	
				accountchanneltemplateinfo.setChannelTemplateID(rs.getString(("CHANNEL_TEMPLATE_ID")));
				accountchanneltemplateinfo.setChannelTemplateVariableFormat(rs.getString(("CHANNEL_TEMPLATE_VARIABLE_FORMAT")));
				accountchanneltemplateinfo.setTemplateFlag(rs.getString(("TEMPLATE_FLAG")));
				templateMap.put(templateID, accountchanneltemplateinfo);
				
			}
			superMap = templateMap;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt , conn);		
		}
		
	}
	
	class AccountChanelTemplateInfo{
		
		/**通道模板id
		 * 
		 */
		private String channelTemplateID;
		
		/**通道模板状态
		 * 
		 */
		private String channelTemplateStatus;
		
		/**通道模板变量格式
		 * 
		 */
		private String channelTemplateVariableFormat;
		
		/**变量标识
		 * 
		 */
		private String templateFlag;

		public String getChannelTemplateID() {
			return channelTemplateID;
		}

		public void setChannelTemplateID(String channelTemplateID) {
			this.channelTemplateID = channelTemplateID;
		}

		public String getChannelTemplateStatus() {
			return channelTemplateStatus;
		}

		public void setChannelTemplateStatus(String channelTemplateStatus) {
			this.channelTemplateStatus = channelTemplateStatus;
		}

		public String getChannelTemplateVariableFormat() {
			return channelTemplateVariableFormat;
		}

		public void setChannelTemplateVariableFormat(String channelTemplateVariableFormat) {
			this.channelTemplateVariableFormat = channelTemplateVariableFormat;
		}

		public String getTemplateFlag() {
			return templateFlag;
		}

		public void setTemplateFlag(String templateFlag) {
			this.templateFlag = templateFlag;
		}
		
		
		
	}

}
