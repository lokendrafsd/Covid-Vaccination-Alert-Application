package com.covid19.vaccination.services.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.covid19.vaccination.services.model.AlertRequestDto;
import com.covid19.vaccination.services.repository.AlertsRepository;
import com.covid19.vaccination.services.utils.MessageGenerator;

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
			// TODO: handle exception
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
