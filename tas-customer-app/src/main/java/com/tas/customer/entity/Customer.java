package com.tas.customer.entity;

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
@Table(name="customers")
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@Column(nullable = false, length = 100)
	private String firstName;
	
	@Column(nullable = false, length = 100)
	private String lastName;
	
	@Column(nullable = false, length = 100, unique = true)
	private String email;
	
	@Column(nullable = false, length = 20)
	private String phone;
	
	@Column(nullable = false, length = 250)
	private String address;
	
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
