package com.business.alarm.worker;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class QuartzContainer {
	private static  Logger logger = LoggerFactory.getLogger(QuartzContainer.class);
	/**
	 * 读取配置文件并设置定时器 1、该方法加载一次即可 2、需要读取配置文件
	 * 
	 */
	public static void dispatcherTask(){
		try {
			// 创建工厂
			SchedulerFactory schedulerfactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerfactory.getScheduler();

			// 加载配置文件，并循环创建每一个定时任务
			Properties properties = new Properties();
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("resource.properties");
			properties.load(is);

			Enumeration<Object> enums = properties.keys();
			while (enums.hasMoreElements()) {
				// 获取符合规范的参数名称
				String key = (String) enums.nextElement(); // xxxx.job
				if (!key.endsWith("job")) {
					continue;
				}
				String cronKey = key.substring(0, key.indexOf("job")) + "cron"; // xxxx.cron
				String enable = key.substring(0, key.indexOf("job")) + "enable"; // xxxx.enable

				// 获取参数值
				String jobClassName = properties.getProperty(key); // 定时任务实现类
				String jobCronExp = properties.getProperty(cronKey); // 定时任务的cron表达式
				String enableVal = properties.getProperty(enable); // 定时任务开关

				if (!"true".equalsIgnoreCase(enableVal)) {
					continue;
				}

				Class clazz = Class.forName(jobClassName);
				// 指明job的名称，所在组的名称，以及绑定job类
				JobDetail job = JobBuilder.newJob(clazz)
						                  .withIdentity(jobClassName, jobClassName)
						                  .build();

				// 定义触发的条件
				Trigger trigger = TriggerBuilder.newTrigger()
						.withIdentity(jobClassName, jobClassName)
						.startNow()// 马上执行
						.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(Integer.valueOf(jobCronExp)))
						.build();

				// 把任务和触发器注册到任务调度中
				Date date = scheduler.scheduleJob(job, trigger);
				logger.info("注册成功,时间为:" + date);
			}

			// 启动任务
			scheduler.start();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
}
