package com.covid19.vaccination.services.model;

import java.util.List;

import lombok.Data;

@Data
public class Sessions {
	
	private String session_id;
	private String date;
	private int available_capacity;
	private int min_age_limit;
	private String vaccine;
	private List<String> slots;

}
