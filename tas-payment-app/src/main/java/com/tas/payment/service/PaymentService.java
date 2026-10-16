package com.tas.payment.service;

import java.util.List;

import com.tas.payment.dto.PaymentDTO;

public interface PaymentService {
	public PaymentDTO processPayment(PaymentDTO dto);
	public List<PaymentDTO> getAllPayments();
	public PaymentDTO getPaymentById(Long id);
	public List<PaymentDTO> getPaymentsByBooking(Long bookingId);
	public PaymentDTO updatePayment (Long id,PaymentDTO dto);
	public void deletePayment(Long id);
}
