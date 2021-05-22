package com.aditi.services.vaccination.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.aditi.services.vaccination.model.AlertRequestDto;
import com.aditi.services.vaccination.model.AlertsResponse;
import com.aditi.services.vaccination.model.Centers;
import com.aditi.services.vaccination.model.FinalResponse;
import com.aditi.services.vaccination.model.Slots;
import com.aditi.services.vaccination.repository.AlertsRepository;
import com.aditi.services.vaccination.utils.Constants;
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
	
	@Value("${Send.Email.On.Error}")
	private String errorAlertsUser;

	@Autowired
	EmailService emailService;

	static String currentDate;

	static ObjectMapper mapper = new ObjectMapper();

	static int totalSl;

	static {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		currentDate = formatter.format(date);
		totalSl = 0;

		mapper.writerWithDefaultPrettyPrinter();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@Autowired
	AlertsRepository repository;

	public void checkVaccineAvailability() {
		try {
			List<AlertRequestDto> alerts = repository.findAll();
			alerts.parallelStream().forEach(alert -> processAlerts(alert));
		} catch (Exception e) {
			log.error("Error Occurred while making api call to cowin-app: ", e);
		}
	}

	/**
	 * @param alert
	 */
	@Async("threadPoolTaskExecutor")
	private void processAlerts(AlertRequestDto alert) {
		String apiUrl = urlBuilder(alert.getDistrictId());
		HttpHeaders headers = getHeaders();
		String requestData = StringUtils.EMPTY;

		HttpEntity entity = new HttpEntity<>(requestData, headers);

		log.info("Get Vaccination Information call with headers: {} ", entity);
		try {
			ResponseEntity<AlertsResponse> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity,
					AlertsResponse.class);
			log.info("Response Received with status: {}", response.getStatusCode());
			if (response.getStatusCodeValue() == 200 ) {
				processReponse(response.getBody(), alert);
			}
		} catch (Exception ex) {
			log.error("Error Occurred while making api call to cowin-app: ", ex);
			}

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
				log.info("Slots found, sending alerts");
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
		String tempUrl = sb.toString();
		log.info("URL: {}", tempUrl);
		return tempUrl;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language","en_US");
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		return headers;
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

		sb.append(Constants.MAIL_SIGNATURE);

		return sb.toString();
	}
}
