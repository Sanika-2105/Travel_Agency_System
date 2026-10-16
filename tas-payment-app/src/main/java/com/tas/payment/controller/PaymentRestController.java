package com.tas.payment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tas.payment.dto.PaymentDTO;
import com.tas.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment API", description = "Operations related to TAS - Payment Service")
@Validated
public class PaymentRestController {
	
	public final PaymentService paymentService;
	//constructor injection
	public PaymentRestController(PaymentService paymentService) {
		super();
		this.paymentService = paymentService;
	}
	
	//process payment
	@PostMapping
	public ResponseEntity<PaymentDTO> processPayment(@Valid @RequestBody PaymentDTO dto) {
	    PaymentDTO result = paymentService.processPayment(dto);
	    return ResponseEntity
	            .status(HttpStatus.CREATED)   // return 201 Created
	            .body(result);
	}
	 //Retrieve all payments
	@GetMapping
	public ResponseEntity<List<PaymentDTO>> getAllPayments()
	{
		return ResponseEntity.ok(paymentService.getAllPayments());
	}
	
	//get payment by paymentId
	 @GetMapping("/{id}")
	public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id)
	{
		return ResponseEntity.ok(paymentService.getPaymentById(id));
	}
	 
	//get payments by bookingId
	 @GetMapping("/booking/{bookingId}")
	 public ResponseEntity<List<PaymentDTO>> getPaymentsByBooking(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long bookingId)
	 {
		 return ResponseEntity.ok(paymentService.getPaymentsByBooking(bookingId));
	 }
	 
	 //update payment
	 @PutMapping("/{id}")
	 public ResponseEntity<PaymentDTO> updatePayment(@PathVariable @Min(value = 1, message = "Id must be greater than 0")Long id, @Valid @RequestBody PaymentDTO dto)
	 {
		 return ResponseEntity.ok(paymentService.updatePayment(id, dto));
	 }
	 
	 //delete payment
	 @DeleteMapping("/{id}")
	 public ResponseEntity<String> deletePayment(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id)
	 {
		 paymentService.deletePayment(id);
		 return ResponseEntity.ok("Payment with id "+id+" deleted successfully.");
	 }
}
