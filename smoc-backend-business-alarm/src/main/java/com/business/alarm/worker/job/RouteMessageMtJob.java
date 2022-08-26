package com.business.alarm.worker.job;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.business.alarm.worker.dao.RouteMessageDao;
import com.business.alarm.worker.util.MailStmpMessage;
import com.business.alarm.worker.util.SendMessage;

public class RouteMessageMtJob implements Job {
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//告警值
		int routeMtNumber = ResourceManager.getInstance().getIntValue("routeMtNumber");
		
		//获取route_message_mt_info下行数据临时表中的数据条数
		int number = RouteMessageDao.routeMessageMtInfoNumber();
		// 当下行数据临时表中的数据条数大于告警值时，
		if (number>routeMtNumber) {
			String text =new StringBuffer().append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE)).
            		append(",").append("route_message_mt_info中的数据为").append(number).append(",超过告警值,请及时处理。").toString();
			// 发送邮件
			MailStmpMessage.sendHtmlMail(text);
			// 发送短信
			SendMessage.push(text);
		}
		
	}
}
