package com.covid19.vaccination.services.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.covid19.vaccination.services.repository.AlertsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestCallService {

	@Autowired
	RestTemplate restTemplate;

	@Value("${vaccination.url}")
	private String url;

	@Autowired
	EmailService emailService;

	static String currentDate;

	static ObjectMapper mapper = new ObjectMapper();

	static {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		currentDate = formatter.format(date);

		mapper.writerWithDefaultPrettyPrinter();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@Autowired
	AlertsRepository repository;

	public void checkVaccineAvailability() {
		List<AlertRequestDto> alerts = repository.findAll();
		alerts.parallelStream().forEach(alert -> {
			String apiUrl = urlBuilder(alert.getDistrictId());
			HttpHeaders headers = getHeaders();
			String requestData = StringUtils.EMPTY;

			HttpEntity entity = new HttpEntity<>(requestData, headers);

			log.info("Get Vaccination Information call with headers: {} " + entity);

			// RestTemplate omiRestTemplate = new RestTemplate();
			ResponseEntity<AlertsResponse> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity,
					AlertsResponse.class);
			try {
				log.info("Response Received: {}", new ObjectMapper().writeValueAsString(response.getBody()));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			processReponse(response.getBody(), alert);
		});
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

	private String urlBuilder(int districtId) {
		StringBuilder sb = new StringBuilder();
		sb.append(url).append("district_id=").append(districtId).append("&date=").append(currentDate);
		String url = sb.toString();
		log.info("URL: {}", url);
		return url;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private String getEmailResponse(List<Centers> centers, String state, String city, AlertRequestDto alert) {
		Map<String, Integer> frequencyMap = new HashMap<>();
		centers.stream().forEach(center -> center.getSessions().stream().forEach(states -> {
			if (frequencyMap.containsKey(states.getDate())) {
				frequencyMap.put(states.getDate(), frequencyMap.get(states.getDate()) + states.getAvailable_capacity());
			} else {
				frequencyMap.put(states.getDate(), states.getAvailable_capacity());
			}
		}));
		StringBuilder sb = new StringBuilder();
		sb.append("Hi ").append(alert.getName());
		sb.append("\n\nTotal Vaccination Slots available for ").append(state).append(", ").append(city).append(" - ")
				.append(String.valueOf(frequencyMap.values().stream().reduce(0, (a, b) -> a + b))).append("\n");

		frequencyMap.entrySet().stream().filter(ele -> ele.getValue() != 0).forEach(
				ele -> sb.append("\nVaccination Slot: ").append(ele.getKey()).append(" : ").append(ele.getValue()));

		return sb.toString();
	}
}
