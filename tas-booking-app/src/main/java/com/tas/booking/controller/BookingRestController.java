package com.tas.booking.controller;

import java.util.List;

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

import com.tas.booking.client.dto.PaymentDTO;
import com.tas.booking.dto.BookingDTO;
import com.tas.booking.dto.BookingStatusUpdateDTO;
import com.tas.booking.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/bookings")
@Validated
@Tag(name = "Booking API", description = "Operations related to TAS - Booking Service")

public class BookingRestController {
	private final BookingService bookingService;
	
	//constructor injection
	
	public BookingRestController(BookingService bookingService) {
		super();
		this.bookingService = bookingService;
	}
	
	//create new booking
	
	@Operation(summary = "Create a new booking", 
            description = "Creates a booking for a customer with a travel package. Returns the created booking details.")
	@PostMapping
    public BookingDTO createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        return bookingService.createBooking(bookingDTO);
    }
	
	//fetch booking by id
	
	@Operation(summary = "Get booking by ID", 
            description = "Fetches a booking by its unique identifier. Throws BookingNotFoundException if not found.")
	@GetMapping("/{id}")
    public BookingDTO getBookingById(@PathVariable @Min(value = 1, message = "Id must be greater than 0")Long id) {
        return bookingService.getBookingById(id);
    }
	
	//fetch all bookings
	
	@Operation(summary = "Get all bookings", 
            description = "Retrieves a list of all bookings available in the system.")
	@GetMapping
    public List<BookingDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }
	
	//update booking
	
	@Operation(summary = "Update booking status", 
            description = "Updates the status of an existing booking (e.g., CONFIRMED, CANCELED). Other fields cannot be modified.")
	@PutMapping("/{id}/status")
	public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id,
	                                                      @Valid @RequestBody BookingStatusUpdateDTO dto) {
	    return ResponseEntity.ok(bookingService.updateBookingStatus(id, dto.getStatus()));
	}
	
	//update 
	@PutMapping("/{id}/payment-status")
	public ResponseEntity<BookingDTO> updateBookingStatusFromPayment(
	        @PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id,
	        @Valid @RequestBody PaymentDTO paymentResponse) {

	    BookingDTO updatedBooking = bookingService.updateBookingStatusFromPayment(id, paymentResponse);
	    return ResponseEntity.ok(updatedBooking);
	}
	
	@Operation(summary = "Delete a booking", 
            description = "Deletes a booking by its ID. Returns a confirmation message if successful.")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBooking(@PathVariable @Min(value = 1, message = "Id must be greater than 0") Long id) {
	    bookingService.deleteBooking(id);
	    return ResponseEntity.ok("Booking with id "+id+ " deleted Successfully");
	}
}
