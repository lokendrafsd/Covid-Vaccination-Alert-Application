package com.aditi.services.vaccination.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.aditi.services.vaccination.model.MailCredential;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private JavaMailSenderImpl emailSender;
	
	private MailCredential mailCredential;	

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		if (mailCredential!= null && !mailCredential.getEmail().isEmpty()) {
			message.setFrom("NO_REPLY@aditiconsulting.com");
			emailSender.setHost(mailCredential.getHost());
			emailSender.setPort(mailCredential.getPort());
			emailSender.setUsername(mailCredential.getEmail());
			emailSender.setPassword(mailCredential.getPassword());
			emailSender.send(message);
			log.info("Email Sent to: {}", to);
			return;
		}
		log.warn("Credentials not available, please update the credentials using /register-mail-details/uname/passcode");
	}


	public void setMailCredential(MailCredential tempmailCredential) {
		mailCredential = tempmailCredential;
		
	}

}
