package com.business.statistics.message.proxy;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.constant.LogPathConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.manager.AlarmManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.vo.AlarmMessage;
import com.base.common.vo.BusinessRouteValue;
import com.business.statistics.util.FileFilterUtil;

/**
 * business/proxy/mt business/proxy/mr
 * 代理业务层 mt下发记录和mr回执记录 以客户提交时间(取天yyyyMMdd)为后缀  proxy_message_info_yyyyMMdd
 */
public class ProxyBusinessMessageLogMerge {
	
	//代理业务层mt下发记录字段数量
	private static final int BUSINESS_PROXY_MT_MESSAGE_LOG_LENGTH = ResourceManager.getInstance().getIntValue("business.proxy.mt.message.log.length");
	//代理业务层mr回执记录字段数量
	private static final int BUSINESS_PROXY_MR_MESSAGE_LOG_LENGTH = ResourceManager.getInstance().getIntValue("business.proxy.mr.message.log.length");
	
	//告警信息
	private static String ALARM_VALUE = "ProxyBusinessMessageLogMerge";
	//代理业务层按条记录表前缀
	private static String TABLENAME_PREFIX = "proxy_message_info_";
	
	//线程池核心线程数占CPU核数的倍数
	private static int WORKER_MULTIPLE = ResourceManager.getInstance().getIntValue("worker.multiple");
	//整体耗时阈值，当超过时加入告警
	private static int ELAPSED_TIME_THRESHOLD = ResourceManager.getInstance().getIntValue("elapsed.time.threshold");
	
	static{
		//为0设置默认值
		if(WORKER_MULTIPLE == 0){
			WORKER_MULTIPLE = 1;
		}
		//为0设置默认值
		if(ELAPSED_TIME_THRESHOLD == 0){
			ELAPSED_TIME_THRESHOLD = 45 * 1000;
		}
	}

