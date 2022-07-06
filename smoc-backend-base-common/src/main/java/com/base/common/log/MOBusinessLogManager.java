/**
 * @desc
 * @author ma
 * @date 2017年7月14日
 * 
 */
package com.base.common.log;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.base.common.constant.LogPathConstant;
import com.base.common.util.DateUtil;
import com.base.common.worker.SuperQueueWorker;

/**
 * 代理协议层记录上行日志
 */
public class MOBusinessLogManager extends SuperQueueWorker<String>{
	
	private static MOBusinessLogManager manager = new MOBusinessLogManager();

	private MOBusinessLogManager(){
		this.start();
	}
	
	public static MOBusinessLogManager getInstance(){
		return manager;
	}
	
	public void process(String contentLog){
		add(contentLog);
	}

	@Override
	public void doRun() throws Exception {
		String contentLog = take();
		String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH).append(LogPathConstant.BUSINESS_MO_LOG_PATH).toString();
		
		String fileName = new StringBuilder().append(LogPathConstant.LOG_FILENAME_PREFIX_MO)
											 .append(DateFormatUtils.format(new Date(), DateUtil.DATE_FORMAT_COMPACT_HOUR)).append(".").append(LogPathConstant.LOCALHOST_IP).toString();
		
		File file = new File(filePath,fileName);
		FileUtils.writeStringToFile(file, contentLog, "UTF-8", true);
		
	}
}
