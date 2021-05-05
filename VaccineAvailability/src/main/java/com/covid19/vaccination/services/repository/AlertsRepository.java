package com.covid19.vaccination.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.covid19.vaccination.services.model.AlertRequestDto;

@Repository
public interface AlertsRepository extends JpaRepository<AlertRequestDto, Long> {

}