	//线程池
	private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(FixedConstant.CPU_NUMBER*WORKER_MULTIPLE,
			FixedConstant.CPU_NUMBER*WORKER_MULTIPLE*2, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	static{
		threadPoolExecutor.prestartAllCoreThreads();
	}
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		try {	
				
			CategoryLog.proxyLogger.info("参数={}",Arrays.toString(args));
			
			Map<String,String> argMap = new HashMap<String, String>();
			for(String arg:args){
				if(arg.split("=").length == 2){
					argMap.put(arg.split("=")[0], arg.split("=")[1]);
				}
			}
			//默认前一分钟
			int afterMinute = -1;
			//传参以参数为准
			if (StringUtils.isNotEmpty(argMap.get("afterMinute"))) {
				afterMinute = Integer.valueOf(argMap.get("afterMinute"));
			}
			//确定文件日期
			String date = DateUtil.getAfterMinuteDateTime(afterMinute, DateUtil.DATE_FORMAT_COMPACT_HOUR);
			
			//确定有效时间段
			String lineTime = DateUtil.getAfterMinuteDateTime(afterMinute, DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE);
			//传参以参数为准
			if (StringUtils.isNotEmpty(argMap.get("lineTime"))) {
				lineTime = argMap.get("lineTime");
			}
			
			//下行文件路径
			String mtFilePath  = argMap.get("mtFilePath");
			//回执文件路径
			String mrFilePath = argMap.get("mrFilePath");

			//入库mt数据：长短信只取第一条，提交响应码通过追加的方式index=responseCode,index=responseCode
			mergeMessageMtLog(lineTime,date,mtFilePath);
			
			//入库mr数据：长短信状态码通过追加的方式index=statusCode,index=statusCode
			mergeMessageMrLog(lineTime,date,mrFilePath);
			
		} catch (Exception e) {
			CategoryLog.proxyLogger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmMessage.AlarmKey.BusinessStatistics, 
					new StringBuilder(ALARM_VALUE).append(":").append(e.getMessage()).toString()));
		}

		//消耗时间
		long elapsedTime = System.currentTimeMillis() - start;
		
		//应该在指定时间内完成
		if( elapsedTime > ELAPSED_TIME_THRESHOLD ){
			AlarmManager.getInstance().process(new AlarmMessage(AlarmMessage.AlarmKey.BusinessStatistics, 
					new StringBuilder(ALARM_VALUE).append(":耗时").append(elapsedTime).append("毫秒").toString()));
		}
		
		CategoryLog.proxyLogger.info("程序正常退出");
		System.exit(0);
	}
	
	/**
	 * 加载下行日志
	 * @param lineTime
	 * @param afterMinute
	 */
	static private void mergeMessageMtLog(String lineTime,String date,String mtFilePath) throws Exception{
		long start = System.currentTimeMillis();
		
		Vector<Future<Integer>> calls = new Vector<Future<Integer>>();
		int fileNumber = 0;
		
		//当参数传文件路径时以参数为准
		File[] fileList = null;
		if(StringUtils.isNotEmpty(mtFilePath)){
			ArrayList<File> files = new ArrayList<File>();
			for(String file : mtFilePath.split(",")){
				files.add(new File(file));
			}
			fileList = files.toArray(new File[files.size()]);
		}else{
			//下行文件路径
			String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
					.append(LogPathConstant.getProxyBusinessFilePathPart(FixedConstant.RouteLable.MT.name()))
					.toString();
			//下行文件名前缀
			String fileNamePrefix = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MT.name()))
					.append(date).toString();
			
			CategoryLog.proxyLogger.info("文件路径={},文件前缀={},时间段={}",filePath,fileNamePrefix,lineTime);
			
			fileList = FileFilterUtil.findFile(filePath, fileNamePrefix);
		}
	
		fileNumber = fileList.length;
		for(File file : fileList) {
			CategoryLog.proxyLogger.info("启动加载文件={}线程",file.getAbsolutePath());
			//一个文件启动一个线程进行处理
			ProxyBusinessMessageLogMerge.MtWorker worker = new ProxyBusinessMessageLogMerge().new MtWorker(file, lineTime);
			Future<Integer> call = threadPoolExecutor.submit(worker);
			calls.add(call);
		}
		
		int rowNumber = 0;
		//循环结束 所有子线程处理完毕
		for (Future<Integer> call : calls) {			
			rowNumber += call.get();		
		}
		
		CategoryLog.proxyLogger.info("下行时间段={},文件数={},总条数={},总耗时={}",lineTime,fileNumber,rowNumber,(System.currentTimeMillis() - start));
	}
	
	/**
	 * 加载状态报告文件
	 * @param lineTime
	 * @param afterMinute
	 */
	static private void mergeMessageMrLog(String lineTime,String date,String mrFilePath) throws Exception{
		long start = System.currentTimeMillis();
		
		Vector<Future<Integer>> calls = new Vector<Future<Integer>>();
		int fileNumber = 0;
		
		//当参数传文件路径时以参数为准
		File[] fileList = null;
		if(StringUtils.isNotEmpty(mrFilePath)){
			ArrayList<File> files = new ArrayList<File>();
			for(String file : mrFilePath.split(",")){
				files.add(new File(file));
			}
			fileList = files.toArray(new File[files.size()]);
		}else{
			//状态报告文件路径
			String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
					.append(LogPathConstant.getProxyBusinessFilePathPart(FixedConstant.RouteLable.MR.name()))
					.toString();
			//状态报告文件名前缀
			String fileNamePrefix = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MR.name()))
					.append(date).toString();
			
			CategoryLog.proxyLogger.info("文件路径={},文件前缀={},时间段={}",filePath,fileNamePrefix,lineTime);
			
			fileList = FileFilterUtil.findFile(filePath, fileNamePrefix);
		}
		
		fileNumber = fileList.length;
		for(File file : fileList) {
			CategoryLog.proxyLogger.info("启动加载文件={}线程",file.getAbsolutePath());
			//一个文件启动一个线程进行处理
			ProxyBusinessMessageLogMerge.MrWorker worker = new ProxyBusinessMessageLogMerge().new MrWorker(file, lineTime);
			Future<Integer> call = threadPoolExecutor.submit(worker);
			calls.add(call);
		}
		
		int rowNumber = 0;
		//循环结束 所有子线程处理完毕
		for (Future<Integer> call : calls) {
			rowNumber += call.get();
		}
		
		CategoryLog.proxyLogger.info("回执时间段={},文件数={},总条数={},总耗时={}",lineTime,fileNumber,rowNumber,(System.currentTimeMillis() - start));
	}

	class MtWorker implements Callable<Integer> {
		private File file;
		private String lineTime;

		public MtWorker(File file, String lineTime) {
			this.file = file;
			this.lineTime = lineTime;
		}

		@Override
		public Integer call() throws Exception {
			int rowNumber = 0;
			long start = System.currentTimeMillis();
			try {
				Map<String,List<BusinessRouteValue>> businessRouteValueMap = loadMtFile(file, lineTime);
				
				//insert map
				Map<String,List<BusinessRouteValue>> insertBusinessRouteValueMap = new HashMap<String, List<BusinessRouteValue>>();
				//update map
				Map<String,List<BusinessRouteValue>> updateBusinessRouteValueMap = new HashMap<String, List<BusinessRouteValue>>();
				
				//按照insert和update分组
				for(Map.Entry<String,List<BusinessRouteValue>> entry : businessRouteValueMap.entrySet()){
					String[] keys = entry.getKey().split(FixedConstant.SPLICER);
					String tableNameSuffix = keys[0];
					int index = Integer.parseInt(keys[1]);
					List<BusinessRouteValue> businessRouteValueList = entry.getValue();	
					rowNumber+=businessRouteValueList.size();
					StringBuilder tableName = new StringBuilder();
					tableName.append(TABLENAME_PREFIX).append(tableNameSuffix);
					if(index == 1){
						insertBusinessRouteValueMap.put(tableName.toString(), businessRouteValueList);
					}else{
						updateBusinessRouteValueMap.put(tableName.toString(), businessRouteValueList);
					}
				}
				//优先保存
				for(Map.Entry<String,List<BusinessRouteValue>> entry : insertBusinessRouteValueMap.entrySet()){
					saveProxyMessageMtInfo(entry.getValue(),entry.getKey());
					CategoryLog.proxyLogger.info("下行保存：tableName={},条数={}",entry.getKey(),entry.getValue().size());
				}
				for(Map.Entry<String,List<BusinessRouteValue>> entry : updateBusinessRouteValueMap.entrySet()){
					updateProxyMessageMtInfo(entry.getValue(),entry.getKey());
					CategoryLog.proxyLogger.info("下行更新：tableName={},条数={}",entry.getKey(),entry.getValue().size());
				}
				
				CategoryLog.proxyLogger.info("文件={},时间段={},条数={},耗时={}",file.getName(),lineTime,rowNumber,(System.currentTimeMillis() - start));
			} catch (Exception e) {
				CategoryLog.proxyLogger.error(e.getMessage(), e);
				throw e;
			}

			CategoryLog.proxyLogger.info("线程退出循环");
			return rowNumber;

		}
	}
	
	class MrWorker implements Callable<Integer> {
		private File file;
		private String lineTime;

		public MrWorker(File file, String lineTime) {
			this.file = file;
			this.lineTime = lineTime;
		}

		@Override
		public Integer call() throws Exception {
			int rowNumber = 0;
			long start = System.currentTimeMillis();
			try {
				Map<String,List<BusinessRouteValue>> businessRouteValueMap = loadMrFile(file, lineTime);
				
				for(Map.Entry<String,List<BusinessRouteValue>> entry : businessRouteValueMap.entrySet()){
					String tableNameSuffix = entry.getKey();
					List<BusinessRouteValue> businessRouteValueList = entry.getValue();	
					rowNumber+=businessRouteValueList.size();
					StringBuilder tableName = new StringBuilder();
					tableName.append(TABLENAME_PREFIX).append(tableNameSuffix);
					updateProxyMessageMrInfo(businessRouteValueList,tableName.toString());
					CategoryLog.proxyLogger.info("回执更新：tableName={},条数={}",tableName.toString(),businessRouteValueList.size());
				}
				
				CategoryLog.proxyLogger.info("文件={},时间段={},条数={},耗时={}",file.getName(),lineTime,rowNumber,(System.currentTimeMillis() - start));
			} catch (Exception e) {
				CategoryLog.proxyLogger.error(e.getMessage(), e);
				throw e;
			}
			CategoryLog.proxyLogger.info("线程退出循环");
			return rowNumber;

		}
	}
	
	/**
	 * 	读取文件中的业务日志 然后根据channelSRCID 进行分组处理
	 * @param file
	 * @param lineTime
	 * @return
	 * @throws Exception
	 */
	private Map<String,List<BusinessRouteValue>> loadMtFile(File file,String lineTime) throws Exception {
		LineIterator lines = null;
		String[] array = null;
		String line = null;
		Map<String,List<BusinessRouteValue>> businessRouteValueListMap = new HashMap<String, List<BusinessRouteValue>>();
		BusinessRouteValue businessRouteValue = null;
		try {
			lines = FileUtils.lineIterator(file, "utf-8");
			StringBuilder sb = new StringBuilder();
			while (lines.hasNext()) {
				line = lines.next();
				//时间过滤
				if (!line.startsWith(lineTime)) {
					continue;
				}
				//拆分及字段不全过滤
				array = line.split(FixedConstant.LOG_SEPARATOR);
				if (array.length < BUSINESS_PROXY_MT_MESSAGE_LOG_LENGTH) {
					CategoryLog.proxyLogger.warn("错误文件={},错误行={}",file.getAbsolutePath(),line);
					continue;
				}
				//数据封装，长短信只处理第一条
				businessRouteValue = new BusinessRouteValue();
				businessRouteValue.setAccountID(array[1]);
				businessRouteValue.setEnterpriseFlag(array[2]);
				//businessRouteValue.setProtocol(array[3]);
				businessRouteValue.setAccountSubmitTime(array[4]);
				//businessRouteValue.setTableAuditTime(array[5]);
				//businessRouteValue.setTableSubmitTime(array[6]);
				//businessRouteValue.setQueueSubmitTime(array[7]);
				businessRouteValue.setChannelSubmitTime(array[8]);
				businessRouteValue.setBusinessMessageID(array[9]);
				businessRouteValue.setPhoneNumber(array[10]);
				//businessRouteValue.setSegmentCarrier(array[11]);
				businessRouteValue.setBusinessCarrier(array[12]);
				//businessRouteValue.setAreaCode(array[13]);
				businessRouteValue.setAreaName(array[14]);
				businessRouteValue.setCityName(array[15]);
				businessRouteValue.setChannelSubmitSRCID(array[16]);
				//businessRouteValue.setAccountExtendCode(array[17]);
				//businessRouteValue.setAccountBusinessCode(array[18]);
				//businessRouteValue.setAccountSubmitSRCID(array[19]);
				//businessRouteValue.setMessageFormat(array[20]);
				businessRouteValue.setMessageContent(array[21]);
				//businessRouteValue.setMessageSignature(array[22]);
				//businessRouteValue.setInfoType(array[23]);
				//businessRouteValue.setIndustryTypes(array[24]);
				//businessRouteValue.setAccountPriority(array[25]);
				//businessRouteValue.setAccountMessageIDs(array[26]);
				//businessRouteValue.setAccountTaskID(array[26]);
				businessRouteValue.setChannelID(array[27]);
				//businessRouteValue.setChannelSRCID(array[28]);
				businessRouteValue
						.setChannelTotal(Integer.parseInt(array[29]));
				businessRouteValue
						.setChannelIndex(Integer.parseInt(array[30]));
				//businessRouteValue.setNextNodeCode(array[32]);
				businessRouteValue.setNextNodeErrorCode(array[33]);
				//businessRouteValue.setChannelMessageID(array[34]);// 35-提交响应消息id
				//businessRouteValue.setChannelPrice(array[35]);
				//businessRouteValue.setPriceAreaCode(array[36]);// 37-价格区域编码
				//businessRouteValue.setFinanceAccountID(array[37]);// 38-计费账号
				//businessRouteValue.setMessageNumber(array[38]);
				//businessRouteValue.setMessagePrice(array[39]);// 40-账号计费单价
				//businessRouteValue.setMessageAmount(array[40]);
				businessRouteValue.setBusinessType(array[41]);
				//businessRouteValue.setAccountTemplateID(array[42]);
				businessRouteValue
						.setRepeatSendTimes(Integer.parseInt(array[43]));
				//businessRouteValue.setOptionParam(array[44]);
				//businessRouteValue.setConsumeType(array[45]);// 45-扣费类型
				String tableNameSuffix = businessRouteValue.getAccountSubmitTime().substring(0, 10).replaceAll("-", "");
				//1代表insert，2代表update
				int index = businessRouteValue.getChannelIndex()>1?2:1;
				//以客户提交时间,条数序号,按照表名后缀分组
				String key = sb.append(tableNameSuffix).append(FixedConstant.SPLICER).append(index).toString();
				List<BusinessRouteValue> businessRouteValueList = businessRouteValueListMap.get(key);
				if(businessRouteValueList == null){
					businessRouteValueList = new ArrayList<BusinessRouteValue>();
					businessRouteValueListMap.put(key, businessRouteValueList);
				}
				
				businessRouteValueList.add(businessRouteValue);
				sb.setLength(0);
			}
		} catch (Exception e) {
			CategoryLog.proxyLogger.error(e.getMessage(),e);
			throw e;
		}

		return businessRouteValueListMap;
	}
	
	/**
	 * 	读取文件中的业务日志 然后根据channelSRCID 进行分组处理
	 * @param file
	 * @param lineTime
	 * @return
	 * @throws Exception
	 */
	private Map<String,List<BusinessRouteValue>> loadMrFile(File file,String lineTime) throws Exception {
		LineIterator lines = null;
		String[] array = null;
		String line = null;
		Map<String,List<BusinessRouteValue>> businessRouteValueListMap = new HashMap<String, List<BusinessRouteValue>>();
		BusinessRouteValue businessRouteValue = null;
		try {
			lines = FileUtils.lineIterator(file, "utf-8");

			while (lines.hasNext()) {
				line = lines.next();
				//时间过滤
				if (!line.startsWith(lineTime)) {
					continue;
				}
				//拆分及字段不全过滤
				array = line.split(FixedConstant.LOG_SEPARATOR);
				if (array.length < BUSINESS_PROXY_MR_MESSAGE_LOG_LENGTH) {
					CategoryLog.proxyLogger.warn("错误文件={},错误行={}",file.getAbsolutePath(),line);
					continue;
				}
				//数据封装，长短信只处理第一条
				businessRouteValue = new BusinessRouteValue();
				businessRouteValue.setAccountSubmitTime(array[4]);
				businessRouteValue.setChannelReportTime(array[8]);
				businessRouteValue.setBusinessMessageID(array[9]);
				businessRouteValue.setChannelID(array[26]);
				businessRouteValue.setStatusCode(array[29]);
				//复用可选参数用来存间隔时间
				businessRouteValue.setOptionParam(array[31]);
				businessRouteValue.setChannelIndex(Integer.parseInt(array[33]));

				String tableNameSuffix = businessRouteValue.getAccountSubmitTime().substring(0, 10).replaceAll("-", "");

				//以客户提交时间,条数序号,按照表名分组
				List<BusinessRouteValue> businessRouteValueList = businessRouteValueListMap.get(tableNameSuffix);
				if(businessRouteValueList == null){
					businessRouteValueList = new ArrayList<BusinessRouteValue>();
					businessRouteValueListMap.put(tableNameSuffix, businessRouteValueList);
				}
				
				businessRouteValueList.add(businessRouteValue);
				
			}
		} catch (Exception e) {
			CategoryLog.proxyLogger.error(e.getMessage(),e);
			throw e;
		}

		return businessRouteValueListMap;
	}
	
	private void updateProxyMessageMrInfo(List<BusinessRouteValue> businessRouteValues,String tableName) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append("update smoc_route.").append(tableName);
		sql.append(" set STATUS_CODE_EXTEND = CONCAT_WS(',',STATUS_CODE_EXTEND,?) ,TIME_ELAPSED = ?,STATUS_CODE=?,CHANNEL_REPORT_TIME=?,UPDATED_TIME=NOW()");
		sql.append(" where BUSINESS_MESSAGE_ID = ? and CHANNEL_ID = ? ");
		
		int num = 0;
		
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			
			StringBuilder sb = new StringBuilder();
			for (BusinessRouteValue businessRouteValue : businessRouteValues) {
				num++;
				sb.append(businessRouteValue.getChannelIndex()).append("=").append(businessRouteValue.getStatusCode());
				pstmt.setString(1, sb.toString());
				pstmt.setInt(2, Integer.parseInt(businessRouteValue.getOptionParam()));
				pstmt.setString(3, businessRouteValue.getStatusCode());
				pstmt.setString(4, businessRouteValue.getChannelReportTime());
				pstmt.setString(5, businessRouteValue.getBusinessMessageID());	
				pstmt.setString(6, businessRouteValue.getChannelID());
				//CategoryLog.proxyLogger.info(businessRouteValue.getBusinessMessageID()+","+businessRouteValue.getChannelID());
				sb.setLength(0);
				pstmt.addBatch();
				if(num % 10000 == 0) {
					pstmt.executeBatch();
				}
			}

			pstmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			CategoryLog.proxyLogger.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				CategoryLog.proxyLogger.error(e1.getMessage(), e1);
			}
			throw e;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
	}
	
	/**
	 * 长短信更新index>1的提交响应码
	 * @param businessRouteValues
	 * @param tableName
	 */
	private void updateProxyMessageMtInfo(List<BusinessRouteValue> businessRouteValues,String tableName) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append("update smoc_route.").append(tableName);
		sql.append(" set RESPONSE_CODE_EXTEND = CONCAT_WS(',',RESPONSE_CODE_EXTEND,?) ");
		sql.append(" where BUSINESS_MESSAGE_ID = ? and CHANNEL_ID = ? ");
		
		int num = 0;
		
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			
			StringBuilder sb = new StringBuilder();
			for (BusinessRouteValue businessRouteValue : businessRouteValues) {
				num++;
				sb.append(businessRouteValue.getChannelIndex()).append("=").append(businessRouteValue.getNextNodeErrorCode());
				pstmt.setString(1, sb.toString());
				pstmt.setString(2, businessRouteValue.getBusinessMessageID());
				pstmt.setString(3, businessRouteValue.getChannelID());
				sb.setLength(0);
				pstmt.addBatch();
				if(num % 10000 == 0) {
					pstmt.executeBatch();
				}
			}

			pstmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			CategoryLog.proxyLogger.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				CategoryLog.proxyLogger.error(e1.getMessage(), e1);
			}
			throw e;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
	}
	
	private void saveProxyMessageMtInfo(List<BusinessRouteValue> businessRouteValues,String tableName) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.").append(tableName);
		sql.append(" (ID,ACCOUNT_SUBMIT_TIME,ACCOUNT_ID,ENTERPRISE_FLAG,BUSINESS_TYPE,PHONE_NUMBER,BUSINESS_MESSAGE_ID,MESSAGE_CONTENT,MESSAGE_TOTAL,CARRIER,");
		sql.append("AREA_NAME,CITY_NAME,CHANNEL_ID,CHANNEL_SUBMIT_TIME,RESPONSE_CODE,CHANNEL_SUBMIT_SRCID,REPEAT_SEND_TIMES,RESPONSE_CODE_EXTEND,");
		sql.append("CREATED_TIME)");
		sql.append(" values(");
		sql.append("?,?,?,?,?,?,?,?,?,?,");
		sql.append("?,?,?,?,?,?,?,?,");
		sql.append("NOW())");
		
		CategoryLog.proxyLogger.debug("sql={}",sql.toString());
		// 在一个事务中更新数据
		int num = 0;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (BusinessRouteValue businessRouteValue : businessRouteValues) {
				num++;
				pstmt.setString(1, UUID.randomUUID().toString().replace("-", ""));
				pstmt.setString(2, businessRouteValue.getAccountSubmitTime());
				pstmt.setString(3, businessRouteValue.getAccountID());
				pstmt.setString(4, businessRouteValue.getEnterpriseFlag());
				pstmt.setString(5, businessRouteValue.getBusinessType());
				pstmt.setString(6, businessRouteValue.getPhoneNumber());
				pstmt.setString(7, businessRouteValue.getBusinessMessageID());
				pstmt.setString(8, businessRouteValue.getMessageContent());
				pstmt.setInt(9, businessRouteValue.getMessageTotal());				
				pstmt.setString(10, businessRouteValue.getBusinessCarrier());
				
				pstmt.setString(11, businessRouteValue.getAreaName());
				pstmt.setString(12, businessRouteValue.getCityName());
				pstmt.setString(13, businessRouteValue.getChannelID());
				pstmt.setString(14, businessRouteValue.getChannelSubmitTime());
				pstmt.setString(15, businessRouteValue.getNextNodeErrorCode());
				pstmt.setString(16, businessRouteValue.getChannelSubmitSRCID());
				pstmt.setInt(17, businessRouteValue.getRepeatSendTimes());
				pstmt.setString(18, "1="+businessRouteValue.getNextNodeErrorCode());
				
				pstmt.addBatch();
				if(num % 10000 == 0) {
					pstmt.executeBatch();
				}
			}

			pstmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			CategoryLog.proxyLogger.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				CategoryLog.proxyLogger.error(e1.getMessage(), e1);
			}
			throw e;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
	}
		
}
