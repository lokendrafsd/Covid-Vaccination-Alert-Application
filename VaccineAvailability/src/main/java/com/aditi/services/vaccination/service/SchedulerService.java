package com.aditi.services.vaccination.service;

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
	
	@Scheduled(cron =  "*/30 * * * * *")
	public void start() {
		try{
			log.info("Executing Scheduler Service");
			service.checkVaccineAvailability();
		}catch(Exception e){
			log.error("Exeception while executing scheduler {}", e);
		}
	}
}
