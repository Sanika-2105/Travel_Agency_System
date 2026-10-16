package com.tas.payment.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tas.payment.enums.PaymentStatus;

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
@Table(name="payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 	
	 	@Column(nullable = false)
	    private Long bookingId;
	 	
	 	@Column(nullable = false, length = 50)
	    private String paymentMethod;
	 	
	 	@Enumerated(EnumType.STRING)
	    @Column(nullable = false, length = 50)
	    private PaymentStatus paymentStatus;
	 		
	 	@Column(nullable = false)
	 	private double amount;

	 	@Column(nullable = false)
	    private LocalDate paymentDate;
	 	
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
