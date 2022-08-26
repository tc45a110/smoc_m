package com.business.alarm.worker.job;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.business.alarm.worker.dao.RouteMessageDao;
import com.business.alarm.worker.util.MailStmpMessage;
import com.business.alarm.worker.util.SendMessage;

public  class RouteMessageMrJob implements Job {
	
	@Override
	public void execute(JobExecutionContext arg0)  {
		//告警值
		int routeMrNumber = ResourceManager.getInstance().getIntValue("routeMrNumber");
		
		//获取route_message_mr_info状态报告临时表中的数据条数
		int line=RouteMessageDao.routeMessageMrInfoNumber();
		// 当状态报告临时表中的数据条数大于告警值时
		if (line >routeMrNumber) {
			// 发送邮件
			  String text =new StringBuffer().append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE)).
	            		append(",").append("route_message_mr_info中的数据为").append(line).append(",超过告警值,请及时处理。").toString();
			MailStmpMessage.sendHtmlMail(text);
			// 发送短信
			SendMessage.push(text);
		}
		
		
	}
}
