package com.tas.booking.service;

import java.util.List;

import com.tas.booking.client.dto.PaymentDTO;
import com.tas.booking.dto.BookingDTO;
import com.tas.booking.enums.BookingStatus;

public interface BookingService {
	 public BookingDTO createBooking(BookingDTO dto);
	 public BookingDTO getBookingById(Long id);
	 public List<BookingDTO> getAllBookings();
	 public BookingDTO updateBooking(Long id, BookingDTO dto);
	 public void deleteBooking(Long id);
	 public BookingDTO updateBookingStatus(Long id, BookingStatus status);
	 public BookingDTO updateBookingStatusFromPayment(Long id, PaymentDTO paymentResponse);
}
