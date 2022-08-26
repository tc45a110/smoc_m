package com.business.alarm.worker.job;
import java.util.Map;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.business.alarm.worker.dao.RouteMessageDao;
import com.business.alarm.worker.util.MailStmpMessage;
import com.business.alarm.worker.util.SendMessage;

public class RouteChannelMessageMtJob implements Job {
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//告警值
		int routeChannelMtNumber = ResourceManager.getInstance().getIntValue("routeChannelMtNumber");
		
		for (String tableName : RouteMessageDao.getTable()) {
			Map<String, Long> map = RouteMessageDao.routeChannelMessageMtInfoNumber(tableName);
			for (String table : map.keySet()) {
				// 当通道级下行数据临时表中的数据条数大于告警值时，
				if (map.get(table) >routeChannelMtNumber) {
					// 发送邮件
					  String text =new StringBuffer().append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE)).
			            		append(",").append(table).append("中的数据为:").append(map.get(table)).append(",超过告警值,请及时处理。").toString();
					MailStmpMessage.sendHtmlMail(text);
					// 发送短信
					SendMessage.push(text);
				}
			}
		}
		
		
	}

}
