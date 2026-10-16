package com.tas.travelPackage.exceptionImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tas.travelPackage.exception.AgencyNotFoundException;
import com.tas.travelPackage.exception.TravelPackageNotFoundException;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TravelPackageNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleTravelPackageNotFound(TravelPackageNotFoundException ex) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("status", HttpStatus.NOT_FOUND.value());
		errorResponse.put("error", "Not Found");
		errorResponse.put("message", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AgencyNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleAgencyNotFound(AgencyNotFoundException ex) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("status", HttpStatus.NOT_FOUND.value());
		errorResponse.put("error", "Agency Not Found");
		errorResponse.put("message", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

		System.out.println(ex.getBindingResult().getAllErrors());
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
		errorResponse.put("error", "Validation Error");

		Map<String, String> fieldErrors = new HashMap<>();

		// Collect field-level errors (e.g. @NotBlank, @FutureOrPresent)
		ex.getBindingResult().getFieldErrors().forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

		// Collect global errors (e.g. @AssertTrue on isValidDateRange)
		ex.getBindingResult().getGlobalErrors().forEach(err -> {
			// Instead of using object name, attach to a meaningful key
			fieldErrors.put("availableTo", err.getDefaultMessage());
		});

		errorResponse.put("messages", fieldErrors);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
		errorResponse.put("error", "Invalid Argument");
		errorResponse.put("message", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
		errorResponse.put("error", "Validation Error");

		Map<String, String> violations = new HashMap<>();
		ex.getConstraintViolations().forEach(v -> {
			// Extract property path and message
			String field = v.getPropertyPath().toString();
			violations.put(field, v.getMessage());
		});

		errorResponse.put("messages", violations);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(FeignException.NotFound.class)
	public ResponseEntity<Map<String, Object>> handleFeignNotFound(FeignException.NotFound ex) {
	    Map<String, Object> errorResponse = new HashMap<>();
	    errorResponse.put("timestamp", LocalDateTime.now());
	    errorResponse.put("status", HttpStatus.NOT_FOUND.value());
	    errorResponse.put("error", "Agency Not Found");
	    errorResponse.put("message", "Agency not found: " + ex.getMessage());
	    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
	}

	private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		return new ResponseEntity<>(body, status);
	}

}
