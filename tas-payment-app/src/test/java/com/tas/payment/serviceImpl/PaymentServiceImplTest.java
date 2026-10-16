package com.tas.payment.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tas.payment.client.dto.BookingDTO;
import com.tas.payment.clients.BookingClient;
import com.tas.payment.dto.PaymentDTO;
import com.tas.payment.entity.Payment;
import com.tas.payment.enums.PaymentStatus;
import com.tas.payment.exception.InvalidPaymentException;
import com.tas.payment.exception.PaymentNotFoundException;
import com.tas.payment.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

	@Mock
	private PaymentRepository paymentRepo;

	@Mock
	private BookingClient bookingClient;

	@InjectMocks
	private PaymentServiceImpl paymentService;

	@Test
	void testProcessPaymentSuccess() {
		PaymentDTO dto = new PaymentDTO(null, 101L, "CREDIT_CARD", null, null, null);
		BookingDTO booking = new BookingDTO(101L, 500.0, "CONFIRMED", "PENDING");

		when(bookingClient.getBookingById(101L)).thenReturn(booking);
		when(paymentRepo.save(any(Payment.class))).thenAnswer(invocation -> {
			Payment p = invocation.getArgument(0);
			p.setId(1L);
			return p;
		});

		PaymentDTO result = paymentService.processPayment(dto);

		assertNotNull(result.getId());
		assertEquals(101L, result.getBookingId());
		assertEquals("CREDIT_CARD", result.getPaymentMethod());
		assertEquals(500.0, result.getAmount());
		assertEquals(PaymentStatus.SUCCESS, result.getPaymentStatus());
		assertEquals(LocalDate.now(), result.getPaymentDate());
	}

	@Test
	void testProcessPaymentInvalidBooking() {
		PaymentDTO dto = new PaymentDTO(null, 999L, "CREDIT_CARD", null, 100.0, LocalDate.now());
		when(bookingClient.getBookingById(999L)).thenReturn(null);

		assertThrows(InvalidPaymentException.class, () -> paymentService.processPayment(dto));
	}

	@Test
	void testGetPaymentByIdSuccess() {
		Payment payment = new Payment();
		payment.setId(1L);
		payment.setBookingId(101L);
		payment.setPaymentMethod("CREDIT_CARD");
		payment.setAmount(500.0);
		payment.setPaymentStatus(PaymentStatus.SUCCESS);
		payment.setPaymentDate(LocalDate.now());

		when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));

		PaymentDTO result = paymentService.getPaymentById(1L);
		assertEquals(1L, result.getId());
		assertEquals("CREDIT_CARD", result.getPaymentMethod());
	}

	@Test
	void testGetPaymentByIdNotFound() {
		when(paymentRepo.findById(1L)).thenReturn(Optional.empty());
		assertThrows(PaymentNotFoundException.class, () -> paymentService.getPaymentById(1L));
	}

	@Test
	void testGetPaymentsByBookingNotFound() {
		when(paymentRepo.findByBookingId(101L)).thenReturn(Collections.emptyList());
		assertThrows(PaymentNotFoundException.class, () -> paymentService.getPaymentsByBooking(101L));
	}

	@Test
	void testUpdatePaymentSuccess() {
		Payment existing = new Payment();
		existing.setId(1L);
		existing.setBookingId(101L);
		existing.setPaymentMethod("CREDIT_CARD");
		existing.setAmount(500.0);
		existing.setPaymentStatus(PaymentStatus.SUCCESS);
		existing.setPaymentDate(LocalDate.now());

		when(paymentRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(paymentRepo.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

		PaymentDTO updateDto = new PaymentDTO(null, 101L, "PAYPAL", PaymentStatus.FAILED, 600.0, LocalDate.now());
		PaymentDTO result = paymentService.updatePayment(1L, updateDto);

		assertEquals("PAYPAL", result.getPaymentMethod());
		assertEquals(600.0, result.getAmount());
		assertEquals(PaymentStatus.FAILED, result.getPaymentStatus());
	}

	@Test
	void testDeletePaymentNotFound() {
		when(paymentRepo.existsById(1L)).thenReturn(false);
		assertThrows(PaymentNotFoundException.class, () -> paymentService.deletePayment(1L));
	}

	@Test
	void testDeletePaymentSuccess() {
		when(paymentRepo.existsById(1L)).thenReturn(true);
		doNothing().when(paymentRepo).deleteById(1L);

		assertDoesNotThrow(() -> paymentService.deletePayment(1L));
		verify(paymentRepo, times(1)).deleteById(1L);
	}
}
