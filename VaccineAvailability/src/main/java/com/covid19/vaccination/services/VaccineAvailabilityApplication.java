package com.covid19.vaccination.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class VaccineAvailabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccineAvailabilityApplication.class, args);
	}

}
