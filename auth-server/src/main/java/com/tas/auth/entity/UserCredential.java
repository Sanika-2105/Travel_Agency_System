package com.tas.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="USER_CREDENTIALS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(unique = true, nullable = false)
	@NotBlank(message = "Username must not be blank")
    private String username;
	
	@NotBlank(message = "Password must not be blank")
    private String password;
}
