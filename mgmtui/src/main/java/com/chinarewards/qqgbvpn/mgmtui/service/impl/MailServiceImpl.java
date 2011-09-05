package com.chinarewards.qqgbvpn.mgmtui.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.chinarewards.qqgbvpn.mgmtui.service.MailService;
import com.google.inject.Inject;

public class MailServiceImpl implements MailService {
	
	@Inject
	protected Configuration configuration;

	public void sendMail(String[] toAdds, String[] cc, String subject,
			String content, File attachment) throws MessagingException,
			UnsupportedEncodingException, javax.mail.MessagingException {
		JavaMailSenderImpl javaMail = new JavaMailSenderImpl();
		javaMail.setHost(configuration.getString("smtp.server"));
		javaMail.setUsername(configuration.getString("smtp.username"));
		javaMail.setPassword(configuration.getString("smtp.password"));
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth","true");
		
		javaMail.setJavaMailProperties(props);
		
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
	}

}
