package com.tas.payment.mapper;

import com.tas.payment.dto.PaymentDTO;
import com.tas.payment.entity.Payment;

public class PaymentMapper {
	
	//Convert Entity->DTO
	public static PaymentDTO toDto(Payment payment)
	{
		PaymentDTO dto=new PaymentDTO();
		dto.setId(payment.getId());
		dto.setAmount(payment.getAmount());
		dto.setBookingId(payment.getBookingId());
		dto.setPaymentMethod(payment.getPaymentMethod());
		dto.setPaymentStatus(payment.getPaymentStatus());
		dto.setPaymentDate(payment.getPaymentDate());
		return dto;
	}
	
	//Convert DTO->Entity
	
	public static Payment toEntity(PaymentDTO dto)
	{
		Payment payment = new Payment();
        if (dto.getId() != null) {
            payment.setId(dto.getId());
        }
        payment.setBookingId(dto.getBookingId());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        return payment;
	}
}
