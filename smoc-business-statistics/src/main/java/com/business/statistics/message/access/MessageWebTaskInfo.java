package com.business.statistics.message.access;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.base.common.vo.BusinessRouteValue;
import com.business.statistics.util.FileFilterUtil;

public class MessageWebTaskInfo {
	private static final Logger logger = LoggerFactory.getLogger(MessageWebTaskInfo.class);
	// 接入业务层一条mr日志字符串个数
	private static final int ACCESS_BUSINESS_MR_MESSAGE_LOG_LENGTH = ResourceManager.getInstance().getIntValue("access.business.mr.message.log.length");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info(Arrays.toString(args));
		String lineTime = null;

		int afterMinute = -1;
		if (!(args.length == 1 || args.length == 0)) {
			logger.error("参数不符合规范");
			System.exit(0);
		}
		// 创建一个线程池
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(FixedConstant.CPU_NUMBER * 8, Integer.MAX_VALUE,
				100000L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		threadPoolExecutor.prestartAllCoreThreads();

		try {
			// 通过传参 确定读取时间
			if (args.length >= 1) {
				afterMinute = Integer.valueOf(args[0]);
			}
			// 当前系统前一分钟的时间 格式：yyyy-MM-dd HH:mm
			lineTime = DateUtil.getAfterMinuteDateTime(afterMinute, DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE);
			//日志文件路径
			String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
					.append(LogPathConstant.getAccessBusinessFilePathPart(FixedConstant.RouteLable.MR.name()))
					.toString();
			//日志文件名称
			String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MR.name()))
					.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_HOUR)).toString();
			Vector<Future<Integer>> calls = new Vector<Future<Integer>>();
			int fileNumber = 0;
			// 得到yyyyMMddHH格式获取文件
			if (StringUtils.isNotEmpty(filePath) && StringUtils.isNotEmpty(fileName)) {
				//得到文件数组
				File[] fileList = FileFilterUtil.findFile(filePath, fileName);
				fileNumber = fileList.length;
				for(File file:fileList) {
					
					worker  worker=new MessageWebTaskInfo().new worker(file, lineTime);
					Future<Integer> call =threadPoolExecutor.submit(worker);
					calls.add(call);
				}
				

			} 
			int rowNumber = 0;
			for(Future<Integer> call : calls) {
				
				try {
					rowNumber += call.get();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				
			}
			logger.info("处理文件数量:{},处理数据行数:{}",fileNumber,rowNumber);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("程序正常退出");
		System.exit(0);

	}
	
	class worker implements Callable<Integer>{
		
		private File file;
		private String lineTime;
		
		public  worker(File file,String lineTime) {
			this.file=file;
			this.lineTime=lineTime;
			
		}


		@Override
		public Integer call() throws Exception {
			int rowNumber = 0;
			try {
				//得到文件内符合的数据
				List<BusinessRouteValue> businessRouteValuelist= getAccessBusinessMrData(file, lineTime);
				
					rowNumber += businessRouteValuelist.size();
					
					
					for(BusinessRouteValue businessRouteValues: businessRouteValuelist) {
						AccessBusinessDao.updateMessageWebTaskInfo(businessRouteValues);
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
	 * @return 获取mr文件内前一分钟返回的日志数据
	 * @throws Exception
	 */
	public List<BusinessRouteValue> getAccessBusinessMrData(File file, String lineTime) throws Exception {
		List<String> lines = null;
		String[] array = null;
		boolean error = false;
		String error_line = null;
		String error_file = null;
		
		List<BusinessRouteValue> businessRouteValuelist = new ArrayList<BusinessRouteValue>();
		BusinessRouteValue businessRouteValue = null;

		
		try {
			lines = FileUtils.readLines(file, "utf-8");			
			for (String line : lines) {
				array = line.split(FixedConstant.LOG_SEPARATOR);
				if (array.length <ACCESS_BUSINESS_MR_MESSAGE_LOG_LENGTH) {
					error = true;
					error_line = line;
					error_file = file.getAbsolutePath();
					continue;

				}
				//得到一分钟之前的数据
				if (array[0].startsWith(lineTime)) {
					businessRouteValue = new BusinessRouteValue();
																		
					businessRouteValue.setAccountMessageIDs(array[23]);
					
					businessRouteValue.setSuccessCode(array[24]);
					
					
					businessRouteValuelist.add(businessRouteValue);
				
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		if (error) {
			logger.warn("错误文件={},错误行数={}", error_file, error_line);
		}
		return businessRouteValuelist;
	}

}
