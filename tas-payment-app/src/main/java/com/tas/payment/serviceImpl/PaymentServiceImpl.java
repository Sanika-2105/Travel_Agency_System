package com.tas.payment.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tas.payment.client.dto.BookingDTO;
import com.tas.payment.clients.BookingClient;
import com.tas.payment.dto.PaymentDTO;
import com.tas.payment.entity.Payment;
import com.tas.payment.enums.PaymentStatus;
import com.tas.payment.exception.InvalidPaymentException;
import com.tas.payment.exception.PaymentNotFoundException;
import com.tas.payment.mapper.PaymentMapper;
import com.tas.payment.repository.PaymentRepository;
import com.tas.payment.service.PaymentService;

import feign.FeignException;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	private final PaymentRepository paymentRepo;
	private final BookingClient bookingClient;
	
	//constructor injection
	public PaymentServiceImpl(PaymentRepository paymentRepo, BookingClient bookingClient) {
		super();
		this.paymentRepo = paymentRepo;
		this.bookingClient = bookingClient;
	}

	//Process new payment
	@Override
	public PaymentDTO processPayment(PaymentDTO dto) {
		
		System.out.println("Received payment request for bookingId: " + dto.getBookingId());
		// Call Booking Service to validate booking
	    BookingDTO booking ;
	    try {
	    	booking= bookingClient.getBookingById(dto.getBookingId());
	    }catch(FeignException.NotFound e) {
	    	 throw new InvalidPaymentException("Booking not found with id: " + dto.getBookingId());
	    }
	    
	    
	    if (dto.getAmount() == null || dto.getAmount() <= 0) {
	        throw new InvalidPaymentException("Payment amount must be positive");
	    }
	    if (dto.getAmount() > booking.getTotalAmount()) {
	        throw new InvalidPaymentException("Payment exceeds booking total");
	    }
	    
	    double totalPaid = paymentRepo.findByBookingId(dto.getBookingId())
                .stream()
                .mapToDouble(Payment::getAmount)
                .sum();
	    double newTotal = totalPaid + dto.getAmount();
		
		
	    Payment payment = new Payment();
	    payment.setBookingId(dto.getBookingId());
	    payment.setPaymentMethod(dto.getPaymentMethod());
	    payment.setAmount(dto.getAmount());
	    payment.setPaymentDate(LocalDate.now());

	    if (newTotal < booking.getTotalAmount()) {
	        payment.setPaymentStatus(PaymentStatus.PARTIAL);
	    } else if (newTotal == booking.getTotalAmount()) {
	        payment.setPaymentStatus(PaymentStatus.SUCCESS);
	    } else {
	        throw new InvalidPaymentException("Payment exceeds booking total");
	    }
	    
	    Payment saved = paymentRepo.save(payment);
	    bookingClient.updateBookingStatusFromPayment(saved.getBookingId(),
	    	    new PaymentDTO(
	    	        saved.getId(),
	    	        saved.getBookingId(),
	    	        saved.getPaymentMethod(),
	    	        saved.getPaymentStatus(),
	    	        saved.getAmount(),
	    	        saved.getPaymentDate()
	    	    ));
	    //retrun DTO to caller
	    return new PaymentDTO(
	        saved.getId(),
	        saved.getBookingId(),
	        saved.getPaymentMethod(),
	        saved.getPaymentStatus(),
	        saved.getAmount(),
	        saved.getPaymentDate()
	    );

	}
	//get all payments
	@Override
	public List<PaymentDTO> getAllPayments() {
		
		return paymentRepo.findAll()
						  .stream()
						  .map(PaymentMapper::toDto)
						  .collect(Collectors.toList());
	}
	
	//get payment by its id
	@Override
	public PaymentDTO getPaymentById(Long id) {
		
		return paymentRepo.findById(id)
				   .map(PaymentMapper::toDto)
				   .orElseThrow(()->new PaymentNotFoundException("Payment not found with id: " + id));

	}

	//get payments by bookingId
	@Override
	public List<PaymentDTO> getPaymentsByBooking(Long bookingId) {
		
		List<Payment> payments = paymentRepo.findByBookingId(bookingId);
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException("No payments found for bookingId: " + bookingId);
        }
        return payments.stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
	}

	//update payment using id
	@Override
	public PaymentDTO updatePayment(Long id, PaymentDTO dto) {
		Payment existingPayment=paymentRepo.findById(id)
										   .orElseThrow(()-> new PaymentNotFoundException("Payment not found with id: " + id));
		existingPayment.setPaymentMethod(dto.getPaymentMethod());
//		existingPayment.setAmount(dto.getAmount());
//		existingPayment.setPaymentStatus(dto.getPaymentStatus());
//		existingPayment.setPaymentDate(dto.getPaymentDate());
        Payment updatedPayment = paymentRepo.save(existingPayment);
        return PaymentMapper.toDto(updatedPayment);
		
	}
	
	//delete payment using id
	@Override
	public void deletePayment(Long id) {
		if(!paymentRepo.existsById(id))
		{
			throw new PaymentNotFoundException("Payment not found with id: " + id);
		}
		paymentRepo.deleteById(id);
		
	}
	
	
	
	
	
	
	
	
}
