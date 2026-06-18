package com.cts.tas.agency.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyDTO {
	
	private Long id;
	
	@NotBlank(message ="Agency Name must not be empty")
	@Size(max = 150, message = "Agency Name must not exceed 150 characters")
	private String name;
	
	@NotBlank(message = "Contact email must not be empty")
    @Email(message = "Contact email must be valid")
    @Size(max = 100, message = "Contact email must not exceed 100 characters")
	private String contactEmail;
	
	@NotBlank(message = "Address must not be empty")
    @Size(max = 250, message = "Address must not exceed 250 characters")
	private String address;
	
}
