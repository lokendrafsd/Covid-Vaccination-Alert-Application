package com.covid19.vaccination.services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.covid19.vaccination.services.model.AlertRequestDto;
import com.covid19.vaccination.services.service.RegisterAlertsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/vaccination")
@CrossOrigin("*")
public class RegisterAlertsController {

	@Autowired
	RegisterAlertsService service;
	
	@GetMapping("/status")
	public String getStatus()  {
		return "Service is up";
	}

	@PostMapping("/register-alert")
	public ResponseEntity registerAlert(@RequestBody AlertRequestDto alertInfo) throws JsonProcessingException {
		log.info("Received Request for Alert activation: {}", new ObjectMapper().writeValueAsString(alertInfo));
		service.registerAlert(alertInfo);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/deregister-alert/{email}")
	public ResponseEntity deleteEmployee(@PathVariable(value = "email") String email) {
		log.info("Received Request for Alert de-activation for: {}", email);
		service.deregisterAlert(email);
		return ResponseEntity.ok().build();
	}

}
