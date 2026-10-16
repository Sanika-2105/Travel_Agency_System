package com.tas.travelPackage.exception;

public class TravelPackageNotFoundException extends RuntimeException{

	public TravelPackageNotFoundException(String message) {
		super(message);
	}
	public TravelPackageNotFoundException(Long id) {
		super("Travel Agency with not found with id: "+id);
	}
	
}
