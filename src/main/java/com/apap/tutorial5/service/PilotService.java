package com.apap.tutorial5.service;

import org.springframework.stereotype.Service;

import com.apap.tutorial5.model.PilotModel;

@Service
public interface PilotService {
	PilotModel getPilotDetailByLicenseNumber(String licenseNumber);
	
	void addPilot(PilotModel pilot);
	void deletePilot(PilotModel pilot);
	void updatePilot(String licenseNumber, String nama, int flyHour);
}
