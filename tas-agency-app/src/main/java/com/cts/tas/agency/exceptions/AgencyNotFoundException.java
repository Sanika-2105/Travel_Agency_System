package com.cts.tas.agency.exceptions;

public class AgencyNotFoundException extends RuntimeException {

	public AgencyNotFoundException(Long id) {
		super("Agency with id " + id + " not found");
	}
	public AgencyNotFoundException(String message) {
        super(message);
    }

	 
}
