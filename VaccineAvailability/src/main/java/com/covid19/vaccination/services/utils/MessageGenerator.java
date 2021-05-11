package com.covid19.vaccination.services.utils;

import org.springframework.stereotype.Component;

import com.covid19.vaccination.services.model.AlertRequestDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageGenerator {

	public static String getRegisteredSuccessfullyMessage(AlertRequestDto alertInfo) {
			StringBuilder sb = new StringBuilder();
			sb.append("Hi ").append(alertInfo.getName()).append(",").append("\n\n");
			sb.append("You are successfully registered for Vaccination availability alerts for the mentioned state.");
			sb.append(
					" Meanwhile, while we will keep looking for the available slots and notify you as soon as we find any, ");
			sb.append("we request you to stay safe and follow all the precautionary measures, This time shall pass too.");
			sb.append(" We appreciate your trust in us to notify you.");
			sb.append("\n\n").append("Thank You!").append("\nTeam that cares for You");
			log.info("Registration Successful Message: {}", sb.toString());
			return sb.toString();
		}

	public static String getDeRegisteredSuccessfullyMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Dear User,\n\n");
		sb.append("You have successfully De-Registered for Vaccination availability alerts.");
		sb.append("We request you to stay safe and follow all the precautionary measures, This time shall pass too.");
		sb.append(" We appreciate your trust in us to notify you.");
		sb.append("\n\n").append("Thank You!").append("\nTeam that cares for You");
		log.info("De-Registration Successful Message: {}", sb.toString());
		return sb.toString();
	}
}
