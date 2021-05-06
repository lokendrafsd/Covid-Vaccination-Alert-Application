package com.covid19.vaccination.services.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.covid19.vaccination.services.model.AlertRequestDto;

@Repository
public interface AlertsRepository extends JpaRepository<AlertRequestDto, Long> {

	@Query(value = "SELECT * FROM alerts_table u WHERE u.email_id = ?1", nativeQuery = true)
	List<AlertRequestDto> findAllUsersBasedOnEmail(String email);
}
