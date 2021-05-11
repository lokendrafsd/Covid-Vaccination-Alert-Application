package com.covid19.vaccination.services.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.covid19.vaccination.services.model.AlertRequestDto;
import com.covid19.vaccination.services.model.AlertsResponse;
import com.covid19.vaccination.services.model.Centers;
import com.covid19.vaccination.services.model.FinalResponse;
import com.covid19.vaccination.services.model.Slots;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@NoArgsConstructor
public class HelperService extends Thread {

	EmailService emailService = new EmailService();

	static String currentDate;

	static ObjectMapper mapper = new ObjectMapper();

	static int totalSl;

	private String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?";

	private AlertRequestDto alertRequestDto;

	RestTemplate restTemplate = new RestTemplate();

	static {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		currentDate = formatter.format(date);
		totalSl = 0;

		mapper.writerWithDefaultPrettyPrinter();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	public HelperService(AlertRequestDto alertRequestDto) {
		this.alertRequestDto = alertRequestDto;
	}

	public void run() {
		log.info("--- Running a thread");
		processAlert(alertRequestDto);
	}

	/**
	 * @param alert
	 */

	private void processAlert(AlertRequestDto alert) {
		String apiUrl = urlBuilder(alert.getDistrictId());
		HttpHeaders headers = getHeaders();
		String requestData = StringUtils.EMPTY;

		HttpEntity entity = new HttpEntity<>(requestData, headers);

		log.info("Get Vaccination Information call with headers: {} " + entity);

		ResponseEntity<AlertsResponse> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity,
				AlertsResponse.class);
		try {
			log.info("Response Received: {}", new ObjectMapper().writeValueAsString(response.getBody()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		processReponse(response.getBody(), alert);
	}

	private String urlBuilder(int districtId) {
		StringBuilder sb = new StringBuilder();
		sb.append(url).append("district_id=").append(districtId).append("&date=").append(currentDate);
		String url = sb.toString();
		log.info("URL: {}", url);
		return url;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private void processReponse(AlertsResponse response, AlertRequestDto alert) {
		if (!CollectionUtils.isEmpty(response.getCenters())) {
			List<Centers> centers = response.getCenters().stream()
					.filter(x -> !CollectionUtils.isEmpty(x.getSessions().stream()
							.filter(y -> y.getMin_age_limit() == alert.getFilterAge() && y.getAvailable_capacity() > 0)
							.collect(Collectors.toList())))
					.collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(centers)) {
				String state = centers.get(0).getState_name();
				String city = centers.get(0).getDistrict_name();
				String resp = getEmailResponse(centers, state, city, alert);

				log.info("Found a Vaccine Slot, Vaccination center Details are {}", resp);
				emailService.sendSimpleMessage(alert.getEmailId(), alert.getFilterAge()
						+ "+ Age Group: Vaccination Slot Available in: ".concat(state).concat(", ").concat(city), resp);
			} else {
				log.info("No Slots Found");
			}
		}
	}

	private String getEmailResponse(List<Centers> centers, String state, String city, AlertRequestDto alert) {
		totalSl = 0;
		String dl = "\n--------------------------------------------------- \n";
		List<FinalResponse> finalResponseList = new ArrayList<>();

		centers.stream().forEach(ele -> {
			FinalResponse response = new FinalResponse();
			List<Slots> slots = new ArrayList<>();
			response.setAddress(ele.getAddress());
			response.setDistrict_name(ele.getDistrict_name());
			response.setState_name(ele.getState_name());

			ele.getSessions().stream().forEach(x -> {
				Slots sl = new Slots();
				sl.setDate(x.getDate());
				sl.setVaccineSlots(x.getAvailable_capacity());
				totalSl += x.getAvailable_capacity();
				slots.add(sl);
			});
			response.setVaccinationSlots(slots);
			finalResponseList.add(response);
		});

		StringBuilder sb = new StringBuilder();
		sb.append("Hi ").append(alert.getName());
		sb.append("\n\nTotal Vaccination Slots available for ").append(state).append(", ").append(city).append(" - ")
				.append(String.valueOf(totalSl)).append("\n");
		sb.append("\nPlease visit https://www.cowin.gov.in/home to book your vaccination slots. \n");

		finalResponseList.stream().forEach(resp -> {
			sb.append(dl);
			sb.append("\nAddress: ").append(resp.getAddress());
			sb.append("\nState\t  : ").append(resp.getDistrict_name()).append(", ").append(resp.getState_name());

			resp.getVaccinationSlots().stream().forEach(slot -> {
				sb.append("\n   -Date  : ").append(slot.getDate());
				sb.append("\n   -Slots : ").append(slot.getVaccineSlots()).append("\n");
			});
		});

		sb.append("\n\n").append("Thank You!").append("\nAditi Family").append("\n~ Developed by Aditi Adhigam");
		return sb.toString();
	}

}
