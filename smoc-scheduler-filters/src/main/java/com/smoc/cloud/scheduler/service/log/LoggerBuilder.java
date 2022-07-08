package com.smoc.cloud.scheduler.service.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * https://github.com/Hlingoes/file-message-server
 * https://www.cnblogs.com/leohe/p/12117183.html
 */
@Component
public class LoggerBuilder {
    private ConcurrentHashMap<String, Logger> container = new ConcurrentHashMap<>();

    public Logger getLogger(String name,Class<?> clazz) {
        Logger logger = container.get(name);
        if (logger != null) {
            return logger;
        }
        synchronized (LoggerBuilder.class) {
            logger = container.get(name);
            if (logger != null) {
                return logger;
            }
            logger = build(name,clazz);
            container.put(name, logger);
        }
        return logger;
    }

    private Logger build(String name,Class<?> clazz) {
        RollingFileAppender errorAppender = new AppenderFactory().createRollingFileAppender(name, Level.ERROR);
        RollingFileAppender infoAppender = new AppenderFactory().createRollingFileAppender(name, Level.INFO);
        ConsoleAppender consoleAppender = new AppenderFactory().createConsoleAppender();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(clazz + " [" + name + "]");
        //设置不向上级打印信息
        logger.setAdditive(false);
        logger.addAppender(errorAppender);
        logger.addAppender(infoAppender);
        logger.addAppender(consoleAppender);

        return logger;
    }

}