package com.tas.booking.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tas.booking.enums.BookingStatus;
import com.tas.booking.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long customerId;
    private Long packageId;
    private LocalDate bookingDate;
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status; // PENDING, CONFIRMED, CANCELED
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // SUCCESS, FAILED, PENDING ,PARTIAL
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;
    @Column(nullable = true)
    private LocalDateTime updated_at;
    
    @PrePersist
	public void onCreate()
	{
		created_at=LocalDateTime.now();
		
	}
	@PreUpdate
	public void onUpdate()
	{
		updated_at=LocalDateTime.now();
	}
}
