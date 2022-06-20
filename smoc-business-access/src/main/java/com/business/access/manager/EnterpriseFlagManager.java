/**
 * @desc
 * 
 */
package com.business.access.manager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import com.base.common.constant.TableNameConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.util.TableNameGeneratorUtil;
import com.base.common.worker.SuperQueueWorker;

/**
 * 维护临时表 enterprise_message_mr_info_tuo_EnterpriseFlag
 */
public class EnterpriseFlagManager extends SuperQueueWorker<String>{
	
	private static EnterpriseFlagManager manager = new EnterpriseFlagManager();
	
	private Set<String> enterpriseFlagSet = new HashSet<String>();
		
	private EnterpriseFlagManager(){
		this.start();
	}
	
	public static EnterpriseFlagManager getInstance(){
		return manager;
	}
	
	
	public void doRun() throws Exception {
		String enterpriseFlag = take();
		//当不包含enterpriseFlag则需新增表
		if(!enterpriseFlagSet.contains(enterpriseFlag)){
			createEnterpriseMessageMrInfoTable(TableNameGeneratorUtil.generateEnterpriseMessageMRInfoTableName(enterpriseFlag));
			enterpriseFlagSet.add(enterpriseFlag);
		}
	}
	
	/**
	 * 创建通道码号级下发信息表
	 * @param tablename
	 * @return
	 */
	private void createEnterpriseMessageMrInfoTable(String tablename) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		sql.append("{call smoc_route.");
		sql.append(TableNameConstant.PROCEDURE_CREATE_ENTERPRISE_MESSAGE_MR_INFO);
		sql.append("(");
		sql.append("?");
		sql.append(")} ");
	
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareCall(sql.toString());
			pstmt.setString(1, tablename);
			pstmt.execute();
			logger.info("初始化表{}",tablename);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
	}
	
}


