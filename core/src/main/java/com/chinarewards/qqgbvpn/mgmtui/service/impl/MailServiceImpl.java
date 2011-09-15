package com.chinarewards.qqgbvpn.mgmtui.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
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
        mailMessage.setSubject(subject);  
        mailMessage.setSentDate(new Date());  
        
        Multipart mp = new MimeMultipart();  
        MimeBodyPart mbp = new MimeBodyPart();  
        mbp.setContent(messageText, "text/html;charset=UTF-8");
        mp.addBodyPart(mbp);
        mailMessage.setContent(mp);
        Transport.send(mailMessage);  
	}

}
