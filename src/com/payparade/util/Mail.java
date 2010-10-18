package com.payparade.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
	static Logger logger_ = Logger.getLogger(Mail.class.getName());
	String hostname_ = null;
	String defaultSender_ = null;
	Properties props_ = null;

	public void init(String hostName, String defaultSender) {
		hostname_ = hostName;
		defaultSender_ = defaultSender;

		props_ = new Properties();
		props_.put("mail.smtp.host", hostname_);

	}

	public void send(String addressee, String subject, String content) {
		send(addressee, defaultSender_, subject, content);
	}

	public void send(String addressee, String from, String subject,
			String content) {
		if (addressee != null) {
			InternetAddress[] addressTo = new InternetAddress[1];
			try {
				addressTo[0] = new InternetAddress(addressee);
			} catch (MessagingException me) {
				logger_.error("aig mail error - " + me.getMessage() + "\n");
			}
			send(addressTo, from, subject, content);
		}
	}

	public void send(String[] addressee, String from, String subject,
			String content) {
		if (addressee != null) {
			InternetAddress[] addressTo = new InternetAddress[addressee.length];
			try {
				for (int i = 0; i < addressee.length; i++) {
					addressTo[i] = new InternetAddress(addressee[i]);
				}
			} catch (MessagingException me) {
				logger_.error("aig mail error - " + me.getMessage() + "\n");
			}
			send(addressTo, from, subject, content);
		}
	}

	public void send(InternetAddress[] addressee, String from, String subject,
			String content) {
		String resultMsg = "";
		// String from = request.getParameter("from");
		// String to = request.getParameter("to");
		// String subject = request.getParameter("subject");
		// String content = request.getParameter("message");

		if (props_ == null) {
			// logger_.error("Please init Mail first", false);
			logger_.error("Please init Mail first");
		} else {
			Session mailSession = Session.getDefaultInstance(props_, null);
			mailSession.setDebug(true);

			try {
				Transport transport = mailSession.getTransport("smtp");

				// Setup message
				MimeMessage message = new MimeMessage(mailSession);
				// From address
				message.setFrom(new InternetAddress(from));
				// To address

				message.setRecipients(Message.RecipientType.TO, addressee);
				// Subject

				message.setSubject(subject);
				// Content
				message.setText(content);

				// Send message
				transport.connect();
				transport.send(message);
			} catch (NoSuchProviderException e) {
				logger_.error(e.getStackTrace()[0] + " - " + e.getMessage());
			} catch (AddressException e) {
				logger_.error(e.getStackTrace()[0] + " - " + e.getMessage());
			} catch (MessagingException e) {
				logger_.error(e.getStackTrace()[0] + " - " + e.getMessage());
			}
		}

	}

}
