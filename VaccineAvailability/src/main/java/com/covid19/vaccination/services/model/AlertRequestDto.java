package com.covid19.vaccination.services.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "AlertsTable")
public class AlertRequestDto {

	public AlertRequestDto(String name, int filterAge, String state, int districtId, String emailId) {
		super();
		this.name = name;
		this.filterAge = filterAge;
		this.state = state;
		this.districtId = districtId;
		this.emailId = emailId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name", nullable = true)
	private String name;

	@Column(name = "filterAge", nullable = false)
	private int filterAge;

	@Column(name = "state", nullable = true)
	private String state;

	@Column(name = "districtId", nullable = false)
	private int districtId;

	@Column(name = "emailId", nullable = true)
	private String emailId;
	
	@Column(name = "phone", nullable = true)
	private String phone;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFilterAge() {
		return filterAge;
	}

	public void setFilterAge(int filterAge) {
		this.filterAge = filterAge;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		return "AlertRequestDto [id=" + id + ", name=" + name + ", filterAge=" + filterAge + ", state=" + state
				+ ", districtId=" + districtId + ", emailId=" + emailId + ", phone=" + phone + "]";
	}

}
