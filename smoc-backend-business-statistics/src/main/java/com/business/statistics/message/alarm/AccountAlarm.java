package com.business.statistics.message.alarm;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.LogPathConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.AccountInfoManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.vo.AlarmMessage;
import com.business.statistics.util.AlarmUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.io.FileFilter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.*;

public class AccountAlarm {
	// 接入业务层一条mt日志字符串个数
	private static final int ACCESS_BUSINESS_MT_MESSAGE_LOG_LENGTH = ResourceManager.getInstance()
			.getIntValue("access.business.mt.message.log.length");
	// 接入业务层一条mr日志字符串个数
	private static final int ACCESS_BUSINESS_MR_MESSAGE_LOG_LENGTH = ResourceManager.getInstance()
			.getIntValue("access.business.mr.message.log.length");

	// mt信息
	private static Map<String, AccountMessage> mtAccountMessageMap = new HashMap<String, AccountMessage>();

	// mr信息
	private static Map<String, AccountMessage> mrAccountMessageMap = new HashMap<String, AccountMessage>();
   //企业标识
	private static Set<String> enterpriseFlagSet = new HashSet<String>();
   //企业标识和该企业下的监控的账号
	private static Map<String, Set<String>> enterpriseFlagAccountMap = new HashMap<String, Set<String>>();
	//监控账号
	private static String[] accountIDs;
	// 线程池核心线程数占CPU核数的倍数
	private static int WORKER_MULTIPLE = ResourceManager.getInstance().getIntValue("worker.multiple");

	static {
		// 为0设置默认值
		if (WORKER_MULTIPLE == 0) {
			WORKER_MULTIPLE = 1;
		}

		String alarmAccountIDs = ResourceManager.getInstance().getValue("alarm.accountIds");
		if (StringUtils.isNotEmpty(alarmAccountIDs)) {
			accountIDs = alarmAccountIDs.split(",");
			for (String accountID : accountIDs) {
				String enterpriseFlag = AccountInfoManager.getInstance().getEnterpriseFlag(accountID);
				if (StringUtils.isNotEmpty(enterpriseFlag)) {
					enterpriseFlagSet.add(enterpriseFlag);
					Set<String> acccountSet = enterpriseFlagAccountMap.get(enterpriseFlag);
					if (acccountSet == null) {
						acccountSet = new HashSet<String>();
						enterpriseFlagAccountMap.put(enterpriseFlag, acccountSet);
					}
					acccountSet.add(accountID);
				}
			}
		}
		CategoryLog.accessLogger.info("监控的账号为：" + alarmAccountIDs);
	}

	// 线程池
	private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
			FixedConstant.CPU_NUMBER * WORKER_MULTIPLE, FixedConstant.CPU_NUMBER * WORKER_MULTIPLE * 2, 50,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	static {
		threadPoolExecutor.prestartAllCoreThreads();
	}

	public static void main(String[] args) {
		CategoryLog.accessLogger.info(Arrays.toString(args));

		String lineTime = null;

		CategoryLog.accessLogger.info("系统当前时间：" + DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));

