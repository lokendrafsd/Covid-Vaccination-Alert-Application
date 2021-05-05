package com.covid19.vaccination.services.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Centers {

	private int center_id;

	private String name;
	
	private String address;
	
	private String state_name;
	
	private String district_name;
	
	private String block_name;
	
	private int pincode;
	
	private int lat;
	
	@JsonProperty(value = "long")
	private int longitude;

	private String from;

	private String to;

	private String fee_type;

	private List<Sessions> sessions;



	
}
