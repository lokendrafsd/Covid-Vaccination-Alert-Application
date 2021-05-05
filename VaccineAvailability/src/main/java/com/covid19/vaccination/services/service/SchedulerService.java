package com.covid19.vaccination.services.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Service
public class SchedulerService {

	@Autowired
	RestCallService service; 
	
	@Scheduled(cron =  "*/60 * * * * *")
	public void start() {
		System.out.println("Scheduler Executing");
		service.checkVaccineAvailability();
	}
}
