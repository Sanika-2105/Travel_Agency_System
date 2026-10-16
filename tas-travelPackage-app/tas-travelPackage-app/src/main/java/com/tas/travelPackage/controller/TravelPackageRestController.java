package com.tas.travelPackage.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tas.travelPackage.dto.TravelPackageDTO;
import com.tas.travelPackage.service.TravelPackageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/travel-packages")
@Tag(name = "Travel Package API", description = "Operations related to TAS - Travel Package Service")
@Validated 
public class TravelPackageRestController {
	private final TravelPackageService travelPackageService;
	//constructor injection
	public TravelPackageRestController(TravelPackageService travelPackageService) {
		super();
		this.travelPackageService = travelPackageService;
	}
	
	//get all travel packages
	@Operation(
		    summary = "Get all travel packages",
		    description = "Retrieves a list of all travel packages available in the system."
		)
	@GetMapping
	public ResponseEntity<List<TravelPackageDTO>> getAllPackages()
	{
		return ResponseEntity.ok(travelPackageService.getAllPackages());
	}
	
	//get travel package by id
	@Operation(
		    summary = "Get travel package by ID",
		    description = "Retrieves details of a specific travel package using its unique identifier."
		)
	@GetMapping("/{id}")
	public ResponseEntity<TravelPackageDTO> getPackageById(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id)
	{
		return ResponseEntity.ok(travelPackageService.getPackageById(id));
	}
	
	//create new travel package
	@Operation(
		    summary = "Create a new travel package",
		    description = "Adds a new travel package by providing name, description, price, available from date, and available to date. "
		    		+ "The system automatically generates the package ID and timestamps."
		)
	@PostMapping
	public ResponseEntity<TravelPackageDTO> createPackage(@Valid @RequestBody TravelPackageDTO dto)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(travelPackageService.createPackage(dto));
	}
	
	//update existing travel package
	@Operation(
		    summary = "Update travel package information",
		    description = "Updates an existing travel package’s details such as name, description, price, and availability dates using its unique identifier."
		)
	@PutMapping("/{id}")
	public ResponseEntity<TravelPackageDTO> updatePackage(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id, @Valid @RequestBody TravelPackageDTO dto)
	{
		return ResponseEntity.ok(travelPackageService.updatePackage(id, dto));
	}
	
	//delete travel package
	@Operation(
		    summary = "Delete a travel package",
		    description = "Removes a travel package from the system using its unique identifier."
		)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePackage(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id)
	{
		travelPackageService.deletePackage(id);
		return ResponseEntity.ok("Travel package with id " + id + " deleted successfully");
	}
	@Operation(
		    summary = "Get travel packages by Agency ID",
		    description = "Retrieves all travel packages offered by a specific agency using its unique identifier."
		)
		@GetMapping("/agency/{agencyId}")
		public ResponseEntity<List<TravelPackageDTO>> getPackagesByAgency(
		        @PathVariable @Min(value = 1, message = "AgencyId must be greater than 0") Long agencyId) {
		    return ResponseEntity.ok(travelPackageService.getPackagesByAgencyId(agencyId));
		}
}
