/**
 * @desc
 * 
 */
package com.base.common.log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.base.common.constant.LogPathConstant;
import com.base.common.util.DateUtil;
import com.base.common.vo.LogValue;
import com.base.common.worker.SuperQueueWorker;

public class AccessBusinessLogManager extends SuperQueueWorker<LogValue>{
	
	private static AccessBusinessLogManager manager = new AccessBusinessLogManager();
	
	/**
	 * Worker集合:企业标识-Worker
	 */
	Map<String,Worker> workerMap = new HashMap<String, AccessBusinessLogManager.Worker>();
	
	private AccessBusinessLogManager(){
		this.start();
	}
	
	public static AccessBusinessLogManager getInstance(){
		return manager;
	}
	
	public void process(String contentLog,String enterpriseFlag,String lable){
		add(new LogValue(contentLog, enterpriseFlag,lable));
	}

	@Override
	public void doRun() throws Exception{
		LogValue logValue = take();
		Worker worker = workerMap.get(logValue.getCategory());
		if(worker == null){
			worker = new Worker(logValue.getCategory());
			workerMap.put(logValue.getCategory(), worker);
			worker.start();
		}
		worker.add(logValue);
	}
	
	class Worker extends SuperQueueWorker<LogValue>{
		private String category;
		
		public Worker(String category) {
			this.category = category.toLowerCase();
		}

		@Override
		protected void doRun() throws Exception {
			LogValue logValue = take();
			String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
			.append(LogPathConstant.getAccessBusinessFilePathPart(logValue.getLable()))
			.toString();
			
			String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(logValue.getLable()))
			.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_HOUR))
			.append(".")
			.append(category)
			.append(".")
			.append(LogPathConstant.LOCALHOST_IP)
			.toString();
			
			File file = new File(filePath,fileName);
			
			FileUtils.writeStringToFile(file, logValue.getContent(), "UTF-8", true);
		}
		
	}
	
}


