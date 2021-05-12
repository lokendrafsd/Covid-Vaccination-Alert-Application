package com.aditi.services.vaccination.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.aditi.services.vaccination.model.AlertRequestDto;
import com.aditi.services.vaccination.repository.AlertsRepository;
import com.aditi.services.vaccination.utils.MessageGenerator;

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
			emailService.sendSimpleMessage(alertInfo.getEmailId(), subject,
					MessageGenerator.getRegisteredSuccessfullyMessage(alertInfo));
		} catch (Exception e) {
			log.error("Error while registering for alerts: {}, {}", e.getLocalizedMessage(), e);
			emailService.sendSimpleMessage("lokendrakumar.vk@gmail.com", "Application Error Alert - Unable to register",
					e.toString());
		}
	}

	public void deregisterAlert(String email) {
		List<AlertRequestDto> alertinfo = repository.findAllUsersBasedOnEmail(email);
		if (!CollectionUtils.isEmpty(alertinfo)) {
			alertinfo.stream().forEach(alert -> repository.deleteById(alert.getId()));
			emailService.sendSimpleMessage(email, "Vaccination Availability Alerts De-Registered Successfully, Thank You!",
					MessageGenerator.getDeRegisteredSuccessfullyMessage());
			log.info("Alert Deactivated for Email: {}", email);
		} else {
			log.info("No Record found with Email: {}", email);
		}

	}

}