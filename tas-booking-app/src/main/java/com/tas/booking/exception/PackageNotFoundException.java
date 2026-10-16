package com.tas.booking.exception;

public class PackageNotFoundException extends RuntimeException {

	public PackageNotFoundException(Long id) {
		super("Travel package not found with id: " + id);
		
	}

}
