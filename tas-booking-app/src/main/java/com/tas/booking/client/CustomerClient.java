package com.tas.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tas.booking.client.dto.CustomerDTO;

@FeignClient(name = "tas-customer-app")
public interface CustomerClient {
	@GetMapping("/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable("id") Long id);
}
