package com.business.alarm.worker.job;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.base.common.util.DateUtil;
import com.business.alarm.worker.dao.RouteMessageDao;
import com.business.alarm.worker.util.MailStmpMessage;
import com.business.alarm.worker.util.SendMessage;


public class ChannelRunStatusJob  implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
	
		List<String>  list=RouteMessageDao.channelRunStatus();
		if(list.size()>0){
			for(int i=0;i<list.size();i++) {
			 String text =new StringBuffer().append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE)).
			              append(",通道").append(list.get(i)).append("的运行状态异常,请及时处理").toString();
			// 发送邮件
			MailStmpMessage.sendHtmlMail(text);
			SendMessage.push(text);			
		}
		}		
	}
}


