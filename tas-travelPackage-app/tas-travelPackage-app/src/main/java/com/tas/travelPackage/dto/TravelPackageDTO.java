package com.tas.travelPackage.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
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
public class TravelPackageDTO {
	
	private Long id;
	
	@NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name must not exceed 150 characters")
	private String name;
	
	@NotBlank(message = "Description is required")
	private String description;
	
	@NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
	private Double price;
	
	@NotNull(message = "Available from date is required")
    @FutureOrPresent(message = "Available from date cannot be in the past")
    private LocalDate availableFrom;
	
	@NotNull(message = "Available to date is required")
    @FutureOrPresent(message = "Available to date cannot be in the past")
	private LocalDate availableTo;
	
	private Long agencyId;   
    private String agencyName; 
	
	//Validation to ensure availableTo is not before availableFrom.
	@AssertTrue(message = "Available to date must be after or equal to available from date")
	@JsonIgnore
    public boolean isValidDateRange() {
        if (availableFrom == null || availableTo == null) return true;
        return availableTo.isAfter(availableFrom) || availableTo.isEqual(availableFrom);
    }
	
	
	

}
