package com.business.statistics.message.access;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.LogPathConstant;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.util.TableNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;
import com.business.statistics.message.access.AccessBusinessDao;
import com.business.statistics.util.FileFilterUtil;

public class AccessBusinessMtLog {
	private static final Logger logger = LoggerFactory.getLogger(AccessBusinessMtLog.class);
	//接入业务层一条mt日志字符串个数
	private static final int ACCESS_BUSINESS_MT_MESSAGE_LOG_LENGTH = ResourceManager.getInstance().getIntValue("access.business.mt.message.log.length");

	public static void main(String[] args) {
		logger.info(Arrays.toString(args));
		String lineTime = null;
		
		int afterMinute = -1;
		if (!(args.length == 1 || args.length == 0)) {
			logger.error("参数不符合规范");
			System.exit(0);
		}
		//创建一个线程池
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(FixedConstant.CPU_NUMBER * 8, Integer.MAX_VALUE,
				100000L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		threadPoolExecutor.prestartAllCoreThreads();
		
		try {
			//通过传参 确定读取时间
			if (args.length >= 1) {
				afterMinute = Integer.valueOf(args[0]);
			}
			//当前系统前一分钟的时间  格式：yyyy-MM-dd HH:mm
			
			lineTime = DateUtil.getAfterMinuteDateTime(afterMinute, DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE);
			
			//日志文件路径	
			
			String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
					.append(LogPathConstant.getAccessBusinessFilePathPart(FixedConstant.RouteLable.MT.name()))
					.toString();
		
			
			//日志文件名称	
		
			String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MT.name()))
					.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_HOUR)).toString();
			
			Vector<Future<Integer>> calls = new Vector<Future<Integer>>();
			int fileNumber = 0;
			// 得到yyyyMMddHH格式获取文件
			if (StringUtils.isNotEmpty(filePath) && StringUtils.isNotEmpty(fileName)) {
				//得到符合文件数组
				File[] fileList = FileFilterUtil.findFile(filePath, fileName);
				fileNumber = fileList.length;
				
				
				for (File file : fileList) {
					//一个文件用一个线程去处理
					
					Worker worker = new AccessBusinessMtLog().new Worker(file, lineTime);
					Future<Integer> call = threadPoolExecutor.submit(worker);
					calls.add(call);
				}
			}
			int rowNumber = 0;
			for (Future<Integer> call : calls) {
				try {					
					rowNumber += call.get();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			} 
			
			logger.info("{}处理文件数量:{},处理数据行数:{}",fileNumber,rowNumber);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		logger.info("程序正常退出");
		System.exit(0);

	}

	class Worker implements Callable<Integer> {

		private File file;
		private String lineTime;

		public Worker(File file, String lineTime) {
			this.file = file;
			this.lineTime = lineTime;
		}

		@Override
		public Integer call() throws Exception {
			int rowNumber = 0;
			try {
				//得到文件内符合的数据
				Map<String,List<BusinessRouteValue>> enterpriseMap = getAccessBusinessMtData(file, lineTime);
				
				
				String tableName = null;
				for (String EnterpriseFlag : enterpriseMap.keySet()) {
					tableName = TableNameGeneratorUtil.generateEnterpriseMessageMRInfoTableName(EnterpriseFlag);
					logger.info("表{} 保存数据{}条", tableName, enterpriseMap.get(EnterpriseFlag).size());
					rowNumber += enterpriseMap.get(EnterpriseFlag).size();					
					AccessBusinessDao.insertMtLog(enterpriseMap.get(EnterpriseFlag), tableName);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			return rowNumber;
		}
	}

	/**
	 * @param file
	 * @param lineTime
	 * @return 获取mt文件内前一分种下发的数据
	 * @throws Exception
	 */
	public Map<String,List<BusinessRouteValue>> getAccessBusinessMtData(File file, String lineTime) throws Exception {
		List<String> lines = null;
		String[] array = null;
		boolean error = false;
		String error_line = null;
		String error_file = null;
		Map<String,List<BusinessRouteValue>> enterpriseMap = new HashMap<String,List<BusinessRouteValue>>();
		BusinessRouteValue businessRouteValue = null;

		
		try {
			lines = FileUtils.readLines(file,"utf-8");
			
			for (String line : lines) {
				array = line.split(FixedConstant.LOG_SEPARATOR);
				
				if (array.length <ACCESS_BUSINESS_MT_MESSAGE_LOG_LENGTH) {					
					error = true;
					error_line = line;
					error_file = file.getAbsolutePath();
					continue;

				}
				//得到一分钟之前的数据
				if (array[0].startsWith(lineTime)) {
					businessRouteValue = new BusinessRouteValue();
				
					businessRouteValue.setAccountID(array[1]);
				
					businessRouteValue.setPhoneNumber(array[6]);
			
					businessRouteValue.setBusinessCarrier(array[8]);
			
					businessRouteValue.setAreaName(array[11]);
			
					businessRouteValue.setAccountSubmitTime(array[4]);
				
					businessRouteValue.setMessageContent( array[16]);
			
					businessRouteValue.setAccountMessageIDs(array[22]);
			
					businessRouteValue.setChannelTemplateID(array[29]);
			
					businessRouteValue.setAccountSubmitSRCID( array[13]);
			
					businessRouteValue.setAccountBusinessCode(array[14]);
			
					businessRouteValue.setChannelTotal(Integer.valueOf(array[18]));
				
					businessRouteValue.setBusinessMessageID(array[5]);
					
					List<BusinessRouteValue> businessRouteValues = enterpriseMap.get(array[2]);
					if(businessRouteValues == null) {
						businessRouteValues = new ArrayList<BusinessRouteValue>();
						enterpriseMap.put(array[2], businessRouteValues);
					}
					businessRouteValues.add(businessRouteValue);
				}
					
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		if (error) {
			logger.warn("错误文件={},错误行数={}", error_file, error_line);
		}
		return enterpriseMap;
	}
}
