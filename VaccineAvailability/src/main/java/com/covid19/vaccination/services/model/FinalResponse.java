package com.covid19.vaccination.services.model;

import java.util.List;

import lombok.Data;

@Data
public class FinalResponse {
	private String address;
	private String state_name;
	private String district_name;
	private List<Slots> vaccinationSlots;

}
