package com.tas.customer.exception;

public class CustomerNotFoundException extends RuntimeException {

	public CustomerNotFoundException(String message) {
		super(message);
	}

	public CustomerNotFoundException(Long id) {
		super("Customer with id " + id + " not found");
	}
}
