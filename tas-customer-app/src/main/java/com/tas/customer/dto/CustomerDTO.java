package com.tas.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
	private Long id;

	@NotBlank(message = "First name is required")
	@Size(max = 100, message = "First name must not exceed 100 characters")
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Size(max = 100, message = "Last name must not exceed 100 characters")
	private String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	@Size(max = 100, message = "Email must not exceed 100 characters")
	private String email;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
	private String phone;

	@NotBlank(message = "Address is required")
	@Size(max = 250, message = "Address must not exceed 250 characters")
	private String address;

}
