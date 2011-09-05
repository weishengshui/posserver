package com.chinarewards.qqgbvpn.mgmtui.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.chinarewards.qqgbvpn.mgmtui.service.MailService;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

public class MailServiceImpl implements MailService {

	public void sendMail(String[] toAdds, String[] cc, String subject,
			String content, File attachment) throws MessagingException,
			UnsupportedEncodingException, javax.mail.MessagingException {
		JavaMailSenderImpl javaMail = new JavaMailSenderImpl();
		javaMail.setHost("smtp.163.com");
		javaMail.setUsername("lin7hao");
		javaMail.setPassword("1986115w");
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth","true");
		
		javaMail.setJavaMailProperties(props);
		
		MimeMessage message = javaMail.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true,"GBK");
		helper.setFrom("lin7hao@163.com");
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
	}

}
