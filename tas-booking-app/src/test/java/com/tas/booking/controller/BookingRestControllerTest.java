package com.tas.booking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tas.booking.client.dto.PaymentDTO;
import com.tas.booking.dto.BookingDTO;
import com.tas.booking.dto.BookingStatusUpdateDTO;
import com.tas.booking.enums.BookingStatus;
import com.tas.booking.enums.PaymentStatus;
import com.tas.booking.service.BookingService;

@WebMvcTest(BookingRestController.class)
class BookingRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookingService bookingService;

	@Autowired
	private ObjectMapper objectMapper;

	private BookingDTO buildBooking() {
		BookingDTO dto = new BookingDTO();
		dto.setId(1L);
		dto.setCustomerId(10L); // valid
		dto.setPackageId(101L); // valid
		dto.setBookingDate(LocalDate.now().plusDays(5)); // satisfies @FutureOrPresent
		dto.setTotalAmount(5000.0); // valid
		dto.setAmount(1000.0); // REQUIRED field
		dto.setStatus(BookingStatus.CONFIRMED);
		dto.setPaymentStatus(PaymentStatus.SUCCESS);
		dto.setAgencyId(5L);
		dto.setAgencyName("Holiday Travels");
		return dto;
	}

	@Test
	void testCreateBooking_Success() throws Exception {
		BookingDTO booking = buildBooking();

		when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(booking);

		mockMvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(booking))).andDo(print()) // shows response if it fails again
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.customerId").value(10L)).andExpect(jsonPath("$.status").value("CONFIRMED"));
	}

	@Test
	void testCreateBooking_InvalidCustomerId() throws Exception {
		// Build a valid booking, then override the invalid field
		BookingDTO booking = buildBooking();
		booking.setCustomerId(0L); // invalid, must be positive

		mockMvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(booking))).andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	void testGetBookingById() throws Exception {
		BookingDTO booking = buildBooking();

		when(bookingService.getBookingById(1L)).thenReturn(booking);

		mockMvc.perform(get("/bookings/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.customerId").value(10L));
	}

	@Test
	void testGetAllBookings() throws Exception {
		BookingDTO booking1 = buildBooking();
		BookingDTO booking2 = buildBooking();
		booking2.setId(2L);
		booking2.setCustomerId(20L);

		when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking1, booking2));

		mockMvc.perform(get("/bookings")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[1].id").value(2L));
	}

	@Test
	void testUpdateBookingStatus() throws Exception {
		BookingDTO updated = buildBooking();
		updated.setStatus(BookingStatus.CANCELED);

		BookingStatusUpdateDTO statusUpdate = new BookingStatusUpdateDTO();
		statusUpdate.setStatus(BookingStatus.CANCELED);

		when(bookingService.updateBookingStatus(1L, BookingStatus.CANCELED)).thenReturn(updated);

		mockMvc.perform(put("/bookings/1/status").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(statusUpdate))).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("CANCELED"));
	}

	@Test
	void testDeleteBooking() throws Exception {
		mockMvc.perform(delete("/bookings/1")).andExpect(status().isOk())
				.andExpect(content().string("Booking with id 1 deleted Successfully"));
	}

	@Test
	void testUpdateBookingStatusFromPayment_Success() throws Exception {
		BookingDTO updated = buildBooking();
		updated.setStatus(BookingStatus.CONFIRMED);
		updated.setPaymentStatus(PaymentStatus.SUCCESS);

		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setId(13L);
		paymentDTO.setBookingId(13L);
		paymentDTO.setPaymentMethod("Credit Card");
		paymentDTO.setPaymentStatus(PaymentStatus.SUCCESS);
		paymentDTO.setAmount(45000.0);
		paymentDTO.setPaymentDate(LocalDate.now());

		when(bookingService.updateBookingStatusFromPayment(13L, paymentDTO)).thenReturn(updated);

		mockMvc.perform(put("/bookings/13/payment-status").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(paymentDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("CONFIRMED"))
				.andExpect(jsonPath("$.paymentStatus").value("SUCCESS"));
	}

}
