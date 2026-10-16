package com.tas.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByBookingId(Long bookingId);
}
