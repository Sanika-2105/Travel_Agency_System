package com.tas.customer.controller;

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

import com.tas.customer.dto.CustomerDTO;
import com.tas.customer.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/customers")
@Tag(name = "Customer API", description = "Operations related to TAS - Customer Service")
@Validated
public class CustomerRestController {

	private final CustomerService customerService;
	
	//constructor injection
	public CustomerRestController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}
	
	//Get all customers
	@Operation(
	        summary = "Get all customers",
	        description = "Retrieves a list of all customers available in the system."
	    )
	@GetMapping
	public ResponseEntity<List<CustomerDTO>> getAllCustomers()
	{
		
		return ResponseEntity.ok(customerService.getAllCustomers());
	}
	
	//Get customer by id
	@Operation(
	        summary = "Get customer by id ",
	        description = "Retrieves a customer by id "
	    )
	@GetMapping("/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id)
	{
		return ResponseEntity.ok(customerService.getCustomerById(id));
	}
	
	//Post new Customer
	@Operation(
	        summary = "Create a new Customer ",
	        description = "Adds a new customer by providing first name, last name, email,phone number, and address. "
	        		+"The system automatically generates the customer ID and creation timestamp. "
	    )
	@PostMapping
	public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO dto)
	{
		CustomerDTO createdCustomer=customerService.createCustomer(dto);
		return ResponseEntity.ok(createdCustomer);
	}
	
	//Update customer
	@Operation(
	        summary = "Update a customer",
	        description = "Updates an existing customer by ID."
	    )
	@PutMapping("/{id}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id, @Valid @RequestBody CustomerDTO dto)
	{
		CustomerDTO updatedCustomer=customerService.updateCustomer(id, dto);
		return ResponseEntity.ok(updatedCustomer);
	}
	
	//Delete Customer
	@Operation(
	        summary = "Delete a customer",
	        description = "Deletes a existing customer by ID."
	    )
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id)
	{
		customerService.deleteCustomer(id);
		return ResponseEntity.ok("Customer with id " + id + " deleted successfully");
	}
	
	
}
