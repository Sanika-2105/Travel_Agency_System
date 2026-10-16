package com.tas.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tas.booking.client.dto.PaymentDTO;

@FeignClient(name = "tas-payment-app")
public interface PaymentClient {
	@PostMapping("/payments")
    PaymentDTO processPayment(@RequestBody PaymentDTO paymentRequest);
}
