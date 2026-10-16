package com.tas.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tas.booking.client.dto.TravelPackageDTO;

@FeignClient(name = "tas-travelPackage-app")
public interface TravelPackageClient {
	
	@GetMapping("/travel-packages/{id}")
    TravelPackageDTO getPackageById(@PathVariable("id") Long id);
}
