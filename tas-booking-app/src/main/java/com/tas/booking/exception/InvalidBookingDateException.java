package com.tas.booking.exception;

public class InvalidBookingDateException extends RuntimeException {
	private final Long packageId;

	public InvalidBookingDateException(Long packageId, String message) {
		super(message);
		this.packageId = packageId;
	}

	public Long getPackageId() {
		return packageId;
	}
}
