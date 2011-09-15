package com.chinarewards.qqgbvpn.core.mail;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface MailService {

	/**
	 * Send email.
	 * 
	 * @param toAdds
	 * @param cc
	 * @param subject
	 * @param content
	 * @param attachment
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws javax.mail.MessagingException
	 */
	/*public void sendMail(String[] toAdds, String[] cc, String subject,
			String content, File attachment) throws MessagingException,
			UnsupportedEncodingException, javax.mail.MessagingException;*/
	
	public void sendMail(String[] toAdds, String[] cc, String subject,
			String tempPathAndName, String tempKey, Object[] params)
					throws AddressException, MessagingException;
}
