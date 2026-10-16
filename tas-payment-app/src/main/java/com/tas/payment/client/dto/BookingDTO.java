package com.tas.payment.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
	
	private Long id;
    private Double totalAmount;
    private String status;        // CONFIRMED, PENDING, CANCELED
    private String paymentStatus; // SUCCESS, FAILED
    
    
}
