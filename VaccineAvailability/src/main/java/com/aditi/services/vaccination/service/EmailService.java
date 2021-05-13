package com.aditi.services.vaccination.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private JavaMailSenderImpl emailSender;

	@Autowired
	Environment environment;

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(environment.getProperty("uname"));
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		if (environment.containsProperty("uname")) {
			emailSender.setHost(environment.getProperty("host"));
			emailSender.setPort(Integer.parseInt(environment.getProperty("port")));
			emailSender.setUsername(environment.getProperty("uname"));
			emailSender.setPassword(environment.getProperty("passcode"));
			emailSender.send(message);
			log.info("Email Sent to: {}", to);
			return;
		}
		log.warn("Credentials not available, please update the credentials using /register-mail-details/uname/passcode");
	}

}
