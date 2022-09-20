package com.business.statistics.message.alarm;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.constant.LogPathConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.AccountInfoManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.vo.*;
import com.business.statistics.message.dao.AlarmDao;
import com.business.statistics.util.AlarmUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.concurrent.*;

public class AccountBusinessAlarm {


    // 接入业务层mt日志参数数量
    private static final int ACCESS_BUSINESS_MT_MESSAGE_LOG_LENGTH = ResourceManager.getInstance()
            .getIntValue("access.business.mt.message.log.length");
    // 接入业务层mr日志参数数量
    private static final int ACCESS_BUSINESS_MR_MESSAGE_LOG_LENGTH = ResourceManager.getInstance()
            .getIntValue("access.business.mr.message.log.length");

    // 统计时间区间
    private static final int STATISTICS_TIME_INTERVAL = ResourceManager.getInstance()
            .getIntValue("statistics.time.interval");

    // 统计从N分钟之前
    private static final int STATISTICS_BEFORE_TIME = ResourceManager.getInstance()
            .getIntValue("statistics.before.time");

    // 正常次数
    private static int NORMAL_NUMBER = ResourceManager.getInstance()
            .getIntValue("normal.number");

    // mt日志文件路径
    private static final String MT_FILE_PATH = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
            .append(LogPathConstant.getAccessBusinessFilePathPart(FixedConstant.RouteLable.MT.name()))
            .toString();

    // mt日志文件路径
    private static final String MR_FILE_PATH = new StringBuilder(LogPathConstant.LOG_BASE_PATH)
            .append(LogPathConstant.getAccessBusinessFilePathPart(FixedConstant.RouteLable.MR.name()))
            .toString();

    // 成功率监控账号
    private static Map<String, AccountSuccessRateAlarmConfiguration> successRateAlarmMap;
    // 延迟率监控账号
    private static Map<String, AccountDelayRateAlarmConfiguration> delayRateAlarmMap;
    // 成功率监控账号统计数据
    private static Map<String, SuccessRateMessageStatistics> successRateStatisticsMap = new HashMap<>();
    // 延迟率监控账号统计数据
    private static Map<String, DelayRateMessageStatistics> delayRateStatisticsMap = new HashMap<>();
    // 当次统计时间
    private static Date statisticsDate = new Date();

    //企业标识
    private static Set<String> enterpriseFlagSet = new HashSet<>();
    //企业标识和该企业下的监控的账号
    private static Map<String, Set<String>> enterpriseFlagAccountMap = new HashMap<>();

    // 线程池核心线程数占CPU核数的倍数
    private static int WORKER_MULTIPLE = ResourceManager.getInstance().getIntValue("worker.multiple");

    // 线程池
    private static ThreadPoolExecutor threadPoolExecutor = null;

