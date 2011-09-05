package com.chinarewards.qqgbvpn.mgmtui.service;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;


public interface MailService {

	public void sendMail(String[] toAdds, String[] cc, String subject,
			String content, File attachment) throws MessagingException,
			UnsupportedEncodingException, javax.mail.MessagingException;
}
