package com.tas.booking.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tas.booking.enums.BookingStatus;
import com.tas.booking.enums.PaymentStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
	
	private Long id;

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be a positive number")
    private Long customerId;

    @NotNull(message = "Package ID is required")
    @Positive(message = "Package ID must be a positive number")
    private Long packageId;

    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date must be today or in the future")
    private LocalDate bookingDate;

    //@NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    private Double totalAmount;
    
    // user’s payment amount 
    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than 0")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Double amount; 

    //@NotNull(message = "Status is required")
    private BookingStatus status; // CONFIRMED, PENDING, CANCELED
    
    //@NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus; // SUCCESS, FAILED
    
    private Long agencyId;
    private String agencyName;
}
