package com.cts.tas.agency.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.tas.agency.dto.AgencyDTO;
import com.cts.tas.agency.service.AgencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/agencies")
@Tag(name = "Agency API", description = "Operations related to TAS - Agency Service")
@Validated
public class AgencyRestController {
	
	private final AgencyService agencyService;
	
	public AgencyRestController(AgencyService agencyService) {
		super();
		this.agencyService = agencyService;
	}

	//Get all agencies
	@Operation(
	        summary = "Get all agencies",
	        description = "Retrieves a list of all agencies available in the system."
	    )
	@GetMapping
	public ResponseEntity<List<AgencyDTO>> getAllAgencies()
	{
		return ResponseEntity.ok(agencyService.getAllAgencies());
	}
	
	//Get an agency based on id
	
	@Operation(
	        summary = "Get an agency based on id",
	        description = "Retrieves an agency based on the id ."
	    )
	@GetMapping("/{id}")
	public ResponseEntity<AgencyDTO> getAgencyById(@PathVariable @Min(value = 1, message = "Id must be greater than 0")Long id)
	{
		return ResponseEntity.ok(agencyService.getAgencyById(id));
	}
	
	//Post new agency
	@Operation(
	        summary = "Create a new agency",
	        description = "Adds a new agency with name, contact email, and address."
	    )
	@PostMapping
	public ResponseEntity<AgencyDTO> createAgency(@Valid @RequestBody AgencyDTO dto )
	{
		AgencyDTO createdAgency=agencyService.createAgency(dto);
		return ResponseEntity.ok(createdAgency);
	}
	
	//Update agency 
	
	@Operation(
	        summary = "Update an agency",
	        description = "Updates an existing agency by ID."
	    )
	@PutMapping("/{id}")
	public ResponseEntity<AgencyDTO> updateAgency(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id,@RequestBody AgencyDTO dto)
	{
		AgencyDTO updatedAgency=agencyService.updateAgency(id, dto);
		return ResponseEntity.ok(updatedAgency);
	}
	
	//delete agency
	@Operation(
	        summary = "Delete an agency",
	        description = "Deletes an existing agency by ID."
	    )
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAgency(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id) 
	{
		agencyService.deleteAgency(id);
		return ResponseEntity.ok("Agency with id " + id + " deleted successfully");
		
	}

}
