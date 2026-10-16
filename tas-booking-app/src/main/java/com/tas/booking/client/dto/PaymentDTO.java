package com.tas.booking.client.dto;

import java.time.LocalDate;

import com.tas.booking.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	private Long id;              
    private Long bookingId;      
    private Double amount;        
    private String paymentMethod; // e.g. Credit Card, UPI
    private PaymentStatus paymentStatus; // SUCCESS, FAILED
    private LocalDate paymentDate;
}
