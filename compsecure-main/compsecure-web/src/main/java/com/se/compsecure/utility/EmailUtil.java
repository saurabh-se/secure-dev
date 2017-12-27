package com.se.compsecure.utility;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

	public static void sendMail(String emailId, String name, String body) {

			final String username = "saurabh.asthana@secureyes.net";
			final String password = "$E@Saurabh@2017";

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.office365.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
					return new javax.mail.PasswordAuthentication(username, password);
				}
			  });

			try {

				Message message = new MimeMessage(session);
				
				message.setFrom(new InternetAddress("saurabh.asthana@secureyes.net"));
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailId));
				message.setRecipients(Message.RecipientType.CC,InternetAddress.parse("saurabh.asthana@secureyes.net"));
				message.setSubject("User Creation - CompSecure");
				message.setContent("Dear,<h3>" + name.toUpperCase() + "</h3> <br>\n Your user account has been created.\n"
					+ "Please check with the Admin for the password and click the link to change your password! \n" + body+"","text/html");

				Transport.send(message);

				System.out.println("Done");

			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
	}

}
