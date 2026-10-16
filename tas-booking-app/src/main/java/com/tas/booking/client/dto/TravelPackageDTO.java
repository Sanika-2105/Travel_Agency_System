package com.tas.booking.client.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelPackageDTO {
	private Long id;
	private String name;
	private String description;
	private Double price;
	private LocalDate availableFrom;
	private LocalDate availableTo;
	private Long agencyId;
	private String agencyName;
}
