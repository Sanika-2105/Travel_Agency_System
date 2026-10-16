package com.tas.travelPackage.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="travel_packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelPackage {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 150, nullable = false)
	private String name;
	
	@Column(columnDefinition = "TEXT", nullable = false)
	private String description;
	
	@Column(columnDefinition = "DECIMAL(10,2)", nullable = false)
	private Double price;
	
	@Column(name = "available_from", nullable = false)
	private LocalDate availableFrom;
	
	@Column(name = "available_to", nullable = false)
	private LocalDate availableTo;
	
	private Long agencyId;    
	
    private String agencyName; 
	
	@Column(nullable = false,updatable = false)
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