		int afterMinute = -10;
		if (!(args.length == 1 || args.length == 0)) {
			CategoryLog.accessLogger.error("参数不符合规范");
			System.exit(0);
		}
		// 通过传参 确定读取时间
		if (args.length >= 1) {
			afterMinute = Integer.valueOf(args[0]);
		}
		try {

			// 当前系统前10分钟的时间 格式：yyyy-MM-dd HH:mm
			lineTime = DateUtil.getAfterMinuteDateTime(afterMinute, DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE);
			CategoryLog.accessLogger.info("系统前10分钟时间：" + lineTime);
			mtMessage(afterMinute, lineTime);
			mrMessage(afterMinute, lineTime);

			// 下发条数
			int mtNumber = 0;
			// 状态报告条数
			int mrNumber = 0;
			// 成功条数
			int successSendNumber = 0;
			// 失败条数
			int failureNumber = 0;
			// 延迟在xx秒以上的条数
			int delayNumber = 0;
			// 获取账号的延迟警告值
			int timeElapsed = 0;

			// 获取所有监控账号该时间段的提交信息和状态报告返回信息
			if(accountIDs!=null) {
			if (accountIDs.length > 0 && mtAccountMessageMap.size() > 0 ){

				for (String accountID : accountIDs) {
                    
					//监控账号该时间段有提交信息和状态报告信息
					if (mtAccountMessageMap.get(accountID) != null && (mrAccountMessageMap.get(accountID) != null)) {

						mtNumber = mtAccountMessageMap.get(accountID).getMtNumber();
						mrNumber = mrAccountMessageMap.get(accountID).getMrNumber();
						successSendNumber = mrAccountMessageMap.get(accountID).getSuccessSendNumber();
						failureNumber = mrAccountMessageMap.get(accountID).getFailureNumber();
						delayNumber = mrAccountMessageMap.get(accountID).getDelayNumber();

						CategoryLog.accessLogger.info(
								new StringBuilder().append("数据为:").append("accountID={}").append("{}mtNumber={}")
										.append("{}mrNumber={}").append("{}successSendNumber={}")
										.append("{}failureNumber={}").append("{}delayNumber={}").toString(),
								accountID, FixedConstant.LOG_SEPARATOR, mtNumber, FixedConstant.LOG_SEPARATOR, mrNumber,
								FixedConstant.LOG_SEPARATOR, successSendNumber, FixedConstant.LOG_SEPARATOR,
								failureNumber, FixedConstant.LOG_SEPARATOR, delayNumber);

						// 获取账号成功率警告值
						int successRatesAlarm = ResourceManager.getInstance().getIntValue(accountID + ".successRates");
						// 获取账号延迟率警告值
						int delayRatesAlarm = ResourceManager.getInstance().getIntValue(accountID + ".delayRates");
						//获取账号提交阈值
						int mtThreshold = ResourceManager.getInstance().getIntValue(accountID + ".mtThreshold");

						// 得到账号报告率
						int reportRates = Integer.valueOf(accuracys(mrNumber, mtNumber));

						// 得到账号成功率
						int successRates = Integer.valueOf(accuracys(successSendNumber, mtNumber));

						// 得到账号延迟率
						int delayRates = Integer.valueOf(accuracys(delayNumber, mtNumber));

						CategoryLog.accessLogger.info(
								new StringBuilder().append("数据为:").append("accountID={}").append("{}reportRates={}%")
										.append("{}successRates={}%").append("{}delayRates={}%").toString(),
								accountID, FixedConstant.LOG_SEPARATOR, reportRates, FixedConstant.LOG_SEPARATOR,
								successRates, FixedConstant.LOG_SEPARATOR, delayRates);
						//10分钟内提交条数大于提交阈值
						CategoryLog.accessLogger.info("提交条数:"+mtNumber+",阈值:"+mtThreshold);
							if (mtNumber>mtThreshold && successRates < successRatesAlarm) {

								AlarmUtil
										.process(new AlarmMessage(AlarmMessage.AlarmKey.AccountSuccessRate,
												new StringBuilder(accountID).append("的成功率为").append(successRates)
														.append("%").toString()));
							}

							if (mtNumber>mtThreshold && delayRates > delayRatesAlarm) {

								timeElapsed = ResourceManager.getInstance().getIntValue(accountID + ".timeElapsed");
								AlarmUtil
										.process(new AlarmMessage(AlarmMessage.AlarmKey.AccountDelayRate,
												new StringBuilder(accountID).append("延迟超过").append(timeElapsed)
														.append("秒的比列为").append(delayRates).append("%").toString()));
							}


					//监控账号该时间段有提交信息,无状态报告信息	
					} else if (mtAccountMessageMap.get(accountID) != null
							&& mrAccountMessageMap.get(accountID) == null) {
						mtNumber = mtAccountMessageMap.get(accountID).getMtNumber();
						//获取账号提交阈值
						int mtThreshold = ResourceManager.getInstance().getIntValue(accountID + ".mtThreshold");
						if(mtNumber>mtThreshold) {
							AlarmUtil
									.process(new AlarmMessage(AlarmMessage.AlarmKey.AccountSuccessRate,
											new StringBuilder(accountID).append(lineTime).append("提交条数为：").append(mtNumber)
													.append("该时间段无状态报告返回").toString()));
						}
					}
				}
				
			} 
			}

		} catch (Exception e) {
			CategoryLog.accessLogger.error(e.getMessage(), e);
		}
		CategoryLog.accessLogger.info("程序正常退出");
		System.exit(0);

	}

	public static void mtMessage(int afterMinute, String lineTime) {
		long startTime = System.currentTimeMillis();
		try {
			// 日志文件路径
			String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
					.append(LogPathConstant.getAccessBusinessFilePathPart(FixedConstant.RouteLable.MT.name()))
					.toString();

			// 读取的文件名,前10分钟的文件名
			String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MT.name()))
					.append(DateUtil.getAfterMinuteDateTime(afterMinute, DateUtil.DATE_FORMAT_COMPACT_HOUR)).toString();

			CategoryLog.accessLogger.info("读取当前文件路径={},文件名={},生成时间={}", filePath, fileName, lineTime);

			Vector<Future<Integer>> calls = new Vector<Future<Integer>>();

			int fileNumber = 0;

			if (StringUtils.isNotEmpty(filePath) && StringUtils.isNotEmpty(fileName) && enterpriseFlagSet.size() > 0) {
				for (String enterpriseFlag : enterpriseFlagSet) {

					// 根据企业标识去获取文件
					File[] files = findFile(filePath, fileName, enterpriseFlag.toLowerCase());
					if (files.length > 0) {
						File file = files[0];
						fileNumber++;

						// 一个文件用一个线程去处理
						Set<String> acccountSet = enterpriseFlagAccountMap.get(enterpriseFlag);
						AccountAlarm.MTWorker worker = new AccountAlarm().new MTWorker(file, lineTime, acccountSet);
						Future<Integer> call = threadPoolExecutor.submit(worker);
						calls.add(call);
					}
				}
			}

			// 循环结束 所有子线程处理完毕
			int rowNumber = 0;
			for (Future<Integer> call : calls) {
				try {
					rowNumber += call.get();
				} catch (Exception e) {
					CategoryLog.accessLogger.error(e.getMessage(), e);
				}
			}
			CategoryLog.accessLogger.info("{},处理文件数量:{},处理数据行数:{},耗时:{}", lineTime, fileNumber, rowNumber,
					(System.currentTimeMillis() - startTime));

		} catch (Exception e) {
			CategoryLog.accessLogger.error(e.getMessage(), e);
		}
		CategoryLog.accessLogger.info("获取mt文件数据结束");
	}

	public static void mrMessage(int afterMinute, String lineTime) {
		long startTime = System.currentTimeMillis();
		try {
			// 日志文件路径
			String filePath = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
					.append(LogPathConstant.getAccessBusinessFilePathPart(FixedConstant.RouteLable.MR.name()))
					.toString();

			// 读取的文件名
			String fileName = new StringBuilder(LogPathConstant.getFileNamePrefix(FixedConstant.RouteLable.MR.name()))
					.append(DateUtil.getAfterMinuteDateTime(afterMinute, DateUtil.DATE_FORMAT_COMPACT_HOUR)).toString();

			CategoryLog.accessLogger.info("读取当前文件路径={},文件名={},生成时间={}", filePath, fileName, lineTime);

			Vector<Future<Integer>> calls = new Vector<Future<Integer>>();

			int fileNumber = 0;

			// 得到yyyyMMddHH格式获取文件
			if (StringUtils.isNotEmpty(filePath) && StringUtils.isNotEmpty(fileName) && enterpriseFlagSet.size() > 0) {
				for (String enterpriseFlag : enterpriseFlagSet) {

					// 根据企业标识去获取文件
					File[] files = findFile(filePath, fileName, enterpriseFlag.toLowerCase());// toLowerCase()
					if (files.length > 0) {
						File file = files[0];

						// 一个文件用一个线程去处理
						fileNumber++;
						Set<String> acccountSet = enterpriseFlagAccountMap.get(enterpriseFlag);
						AccountAlarm.MRWorker worker = new AccountAlarm().new MRWorker(file, lineTime, acccountSet);
						Future<Integer> call = threadPoolExecutor.submit(worker);
						calls.add(call);
					}
				}
			}

			// 循环结束 所有子线程处理完毕
			int rowNumber = 0;
			for (Future<Integer> call : calls) {
				try {
					rowNumber += call.get();
				} catch (Exception e) {
					CategoryLog.accessLogger.error(e.getMessage(), e);
				}
			}
			CategoryLog.accessLogger.info("{},处理文件数量:{},处理数据行数:{},耗时:{}", lineTime, fileNumber, rowNumber,
					(System.currentTimeMillis() - startTime));

		} catch (Exception e) {
			CategoryLog.accessLogger.error(e.getMessage(), e);
		}
		CategoryLog.accessLogger.info("获取mr文件数据结束");

	}

	class MTWorker implements Callable<Integer> {
		private File file;
		private String lineTime;
		private Set<String> acccountSet;

		public MTWorker(File file, String lineTime, Set<String> acccountSet) {
			this.file = file;
			this.lineTime = lineTime;
			this.acccountSet = acccountSet;

		}

		@Override
		public Integer call() throws Exception {
			long startTime = System.currentTimeMillis();
			int mtNumber = 0;

			// 得到文件内符合的数据
			Map<String, AccountMessage> map = getAccessBusinessMtData(file, lineTime, acccountSet);
			for (String accountID : map.keySet()) {
				mtAccountMessageMap.put(accountID, map.get(accountID));
				mtNumber += map.get(accountID).getMtNumber();

			}
			CategoryLog.accessLogger.info("文件:{},时间段:{},处理数据{}条,耗时:{}", file.getName(), lineTime, mtNumber,
					(System.currentTimeMillis() - startTime));

			return mtNumber;
		}

		/**
		 * @param file
		 * @param lineTime
		 * @return 获取mt文件中10分钟下发的数据 比如10-19 ,30-39
		 * @throws Exception
		 */
		public Map<String, AccountMessage> getAccessBusinessMtData(File file, String lineTime, Set<String> acccountSet)
				throws Exception {
			LineIterator lines = null;
			String line = null;
			String[] array = null;
			boolean error = false;
			String error_line = null;
			String error_file = null;

			lineTime = lineTime.substring(0, 15);

			Map<String, AccountMessage> accountMessageMap = new HashMap<String, AccountMessage>();
			try {
				lines = FileUtils.lineIterator(file, "utf-8");
				while (lines.hasNext()) {
					line = lines.next();

					// 时间过滤
					if (!line.startsWith(lineTime)) {
						continue;
					}
					// 拆分及字段不全过滤
					array = line.split(FixedConstant.LOG_SEPARATOR);

					if (array.length < ACCESS_BUSINESS_MT_MESSAGE_LOG_LENGTH) {
						error = true;
						error_line = line;
						error_file = file.getAbsolutePath();
						continue;
					}

					if (acccountSet.contains(array[1])) {

						AccountMessage accountMessage = accountMessageMap.get(array[1]);
						if (accountMessage == null) {
							accountMessage = new AccountMessage();
							accountMessageMap.put(array[1], accountMessage);
						}
						accountMessage.setMtNumber(accountMessage.getMtNumber() + Integer.valueOf(array[18]));
					}
				}

			} catch (Exception e) {
				CategoryLog.accessLogger.error(e.getMessage(), e);
				throw e;
			}
			if (error) {
				CategoryLog.accessLogger.warn("错误文件={},错误行数={}", error_file, error_line);
			}
			return accountMessageMap;
		}
	}

	class MRWorker implements Callable<Integer> {

		private File file;
		private String lineTime;
		private Set<String> acccountSet;

		public MRWorker(File file, String lineTime, Set<String> acccountSet) {
			this.file = file;
			this.lineTime = lineTime;
			this.acccountSet = acccountSet;

		}

		@Override
		public Integer call() throws Exception {
			long startTime = System.currentTimeMillis();
			int mrNumber = 0;
			// 得到文件内符合的数据
			Map<String, AccountMessage> accountMessageMap = getAccessBusinessMRData(file, lineTime, acccountSet);
			for (String accountID : accountMessageMap.keySet()) {
				mrAccountMessageMap.put(accountID, accountMessageMap.get(accountID));
				mrNumber += accountMessageMap.get(accountID).getMrNumber();
			}
			CategoryLog.accessLogger.info("文件:{},时间段:{},处理数据{}条,耗时:{}", file.getName(), lineTime, mrNumber,
					(System.currentTimeMillis() - startTime));

			return mrNumber;

		}

		/**
		 * @param file
		 * @param lineTime
		 * @return 获取mr文件10分钟之内下发的数据
		 * @throws Exception
		 */
		public Map<String, AccountMessage> getAccessBusinessMRData(File file, String lineTime, Set<String> acccountSet)
				throws Exception {
			LineIterator lines = null;
			String line = null;
			String[] array = null;
			boolean error = false;
			String error_line = null;
			String error_file = null;

			lineTime = lineTime.substring(0, 15);
			Map<String, AccountMessage> accountMessageMap = new HashMap<String, AccountMessage>();
			// 获取账号的延迟时间
			int timeElapsed = 0;

			try {
				lines = FileUtils.lineIterator(file, "utf-8");
				while (lines.hasNext()) {
					line = lines.next();
					// 时间过滤
					if (!line.startsWith(lineTime)) {
						continue;
					}

					array = line.split(FixedConstant.LOG_SEPARATOR);

					if (array.length < ACCESS_BUSINESS_MR_MESSAGE_LOG_LENGTH) {
						error = true;
						error_line = line;
						error_file = file.getAbsolutePath();
						continue;
					}

					// 获取提交时间为前10分钟的状态报告
					if (array[4].startsWith(lineTime)) {

						if (acccountSet.contains(array[1])) {
							AccountMessage accountMessage = accountMessageMap.get(array[1]);
							if (accountMessage == null) {
								accountMessage = new AccountMessage();
								accountMessageMap.put(array[1], accountMessage);

							}
							accountMessage.setMrNumber(accountMessage.getMrNumber() + 1);
							if ("0".equals(array[24])) {

								// 成功条数
								accountMessage.setSuccessSendNumber(accountMessage.getSuccessSendNumber() + 1);
							} else {
								// 失败条数
								accountMessage.setFailureNumber(accountMessage.getFailureNumber() + 1);
							}
							timeElapsed = ResourceManager.getInstance().getIntValue(array[1] + ".timeElapsed");

							if (Integer.valueOf(array[28]) > timeElapsed) {
								// 延迟条数
								accountMessage.setDelayNumber(accountMessage.getDelayNumber() + 1);
							}
						}

					}
				}

			} catch (Exception e) {
				CategoryLog.accessLogger.error(e.getMessage(), e);
				throw e;
			}
			if (error) {
				CategoryLog.accessLogger.warn("错误文件={},错误行数={}", error_file, error_line);
			}
			return accountMessageMap;
		}

	}

	public static String accuracys(double num, double total) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		// 可以设置精确几位小数
		df.setMaximumFractionDigits(0);
		// 模式 例如四舍五入
		df.setRoundingMode(RoundingMode.HALF_UP);
		double accuracy_num = num / total * 100;
		return df.format(accuracy_num);
	}

	public static File[] findFile(String filePath, final String filename, final String enterpriseFlag) {
		FileFilter ff = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String s = pathname.getName();
				if (s.startsWith(filename) && s.contains(enterpriseFlag)) {
					return true;
				}
				return false;
			}
		};
		File file = new File(filePath);
		File[] files = file.listFiles(ff);
		if (files == null) {
			return new File[0];
		}
		return files;
	}
}
