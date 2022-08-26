package com.business.alarm.worker.job;
import java.util.Map;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.base.common.util.DateUtil;
import com.business.alarm.worker.dao.RouteMessageDao;
import com.business.alarm.worker.util.MailStmpMessage;
import com.business.alarm.worker.util.SendMessage;

public class AlarmMessageJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		// 获取告警信息表alarm_message_info中的信息
		Map<String, String> alarmMap = RouteMessageDao.alarmMessage();
		if (alarmMap.size() > 0) {
			for (String key : alarmMap.keySet()) {
				if("ChannelWorker".equals(key)||"ExternalBlacklistFilterWorker".equals(key)) {
				String text = new StringBuffer().append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE)).append(",缓存队列")
						.append(key).append("中的数据为").append(alarmMap.get(key)).append(",超过告警值,请及时处理。").toString();

				// 发送邮件
				MailStmpMessage.sendHtmlMail(text);
				// 发送短信
				
				SendMessage.push(text);
			}else {
				String text = new StringBuffer().append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE)).append(",")
						.append(key).append(":").append(alarmMap.get(key)).toString();
				
				// 发送邮件
				MailStmpMessage.sendHtmlMail(text);
				// 发送短信			
				SendMessage.push(text);
				
			}
			}
		}

	}

}
