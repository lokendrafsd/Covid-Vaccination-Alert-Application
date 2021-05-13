package com.aditi.services.vaccination.controller;

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

import com.aditi.services.vaccination.model.AlertRequestDto;
import com.aditi.services.vaccination.service.RegisterAlertsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/vaccination")
@CrossOrigin(origins = "*")
public class RegisterAlertsController {

	@Autowired
	RegisterAlertsService service;

	@GetMapping("/status")
	public String getStatus() {
		return "Service is up";
	}

	@PostMapping("/register-alert")
	public ResponseEntity registerAlert(@RequestBody AlertRequestDto alertInfo) throws JsonProcessingException {
		log.info("Received Request for Alert activation: {}", new ObjectMapper().writeValueAsString(alertInfo));
		service.registerAlert(alertInfo);
		return ResponseEntity.ok().body("Successfully registered alerts");
	}

	@DeleteMapping("/deregister-alert/{email}")
	public ResponseEntity deRegisterAlert(@PathVariable(value = "email") String email) {
		log.info("Received Request for Alert de-activation for: {}", email);
		service.deregisterAlert(email);
		return ResponseEntity.ok().body("Successfully Deactivated from alerts");
	}

	@PostMapping("/register-mail-details/{uname}/{passcode}/{host}/{port}")
	public ResponseEntity registerMailDetails(@PathVariable("uname") String uname,
			@PathVariable("passcode") String passcode,@PathVariable String host,@PathVariable String port) {
		System.setProperty("uname", uname);
		System.setProperty("passcode", passcode);
		System.setProperty("host", host);
		System.setProperty("port", port);
		log.info("Credentials Set");
		return ResponseEntity.ok().body("Successfully Set the details");
	}

}
