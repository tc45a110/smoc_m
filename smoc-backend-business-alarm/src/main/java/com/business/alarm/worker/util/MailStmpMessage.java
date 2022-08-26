package com.business.alarm.worker.util;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.base.common.manager.ResourceManager;
import com.sun.mail.util.MailSSLSocketFactory;

public class MailStmpMessage {	
	private static  Logger logger = LoggerFactory.getLogger(MailStmpMessage.class);
	/**
     * 网易企业163邮箱发送邮件    
     */
    public static void sendHtmlMail(String text) {
        try {

            String from = "监控预警";//发件人昵称展示
                
            String[] to=ResourceManager.getInstance().getValue("email.Accept").split(",");//接收邮箱      
            
           
            String subject = "监控预警";//邮件主题
            
         
            // 一、创建参数配置, 用于连接邮件服务器的参数配置
            Properties prop = new Properties();
            prop.setProperty("mail.smtp.auth", "true"); // 需要经过授权，
            prop.setProperty("mail.smtp.port", "465");// 加密端口(ssl)  //mail.smtp.socketFactory.port
            prop.setProperty("mail.smtp.timeout","10000"); 
          

            MailSSLSocketFactory sf = new MailSSLSocketFactory();// SSL加密
            sf.setTrustAllHosts(true); // 设置信任所有的主机         
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);

            //二、创建一封邮件
            JavaMailSenderImpl javaMailSend = new JavaMailSenderImpl();

            // 3.1. 创建邮件对象
            MimeMessage message = javaMailSend.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

            // 3.2. From: 发件人
            String nick = MimeUtility.encodeText(from);//设置昵称
            messageHelper.setFrom(new InternetAddress(nick + " <" +ResourceManager.getInstance().getValue("email.Username") + ">"));// 邮件发送者

            // 3.3. To: 收件人（可以增加多个收件人）
            messageHelper.setTo(to); 

            

            // 3.5. Subject: 邮件主题
            messageHelper.setSubject(subject); 

            // 3.6. 邮件内容
            messageHelper.setText(text, true); 

                 
            // 3.8. 设置邮件服务器登录信息
            javaMailSend.setHost("smtp.ym.163.com");//163企业邮箱smtp 
            javaMailSend.setUsername(ResourceManager.getInstance().getValue("email.Username"));//企业邮箱
            javaMailSend.setPassword(ResourceManager.getInstance().getValue("email.Password"));//企业邮箱密码
            javaMailSend.setJavaMailProperties(prop);

          
          
            // 六、 发送邮件,
            javaMailSend.send(message);
            logger.info("邮件发送成功");
          
          
        } catch (Exception e) {      	
        	logger.error(e.getMessage(), e);		
        }
    }


}
