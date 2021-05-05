package com.covid19.vaccination.services.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covid19.vaccination.services.model.AlertRequestDto;
import com.covid19.vaccination.services.repository.AlertsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegisterAlertsService {

	@Autowired
	AlertsRepository repository;

	@Autowired
	EmailService emailService;

	public void registerAlert(AlertRequestDto alertInfo) {
		String subject = "Vaccination Availability Alerts Registered Successfully, Thank You! "
				.concat(alertInfo.getName()).concat(", Please do Stay Safe.");
		try {
			repository.save(alertInfo);
			emailService.sendSimpleMessage(alertInfo.getEmailId(), subject, getMessage(alertInfo));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private String getMessage(AlertRequestDto alertInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("Hi ").append(alertInfo.getName()).append(",").append("\n\n");
		sb.append("You are successfully registered for Vaccination availability alerts for the mentioned state.");
		sb.append(
				" Meanwhile, while we will keep looking for the available slots and notify you as soon as we find any, ");
		sb.append("we request you to stay safe and follow all the precautionary measures, This time shall pass too.");
		sb.append(" We appreciate your trust in us to notify you.");
		sb.append(
				"\n\nNote: In case if you would want to de-register from the alerts, Please reply back to this mail with <STOP ALERTS> as a message");
		sb.append("\n\n").append("Thank You!").append("\nTeam that cares for India");
		log.info("Registration Successful Message: {}", sb.toString());
		return sb.toString();
	}

}
