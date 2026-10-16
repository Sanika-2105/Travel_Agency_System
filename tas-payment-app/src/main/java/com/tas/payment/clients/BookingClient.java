package com.tas.payment.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tas.payment.client.dto.BookingDTO;
import com.tas.payment.dto.PaymentDTO;

@FeignClient(name = "tas-booking-app")
public interface BookingClient {
	 @GetMapping("/bookings/{id}")
	 BookingDTO getBookingById(@PathVariable Long id);
	 
	 @PutMapping("/bookings/{id}/payment-status")
	    BookingDTO updateBookingStatusFromPayment(@PathVariable("id") Long id,
	                                              @RequestBody PaymentDTO paymentResponse);
}
