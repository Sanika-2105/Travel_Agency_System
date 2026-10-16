package com.cts.tas.agency.entity;

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
@Table(name="AGENCIES")   
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agency {
	
	@Id   //primary key 
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //auto increment
	private Long id;
	
	@Column(nullable=false,length=150)
	private String name;
	
	@Column(nullable=false,length=100)
	private String contactEmail;
	
	@Column(nullable=false,length=250)
	private String address;
	
	@Column(nullable = false, updatable = false)
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
