package com.chinarewards.qqgbvpn.mgmtui.service.impl;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.configuration.Configuration;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.core.mail.MailService;
import com.google.inject.Inject;

public class MailServiceImpl implements MailService {
	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	protected Configuration configuration;

	/*public void sendMail(String[] toAdds, String[] cc, String subject,
			String content, File attachment) throws MessagingException,
			UnsupportedEncodingException, javax.mail.MessagingException {
		
		// FIXME use JavaMail instead. http://www.rgagnon.com/javadetails/java-0538.html
		
		JavaMailSenderImpl javaMail = new JavaMailSenderImpl();
		
		log.debug("smtp.server={}", configuration.getString("smtp.server"));
		log.debug("smtp.username={}", configuration.getString("smtp.username"));
		
		javaMail.setHost(configuration.getString("smtp.server"));
		javaMail.setUsername(configuration.getString("smtp.username"));
		javaMail.setPassword(configuration.getString("smtp.password"));
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth","true");
		
		javaMail.setJavaMailProperties(props);
		
		// FIXME use UTF-8, it is 2011
		
		MimeMessage message = javaMail.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true,"GBK");
		helper.setFrom(configuration.getString("smtp.username"));
		helper.setTo(toAdds);
		if (cc != null && cc.length > 0) {
			helper.setCc(cc);
		}
		helper.setSubject(subject);
		helper.setText(content,true);
		
		if (attachment != null) {
			helper.addAttachment(new String(attachment.getName().getBytes("GBK"),"ISO-8859-1"),attachment);
		}
		
		javaMail.send(message);
	}*/
	
	public void sendMail(String[] toAdds, String[] cc, String subject,
			String tempPathAndName, String tempKey, Object[] params)
					throws AddressException, MessagingException {
		 // 获得邮件模板信息  
        ResourceBundle mailTemplateRb = ResourceBundle.getBundle(tempPathAndName);  
        MessageFormat formater = new MessageFormat("");  
        formater.applyPattern(mailTemplateRb.getString(tempKey));
        String messageText = formater.format(params);  
        // 设置邮件的传输协议信息  
        Properties transProp = System.getProperties();  
        // 邮件服务器地址  
        transProp.put("mail.smtp.host", configuration.getString("smtp.server"));  
        // 邮件传输协议中的接收协议：smtp  
        transProp.put("mail.transport.protocol", "smtp");  
        // 是否通过验证  
        transProp.put("mail.smtp.auth", "true");
        Session mailSession = Session.getDefaultInstance(transProp,new Authenticator(){  
            @Override  
            protected PasswordAuthentication getPasswordAuthentication() {  
                return new PasswordAuthentication(configuration.getString("smtp.username")
                		,configuration.getString("smtp.password"));  
            }  
        });
        Message mailMessage = new MimeMessage(mailSession);  
        mailMessage.setFrom(new InternetAddress(configuration.getString("smtp.username")));
        InternetAddress[] sendTo = new InternetAddress[toAdds.length];
        for (int i = 0; i < toAdds.length; i++) {
        	sendTo[i] = new InternetAddress(toAdds[i]);
        } 
        mailMessage.setRecipients(RecipientType.TO, sendTo);
        String subject_mail = "";
		try {
			subject_mail = MimeUtility.encodeText(subject, "UTF-8", "B");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        mailMessage.setSubject(subject_mail);
        mailMessage.setSentDate(new Date());  
        
        Multipart mp = new MimeMultipart();  
        MimeBodyPart mbp = new MimeBodyPart();  
        mbp.setContent(messageText, "text/html;charset=UTF-8");
        mp.addBodyPart(mbp);
        mailMessage.setContent(mp);
        Transport.send(mailMessage);  
	}
	
	public void sendMailByVelocity(String[] toAdds, String[] cc, String subject,
			String tempPath, String tempName, Map<String,Object> params)
					throws AddressException, MessagingException {
		String messageText;
		try {
			/* 首先创建一个模板引擎的实例，并予以初始化 */
			VelocityEngine engine = new VelocityEngine();
			Properties properties = new Properties();
			properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, tempPath);
			properties.setProperty(Velocity.ENCODING_DEFAULT, "utf-8");
			properties.setProperty(Velocity.INPUT_ENCODING, "utf-8");
			properties.setProperty(Velocity.OUTPUT_ENCODING, "utf-8");
			engine.init(properties);
			
			/* 接着，获得一个模板 */
			Template template = engine.getTemplate(tempName);
			
			/* 创建上下文，填充数据 */
			VelocityContext context = new VelocityContext();
			if (params != null) {
				for (Entry<String,Object> entry : params.entrySet()) {
					context.put(entry.getKey(), entry.getValue());
				}
			}
			
			/* 现在，把模板和数据合并，输出到StringWriter */
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			
			/* 显示结果 */
			//messageText = new String(writer.toString().getBytes("iso-8859-1"),"utf-8");
			messageText = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			messageText = "";
		}
		
        // 设置邮件的传输协议信息  
        Properties transProp = System.getProperties();  
        // 邮件服务器地址  
        transProp.put("mail.smtp.host", configuration.getString("smtp.server"));  
        // 邮件传输协议中的接收协议：smtp  
        transProp.put("mail.transport.protocol", "smtp");  
        // 是否通过验证  
        transProp.put("mail.smtp.auth", "true");
        Session mailSession = Session.getDefaultInstance(transProp,new Authenticator(){  
            @Override  
            protected PasswordAuthentication getPasswordAuthentication() {  
                return new PasswordAuthentication(configuration.getString("smtp.username")
                		,configuration.getString("smtp.password"));  
            }  
        });
        Message mailMessage = new MimeMessage(mailSession);  
        mailMessage.setFrom(new InternetAddress(configuration.getString("smtp.username")));
        InternetAddress[] sendTo = new InternetAddress[toAdds.length];
        for (int i = 0; i < toAdds.length; i++) {
        	sendTo[i] = new InternetAddress(toAdds[i]);
        } 
        mailMessage.setRecipients(RecipientType.TO, sendTo);
        String subject_mail = "";
		try {
			subject_mail = MimeUtility.encodeText(subject, "UTF-8", "B");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        mailMessage.setSubject(subject_mail);
        mailMessage.setSentDate(new Date());  
        
        Multipart mp = new MimeMultipart();  
        MimeBodyPart mbp = new MimeBodyPart();  
        mbp.setContent(messageText, "text/html;charset=UTF-8");
        mp.addBodyPart(mbp);
        mailMessage.setContent(mp);
        Transport.send(mailMessage);  
	}

}
