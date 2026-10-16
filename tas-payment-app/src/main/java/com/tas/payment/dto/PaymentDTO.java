package com.tas.payment.dto;

import java.time.LocalDate;

import com.tas.payment.enums.PaymentStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	
	private Long id;

    @NotNull(message = "Booking ID is required")
    @Positive(message = "Booking ID must be positive")
    private Long bookingId;

    @NotBlank(message = "Payment method is required")
    @Size(max = 50, message = "Payment method must not exceed 50 characters")
    private String paymentMethod;

    
    private PaymentStatus paymentStatus;

    //@NotNull(message = "Amount is required")
    //@DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private Double amount;

    //@NotNull(message = "Payment date is required")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    //@FutureOrPresent(message = "Payment date cannot be in the past")
    private LocalDate paymentDate;

}
