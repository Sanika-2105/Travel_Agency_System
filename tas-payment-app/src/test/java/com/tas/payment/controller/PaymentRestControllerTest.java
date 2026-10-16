package com.tas.payment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tas.payment.dto.PaymentDTO;
import com.tas.payment.service.PaymentService;

@WebMvcTest(PaymentRestController.class)
class PaymentRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PaymentService paymentService;

	@Test
	void testProcessPayment() throws Exception {
		PaymentDTO dto = new PaymentDTO();
		dto.setId(1L);
		dto.setBookingId(101L);
		dto.setAmount(500.0);
		dto.setPaymentMethod("CREDIT_CARD"); // <-- set this

		Mockito.when(paymentService.processPayment(any(PaymentDTO.class))).thenReturn(dto);

		mockMvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON)
				.content("{\"bookingId\":101,\"paymentMethod\":\"CREDIT_CARD\",\"amount\":500.0}"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.bookingId").value(101L))
				.andExpect(jsonPath("$.paymentMethod").value("CREDIT_CARD"))
				.andExpect(jsonPath("$.amount").value(500.0));
	}

	@Test
	void testGetAllPayments() throws Exception {
		PaymentDTO dto = new PaymentDTO();
		dto.setId(1L);
		dto.setBookingId(101L);
		dto.setAmount(500.0);

		Mockito.when(paymentService.getAllPayments()).thenReturn(Collections.singletonList(dto));

		mockMvc.perform(get("/payments")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L));
	}

	@Test
	void testGetPaymentById() throws Exception {
		PaymentDTO dto = new PaymentDTO();
		dto.setId(1L);
		dto.setBookingId(101L);
		dto.setAmount(500.0);

		Mockito.when(paymentService.getPaymentById(1L)).thenReturn(dto);

		mockMvc.perform(get("/payments/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
	}

	@Test
	void testUpdatePayment() throws Exception {
		PaymentDTO dto = new PaymentDTO();
		dto.setId(1L);
		dto.setBookingId(101L);
		dto.setAmount(600.0);
		dto.setPaymentMethod("PAYPAL"); // <-- set this

		Mockito.when(paymentService.updatePayment(eq(1L), any(PaymentDTO.class))).thenReturn(dto);

		mockMvc.perform(put("/payments/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"bookingId\":101,\"paymentMethod\":\"PAYPAL\",\"amount\":600.0}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.paymentMethod").value("PAYPAL"))
				.andExpect(jsonPath("$.amount").value(600.0));
	}

	@Test
	void testDeletePayment() throws Exception {
		Mockito.doNothing().when(paymentService).deletePayment(1L);

		mockMvc.perform(delete("/payments/1")).andExpect(status().isOk())
				.andExpect(content().string("Payment with id 1 deleted successfully."));
	}

}
