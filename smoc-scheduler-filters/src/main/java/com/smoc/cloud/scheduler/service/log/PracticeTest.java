package com.smoc.cloud.scheduler.service.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description:
 *
 * @author Hlingoes
 * @date 2020/5/22 23:45
 */
public class PracticeTest {
    private static Logger logger = LoggerFactory.getLogger(PracticeTest.class);
    private static Logger testLogger = LoggerUtils.getLogger(LogNameEnum.TEST, PracticeTest.class);

    public void loggerUtilsTest() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        /**
         *  <property scope="context" name="LOG_HOME" value="log"/>
         *  <property scope="context" name="LOG_NAME_PREFIX" value="common"/>
         */
        String oph = OptionHelper.substVars("${LOG_HOME}/${LOG_NAME_PREFIX}/test-log.log", context);// 在日志文件common-info.log中
        logger.info("logger默认配置的日志输出");// 在日志文件common-test-info.log中
        testLogger.info("testLogger#####{}####", oph);
        testLogger.info("testLogger看到这条信息就是info");// 在日志文件common-test-error.log中
        testLogger.error("testLogger看到这条信息就是error");
    }

}
