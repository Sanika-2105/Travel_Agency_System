package com.tas.travelPackage.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tas.travelPackage.client.dto.AgencyDTO;

@FeignClient(name = "tas-agency-app")
public interface AgencyClient {
	@GetMapping("/agencies/{id}")
    AgencyDTO getAgencyById(@PathVariable("id") Long id);
}