    static {
        // 为0设置默认值
        if (WORKER_MULTIPLE == 0) {
            WORKER_MULTIPLE = 1;
        }
        if (NORMAL_NUMBER == 0) {
            NORMAL_NUMBER = 3;
        }
        threadPoolExecutor = new ThreadPoolExecutor(
                FixedConstant.CPU_NUMBER * WORKER_MULTIPLE, FixedConstant.CPU_NUMBER * WORKER_MULTIPLE * 2, 50,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPoolExecutor.prestartAllCoreThreads();
    }

    public static void main(String[] args) {
        CategoryLog.accessLogger.info(Arrays.toString(args));
        CategoryLog.accessLogger.info("系统当前时间：" + DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
        String statisticsTime = null;
        if (!(args.length == 1 || args.length == 0)) {
            CategoryLog.accessLogger.error("参数不符合规范");
            exit();
        }
        // 通过传参 确定读取时间
        if (args.length >= 1) {
            statisticsTime = args[0];
        }
        //设置统计的当前时间
        if (StringUtils.isNotEmpty(statisticsTime)) {
            statisticsDate = DateUtil.parseDate(statisticsTime, DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE);
        }
        try {
            CategoryLog.alarmLogger.info(statisticsDate.toString());
            Set<String> statisticsAccountSet = load();
            if (statisticsAccountSet.size() == 0) {
                CategoryLog.alarmLogger.info("本次加载未发现需要监控成功率或延迟率的账号");
                exit();
            }
            for (String accountID : statisticsAccountSet) {
                String enterpriseFlag = AccountInfoManager.getInstance().getEnterpriseFlag(accountID);
                if (StringUtils.isNotEmpty(enterpriseFlag)) {
                    enterpriseFlagSet.add(enterpriseFlag);
                    Set<String> accountSet = enterpriseFlagAccountMap.get(enterpriseFlag);
                    if (accountSet == null) {
                        accountSet = new HashSet<String>();
                        enterpriseFlagAccountMap.put(enterpriseFlag, accountSet);
                    }
                    accountSet.add(accountID);
                }
            }

            Map<String, Set<String>> mtFilterParams = getMtFilterParams(statisticsDate, STATISTICS_TIME_INTERVAL, STATISTICS_BEFORE_TIME);
            Map<String, Set<String>> mrFilterParams = getMrFilterParams(statisticsDate, STATISTICS_TIME_INTERVAL, STATISTICS_BEFORE_TIME);
            CategoryLog.alarmLogger.info("mtFilterParams:{}", mtFilterParams.toString());
            CategoryLog.alarmLogger.info("mrFilterParams:{}", mrFilterParams.toString());

            mtMessage(mtFilterParams.get("lines"), mtFilterParams.get("fileNames"));
            mrMessage(mrFilterParams.get("lines"), mrFilterParams.get("fileNames"), mtFilterParams.get("lines"));

            successRateAlarmProcess();
            delayRateAlarmProcess();
            CategoryLog.alarmLogger.info("本次成功率告警加载的数据为:{}", successRateAlarmMap.toString());
            CategoryLog.alarmLogger.info("本次延迟率告警加载的数据为:{}", delayRateStatisticsMap.toString());
        } catch (Exception e) {
            CategoryLog.accessLogger.error(e.getMessage(), e);
        }
        exit();
    }

    private static void delayRateAlarmProcess() {
        for (String accountID : delayRateAlarmMap.keySet()) {
            AccountDelayRateAlarmConfiguration configuration = delayRateAlarmMap.get(accountID);
            DelayRateMessageStatistics statistics = delayRateStatisticsMap.get(accountID);
            if (statistics != null) {
                int statisticsNumber = statistics.getStatisticsNumber();
                // 统计数量达到阈值
                if (configuration.getStatisticsNumber() < statisticsNumber) {
                    //计算延迟率
                    int delayNumber = statistics.getDelayNumber();
                    double delayRate = (double) delayNumber / (double) statisticsNumber;
                    statistics.setDelayRate(delayRate);
                    // 延迟率达到阈值
                    if (configuration.getDelayRateThreshold() < delayRate) {
                        configuration.setAlreadyEvaluateNumber(configuration.getAlreadyEvaluateNumber() + 1);
                        List<DelayRateMessageStatistics> statisticsList = configuration.getStatisticsList();
                        if (statisticsList == null) {
                            statisticsList = new ArrayList<>();
                            configuration.setStatisticsList(statisticsList);
                        }
                        statisticsList.add(statistics);
                        configuration.setLastEvaluateDate(statisticsDate);
                        // 检查该账号是否已发送告警
                        String normalNumber = CacheBaseService.getAccountNormalAlarmNumberFromMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountDelayRate.name());
                        if (StringUtils.isNotEmpty(normalNumber)) {
                            // 正常次数重置为0
                            CategoryLog.alarmLogger.info("账号:{}延迟率达到阈值,重新统计恢复正常次数", accountID);
                            CacheBaseService.saveAccountNormalAlarmNumberToMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountDelayRate.name(), "0");
                            continue;
                        }

                        // 是否达到评测告警阈值
                        if (configuration.getAlreadyEvaluateNumber() == configuration.getEvaluateNumber()) {
                            // 发送告警 删除缓存
                            CategoryLog.alarmLogger.info("账号:{},评测次数已达到告警阈值{},发送告警", accountID, configuration.getEvaluateNumber());
                            CacheBaseService.deleteAccountDelayRateAlarmConfigurationToMiddlewareCache(accountID);
                            sendDelayRateAlarm(configuration);
                            // 保存恢复次数缓存 0
                            CacheBaseService.saveAccountNormalAlarmNumberToMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountDelayRate.name(), "0");
                        } else {
                            // 存入缓存
                            CategoryLog.alarmLogger.info("账号:{},当前评测延迟率低于阈值,已评测次数:{},剩余评测次数:{}", accountID, configuration.getAlreadyEvaluateNumber(), configuration.getEvaluateNumber()-configuration.getAlreadyEvaluateNumber());
                            CacheBaseService.saveAccountDelayRateAlarmConfigurationToMiddlewareCache(configuration);
                            continue;
                        }
                    } else {
                        //成功率达到正常值 检查该账号是否已发送告警
                        String normalNumber = CacheBaseService.getAccountNormalAlarmNumberFromMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountDelayRate.name());
                        if (StringUtils.isNotEmpty(normalNumber)) {
                            //已发送告警
                            int number = Integer.parseInt(normalNumber) + 1;
                            if (number >= NORMAL_NUMBER) {
                                // 删除缓存
                                CategoryLog.alarmLogger.info("账号:{}延迟率回复正常,删除缓存,恢复告警", accountID);
                                CacheBaseService.deleteAccountNormalAlarmNumberFromMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountDelayRate.name());
                            } else {
                                CategoryLog.alarmLogger.info("账号:{},延迟率连续恢复正常次数为{}", accountID, number);
                                CacheBaseService.saveAccountNormalAlarmNumberToMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountDelayRate.name(), "" + number);
                            }
                            continue;
                        }
                    }
                }
            }
            if (CacheBaseService.getAccountNormalAlarmNumberFromMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountDelayRate.name()) != null) {
                CacheBaseService.saveAccountNormalAlarmNumberToMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountDelayRate.name(), "0");
            }
            if (CacheBaseService.getAccountDelayRateAlarmConfigurationToMiddlewareCache(accountID) != null) {
                CacheBaseService.deleteAccountDelayRateAlarmConfigurationToMiddlewareCache(configuration.getAccountID());
            }
        }
        CategoryLog.alarmLogger.info(delayRateAlarmMap.toString());
    }

    private static void successRateAlarmProcess() {
        for (String accountID : successRateAlarmMap.keySet()) {
            AccountSuccessRateAlarmConfiguration configuration = successRateAlarmMap.get(accountID);
            SuccessRateMessageStatistics statistics = successRateStatisticsMap.get(accountID);
            if (statistics != null) {
                int statisticsSendNumber = statistics.getStatisticsSendNumber();
                // 统计的下发条数达到阈值
                if (statisticsSendNumber > configuration.getStatisticsSendNumberThreshold()) {
                    //计算成功率
                    int successNumber = statistics.getSuccessNumber();
                    double successRate = (double) successNumber / (double) statisticsSendNumber;
                    statistics.setSuccessRate(successRate);
                    // 统计的成功率低于阈值
                    if (successRate < configuration.getSuccessRateThreshold()) {
                        configuration.setAlreadyEvaluateNumber(configuration.getAlreadyEvaluateNumber() + 1);
                        List<SuccessRateMessageStatistics> statisticsList = configuration.getStatisticsList();
                        if (statisticsList == null) {
                            statisticsList = new ArrayList<>();
                            configuration.setStatisticsList(statisticsList);
                        }
                        statisticsList.add(statistics);
                        configuration.setLastEvaluateDate(statisticsDate);

                        // 检查该账号是否已发送告警
                        String normalNumber = CacheBaseService.getAccountNormalAlarmNumberFromMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountSuccessRate.name());
                        if (StringUtils.isNotEmpty(normalNumber)) {
                            // 正常次数重置为0
                            CategoryLog.alarmLogger.info("账号:{}成功率低于阈值,重新统计恢复正常次数", accountID);
                            CacheBaseService.saveAccountNormalAlarmNumberToMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountSuccessRate.name(), "0");
                            continue;
                        }

                        // 是否达到评测次数告警阈值
                        if (configuration.getAlreadyEvaluateNumber() == configuration.getEvaluateNumber()) {
                            // 发送告警 删除缓存对象
                            CategoryLog.alarmLogger.info("账号:{},评测次数已达到告警阈值{},发送告警", accountID, configuration.getEvaluateNumber());
                            CacheBaseService.deleteAccountSuccessRateAlarmConfigurationToMiddlewareCache(accountID);
                            sendSuccessRateAlarm(configuration);
                            // 保存恢复次数缓存 0
                            CacheBaseService.saveAccountNormalAlarmNumberToMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountSuccessRate.name(), "0");
                        } else {
                            // 存入缓存
                            CacheBaseService.saveAccountSuccessRateAlarmConfigurationToMiddlewareCache(configuration);
                            CategoryLog.alarmLogger.info("账号:{},当前评测成功率低于阈值,已评测次数:{},剩余评测次数:{}", accountID, configuration.getAlreadyEvaluateNumber(), configuration.getEvaluateNumber()-configuration.getAlreadyEvaluateNumber());
                        }
                        continue;
                    } else {
                        //成功率达到正常值 检查该账号是否已发送告警
                        String normalNumber = CacheBaseService.getAccountNormalAlarmNumberFromMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountSuccessRate.name());
                        if (StringUtils.isNotEmpty(normalNumber)) {
                            //已发送告警
                            int number = Integer.parseInt(normalNumber) + 1;
                            if (number >= NORMAL_NUMBER) {
                                // 删除缓存
                                CategoryLog.alarmLogger.info("账号:{},成功率回复正常,删除缓存,恢复告警", accountID);
                                CacheBaseService.deleteAccountNormalAlarmNumberFromMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountSuccessRate.name());
                            } else {
                                CategoryLog.alarmLogger.info("账号:{},成功率连续恢复正常次数为{}", accountID, number);
                                CacheBaseService.saveAccountNormalAlarmNumberToMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountSuccessRate.name(), number + "");
                            }
                            continue;
                        }
                    }
                }

            }
            if (CacheBaseService.getAccountNormalAlarmNumberFromMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountSuccessRate.name()) != null) {
                CategoryLog.alarmLogger.info("账号:{},没达到评测条件或者成功率正常,重新评测", accountID);
                CacheBaseService.saveAccountNormalAlarmNumberToMiddlewareCache(accountID, AlarmMessage.AlarmKey.AccountSuccessRate.name(), "0");
            }
            // 没达到评测条件和成功率正常 删除缓存中账号成功率告警对象
            if (CacheBaseService.getAccountSuccessRateAlarmConfigurationToMiddlewareCache(accountID) != null) {
                CacheBaseService.deleteAccountSuccessRateAlarmConfigurationToMiddlewareCache(configuration.getAccountID());
            }
        }
        CategoryLog.alarmLogger.info(successRateStatisticsMap.toString());
    }

    private static Set<String> load() {
        // 加载监控信息
        successRateAlarmMap = AlarmDao.loadAccountSuccessRateAlarmConfiguration(statisticsDate);
        delayRateAlarmMap = AlarmDao.loadAccountDelayRateAlarmConfiguration(statisticsDate);
        // 需要监控统计的账号
        Set<String> statisticsAccountSet = new HashSet<>();
        if (successRateAlarmMap != null && successRateAlarmMap.size() > 0) {
            statisticsAccountSet.addAll(successRateAlarmMap.keySet());
        }
        if (delayRateAlarmMap != null && delayRateAlarmMap.size() > 0) {
            statisticsAccountSet.addAll(delayRateAlarmMap.keySet());
        }
        return statisticsAccountSet;
    }

    private static void sendSuccessRateAlarm(AccountSuccessRateAlarmConfiguration configuration) {
        StringBuilder logContent = new StringBuilder("账号:").append(configuration.getAccountID());
        logContent.append(",连续").append(configuration.getAlreadyEvaluateNumber()).append("次评测成功率低于")
                .append(String.format("%.2f", configuration.getSuccessRateThreshold() * 100d)).append("%");
        logContent.append(",统计的结果分别为:");
        StringBuilder sendContent = new StringBuilder().append(logContent.toString());
        List<SuccessRateMessageStatistics> statisticsList = configuration.getStatisticsList();
        for (int i = 0; i < statisticsList.size(); i++) {
            SuccessRateMessageStatistics statistics = statisticsList.get(i);
            if (i != 0) {
                logContent.append("、");
                sendContent.append("、");
            }
            sendContent.append(String.format("%.2f", statistics.getSuccessRate() * 100d)).append("%");
            logContent.append("下发数量=").append(statistics.getStatisticsSendNumber()).append(FixedConstant.SPLICER);
            logContent.append("成功数量=").append(statistics.getSuccessNumber()).append(FixedConstant.SPLICER);
            logContent.append("成功率=").append(String.format("%.2f", statistics.getSuccessRate() * 100d)).append("%");
        }
        CategoryLog.alarmLogger.info("告警内容:{}", logContent.toString());
        AlarmUtil.process(new AlarmMessage(AlarmMessage.AlarmKey.AccountSuccessRate, sendContent.toString()));
    }

    private static void sendDelayRateAlarm(AccountDelayRateAlarmConfiguration configuration) {
        StringBuilder logContent = new StringBuilder("账号:").append(configuration.getAccountID());
        logContent.append(",连续").append(configuration.getAlreadyEvaluateNumber())
                .append("次评测延迟率高于").append(String.format("%.2f", configuration.getDelayRateThreshold() * 100d)).append("%");
        logContent.append(",统计的结果分别为:");
        StringBuilder sendContent = new StringBuilder().append(logContent.toString());
        List<DelayRateMessageStatistics> statisticsList = configuration.getStatisticsList();
        for (int i = 0; i < statisticsList.size(); i++) {
            DelayRateMessageStatistics statistics = statisticsList.get(i);
            if (i != 0) {
                logContent.append("、");
                sendContent.append("、");
            }
            sendContent.append(String.format("%.2f", statistics.getDelayRate() * 100d)).append("%");
            logContent.append("统计数量=").append(statistics.getStatisticsNumber()).append(FixedConstant.SPLICER);
            logContent.append("延迟时间超过").append(configuration.getDelayTimeThreshold()).append("秒的延迟数量=").append(statistics.getDelayNumber()).append(FixedConstant.SPLICER);
            logContent.append("延迟率=").append(String.format("%.2f", statistics.getDelayRate() * 100d)).append("%");
        }
        CategoryLog.alarmLogger.info("延迟率告警明细:{}", logContent.toString());
        AlarmUtil.process(new AlarmMessage(AlarmMessage.AlarmKey.AccountDelayRate, sendContent.toString()));
    }

    private static void exit() {
        CategoryLog.accessLogger.info("程序正常退出");
        System.exit(0);
    }

    private static void mtMessage(Set<String> lineTimes, Set<String> fileNames) {
        long startTime = System.currentTimeMillis();
        try {
            CategoryLog.accessLogger.info("读取当前文件路径={},文件名={},生成时间={}", MT_FILE_PATH, fileNames, lineTimes);
            Vector<Future<Integer>> calls = new Vector<Future<Integer>>();
            int fileNumber = 0;
            if (StringUtils.isNotEmpty(MT_FILE_PATH) && CollectionUtils.isNotEmpty(fileNames) && enterpriseFlagSet.size() > 0) {
                for (String enterpriseFlag : enterpriseFlagSet) {
                    // 根据企业标识去获取文件
                    File[] files = findFile(MT_FILE_PATH, fileNames, enterpriseFlag.toLowerCase());
                    if (files.length > 0) {
                        for (int i = 0; i < files.length; i++) {
                            File file = files[0];
                            fileNumber++;
                            // 一个文件用一个线程去处理
                            Set<String> accountSet = enterpriseFlagAccountMap.get(enterpriseFlag);
                            MTWorker worker = new AccountBusinessAlarm().new MTWorker(file, lineTimes, accountSet);
                            Future<Integer> call = threadPoolExecutor.submit(worker);
                            calls.add(call);
                        }
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
            CategoryLog.accessLogger.info("{},处理文件数量:{},处理数据行数:{},耗时:{}", lineTimes, fileNumber, rowNumber,
                    (System.currentTimeMillis() - startTime));

        } catch (Exception e) {
            CategoryLog.accessLogger.error(e.getMessage(), e);
        }
        CategoryLog.accessLogger.info("获取mt文件数据结束");
    }

    public static void mrMessage(Set<String> lineTimes, Set<String> fileNames, Set<String> statisticsTimes) {
        long startTime = System.currentTimeMillis();
        try {
            CategoryLog.accessLogger.info("读取当前文件路径={},文件名={},生成时间={}", MR_FILE_PATH, fileNames, lineTimes);
            Vector<Future<Integer>> calls = new Vector<Future<Integer>>();
            int fileNumber = 0;

            // 得到yyyyMMddHH格式获取文件
            if (StringUtils.isNotEmpty(MR_FILE_PATH) && CollectionUtils.isNotEmpty(fileNames) && enterpriseFlagSet.size() > 0) {
                for (String enterpriseFlag : enterpriseFlagSet) {
                    // 根据企业标识去获取文件
                    File[] files = findFile(MR_FILE_PATH, fileNames, enterpriseFlag.toLowerCase());// toLowerCase()
                    if (files.length > 0) {
                        for (int i = 0; i < files.length; i++) {
                            File file = files[0];
                            // 一个文件用一个线程去处理
                            fileNumber++;
                            Set<String> accountSet = enterpriseFlagAccountMap.get(enterpriseFlag);
                            MRWorker worker = new AccountBusinessAlarm().new MRWorker(file, lineTimes, accountSet, statisticsTimes);
                            Future<Integer> call = threadPoolExecutor.submit(worker);
                            calls.add(call);
                        }
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
            CategoryLog.accessLogger.info("{},处理文件数量:{},处理数据行数:{},耗时:{}", lineTimes, fileNumber, rowNumber,
                    (System.currentTimeMillis() - startTime));

        } catch (Exception e) {
            CategoryLog.accessLogger.error(e.getMessage(), e);
        }
        CategoryLog.accessLogger.info("获取mr文件数据结束");

    }

    public static Map<String, Set<String>> getMtFilterParams(Date date, int statisticsTimeInterval, int minute) {
        Map<String, Set<String>> map = new HashMap<>();
        Set<String> lines = new HashSet<>();
        Set<String> fileNames = new HashSet<>();
        for (int i = minute; i < statisticsTimeInterval + minute; i++) {
            String lineTime = DateFormatUtils.format(DateUtils.addMinutes(date, -i), DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE);
            String fileName = LogPathConstant.LOG_FILENAME_PREFIX_MT + DateFormatUtils.format(DateUtils.addMinutes(date, -i), DateUtil.DATE_FORMAT_COMPACT_HOUR);
            fileNames.add(fileName);
            lines.add(lineTime);
        }
        map.put("lines", lines);
        map.put("fileNames", fileNames);
        return map;
    }

    public static Map<String, Set<String>> getMrFilterParams(Date date, int statisticsTimeInterval, int minute) {
        Map<String, Set<String>> map = new HashMap<>();
        Set<String> lines = new HashSet<>();
        Set<String> fileNames = new HashSet<>();
        for (int i = 0; i < statisticsTimeInterval + minute; i++) {
            String lineTime = DateFormatUtils.format(DateUtils.addMinutes(date, -i), DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE);
            String fileName = LogPathConstant.LOG_FILENAME_PREFIX_MR + DateFormatUtils.format(DateUtils.addMinutes(date, -i), DateUtil.DATE_FORMAT_COMPACT_HOUR);
            fileNames.add(fileName);
            lines.add(lineTime);
        }
        map.put("lines", lines);
        map.put("fileNames", fileNames);
        return map;
    }


    class MTWorker implements Callable<Integer> {
        private File file;
        private Set<String> lineTimes;
        private Set<String> accountSet;

        public MTWorker(File file, Set<String> lineTimes, Set<String> accountSet) {
            this.file = file;
            this.lineTimes = lineTimes;
            this.accountSet = accountSet;

        }

        @Override
        public Integer call() throws Exception {
            long startTime = System.currentTimeMillis();
            // int mtNumber = 0;

            // 得到文件内符合的数据
            int mtNumber = getAccessBusinessMtData(file, lineTimes, accountSet);
            CategoryLog.accessLogger.info("文件:{},时间段:{},处理数据{}条,耗时:{}", file.getName(), lineTimes, mtNumber,
                    (System.currentTimeMillis() - startTime));

            return mtNumber;
        }

        /**
         * @param file
         * @param lineTimes
         * @return 获取mt文件中10分钟下发的数据 比如10-19 ,30-39
         * @throws Exception
         */
        public int getAccessBusinessMtData(File file, Set<String> lineTimes, Set<String> accountSet)
                throws Exception {
            String line = null;
            String[] array = null;
            boolean error = false;
            String error_line = null;
            String error_file = null;

            int statisticsNumber = 0;
            try {
                LineIterator lines = FileUtils.lineIterator(file, "utf-8");
                while (lines.hasNext()) {
                    line = lines.next();
                    // 时间过滤
                    if (!lineTimes.contains(line.substring(0, 16))) {
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

                    String accountID = array[1];
                    if (successRateAlarmMap.containsKey(accountID)) {
                        statisticsNumber++;
                        //该账号需要统计成功率
                        SuccessRateMessageStatistics statistics = successRateStatisticsMap.get(accountID);
                        if (statistics == null) {
                            statistics = new SuccessRateMessageStatistics();
                            statistics.setStatisticsTime(DateUtil.format(statisticsDate, DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE));
                            successRateStatisticsMap.put(accountID, statistics);
                        }
                        statistics.setStatisticsSendNumber(statistics.getStatisticsSendNumber() + Integer.parseInt(array[18]));
                    }
                }
            } catch (Exception e) {
                CategoryLog.accessLogger.error(e.getMessage(), e);
                throw e;
            }
            if (error) {
                CategoryLog.accessLogger.warn("错误文件={},错误行数={}", error_file, error_line);
            }
            return statisticsNumber;
        }
    }

    class MRWorker implements Callable<Integer> {

        private File file;
        private Set<String> lineTimes;
        private Set<String> accountSet;
        private Set<String> statisticsTimes;

        public MRWorker(File file, Set<String> lineTimes, Set<String> accountSet, Set<String> statisticsTimes) {
            this.file = file;
            this.lineTimes = lineTimes;
            this.accountSet = accountSet;
            this.statisticsTimes = statisticsTimes;
        }

        @Override
        public Integer call() throws Exception {
            long startTime = System.currentTimeMillis();
            // 得到文件内符合的数据
            int mrNumber = getAccessBusinessMRData(file, lineTimes, accountSet, statisticsTimes);
            CategoryLog.accessLogger.info("文件:{},时间段:{},处理数据{}条,耗时:{}", file.getName(), lineTimes, mrNumber,
                    (System.currentTimeMillis() - startTime));

            return mrNumber;

        }

        public int getAccessBusinessMRData(File file, Set<String> lineTimes, Set<String> accountSet, Set<String> statisticsTimes)
                throws Exception {
            LineIterator lines = null;
            String line = null;
            String[] array = null;
            boolean error = false;
            String error_line = null;
            String error_file = null;

            // 获取账号的延迟时间
            int statisticsNumber = 0;
            try {
                lines = FileUtils.lineIterator(file, "utf-8");
                while (lines.hasNext()) {
                    line = lines.next();
                    // 时间过滤
                    if (!lineTimes.contains(line.substring(0, 16))) {
                        continue;
                    }
                    array = line.split(FixedConstant.LOG_SEPARATOR);
                    if (array.length < ACCESS_BUSINESS_MR_MESSAGE_LOG_LENGTH) {
                        error = true;
                        error_line = line;
                        error_file = file.getAbsolutePath();
                        continue;
                    }

                    //提交时间为统计mt的状态报告
                    String accountID = array[1];
                    if (accountSet.contains(accountID) && statisticsTimes.contains(array[4].substring(0, 16))) {
                        statisticsNumber++;
                        // 统计成功率
                        if (successRateAlarmMap.containsKey(accountID)) {
                            SuccessRateMessageStatistics statistics = successRateStatisticsMap.get(accountID);
                            if (statistics == null) {
                                statistics = new SuccessRateMessageStatistics();
                                statistics.setStatisticsTime(DateUtil.format(statisticsDate, DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE));
                                successRateStatisticsMap.put(accountID, statistics);
                            }
                            statistics.setStatisticsReportNumber(statistics.getStatisticsReportNumber() + 1);
                            if (InsideStatusCodeConstant.SUCCESS_CODE.equals(array[24])) {
                                // 成功条数
                                statistics.setSuccessNumber(statistics.getSuccessNumber() + 1);
                            } else {
                                // 失败条数
                                statistics.setFailNumber(statistics.getFailNumber() + 1);
                            }
                        }
                        //统计延迟率
                        if (delayRateAlarmMap.containsKey(accountID)) {
                            DelayRateMessageStatistics statistics = delayRateStatisticsMap.get(accountID);
                            if (statistics == null) {
                                statistics = new DelayRateMessageStatistics();
                                statistics.setStatisticsTime(DateUtil.format(statisticsDate, DateUtil.DATE_FORMAT_COMPACT_STANDARD_MINUTE));
                                delayRateStatisticsMap.put(accountID, statistics);
                            }
                            statistics.setStatisticsNumber(statistics.getStatisticsNumber() + 1);
                            if (Integer.parseInt(array[28]) > delayRateAlarmMap.get(accountID).getDelayTimeThreshold()) {
                                // 延迟条数
                                statistics.setDelayNumber(statistics.getDelayNumber() + 1);
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
            return statisticsNumber;
        }

    }

    public static File[] findFile(String filePath, Set<String> fileNames, final String enterpriseFlag) {
        FileFilter ff = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                String s = pathname.getName();
                for (String fileName : fileNames) {
                    if (s.startsWith(fileName) && s.contains(enterpriseFlag)) {
                        return true;
                    }
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
