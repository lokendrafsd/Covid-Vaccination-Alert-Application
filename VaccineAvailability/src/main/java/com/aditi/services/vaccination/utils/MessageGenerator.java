package com.aditi.services.vaccination.utils;

import org.springframework.stereotype.Component;

import com.aditi.services.vaccination.model.AlertRequestDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageGenerator {

	private MessageGenerator() {
	}

	public static String getRegisteredSuccessfullyMessage(AlertRequestDto alertInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("Hi ").append(alertInfo.getName()).append(",").append("\n\n");
		sb.append(
				"You are successfully registered for Vaccination availability alerts for the mentioned state. Meanwhile, while we will keep looking for the available slots and notify you as soon as we find any, we request you to stay safe and follow all the precautionary measures, This time shall pass too. We appreciate your trust in us to notify you.");
		sb.append(Constants.MAIL_SIGNATURE);
		log.info("Registration Successful Message: {}", sb.toString());
		return sb.toString();
	}

	public static String getDeRegisteredSuccessfullyMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"Dear User,\n\nYou have successfully De-Registered for Vaccination availability alerts.We request you to stay safe and follow all the precautionary measures, This time shall pass too. We appreciate your trust in us to notify you.");
		sb.append(Constants.MAIL_SIGNATURE);
		log.info("De-Registration Successful Message: {}", sb.toString());
		return sb.toString();
	}
}
