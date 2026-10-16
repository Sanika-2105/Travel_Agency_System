package com.tas.booking.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tas.booking.client.CustomerClient;
import com.tas.booking.client.PaymentClient;
import com.tas.booking.client.TravelPackageClient;
import com.tas.booking.client.dto.CustomerDTO;
import com.tas.booking.client.dto.PaymentDTO;
import com.tas.booking.client.dto.TravelPackageDTO;
import com.tas.booking.dto.BookingDTO;
import com.tas.booking.entity.Booking;
import com.tas.booking.enums.BookingStatus;
import com.tas.booking.enums.PaymentStatus;
import com.tas.booking.exception.BookingNotFoundException;
import com.tas.booking.exception.CustomerNotFoundException;
import com.tas.booking.exception.InvalidBookingDateException;
import com.tas.booking.exception.PackageNotFoundException;
import com.tas.booking.mapper.BookingMapper;
import com.tas.booking.repository.BookingRepository;
import com.tas.booking.service.BookingService;

import feign.FeignException;

@Service
public class BookingServiceImpl implements BookingService {
	private final BookingRepository bookingRepo;
	private final CustomerClient customerClient;
	private final TravelPackageClient travelPackageClient;
	private final PaymentClient paymentClient;

	public BookingServiceImpl(BookingRepository bookingRepo, CustomerClient customerClient,
			TravelPackageClient travelPackageClient, PaymentClient paymentClient) {
		super();
		this.bookingRepo = bookingRepo;
		this.customerClient = customerClient;
		this.travelPackageClient = travelPackageClient;
		this.paymentClient = paymentClient;
	}

	@Override
	public BookingDTO createBooking(BookingDTO dto) {
		// Validate customer

		System.out.println("Starting booking creation for customerId=" + dto.getCustomerId() + ", packageId="
				+ dto.getPackageId());

		CustomerDTO customer;
		try {
			customer = customerClient.getCustomerById(dto.getCustomerId());
		} catch (FeignException.NotFound ex) {
			throw new CustomerNotFoundException(dto.getCustomerId());
		}
		System.out.println("Customer validated: ");

		// Validate travel package

		TravelPackageDTO travelPackage;
		try {
			travelPackage = travelPackageClient.getPackageById(dto.getPackageId());
		} catch (FeignException.NotFound ex) {
			throw new PackageNotFoundException(dto.getPackageId());
		}
		System.out.println(
				"Travel package validated: " + travelPackage.getName() + ", price=" + travelPackage.getPrice());
		
		LocalDate bookingDate = dto.getBookingDate();
		if (bookingDate.isBefore(travelPackage.getAvailableFrom())
				|| bookingDate.isAfter(travelPackage.getAvailableTo())) {
//			throw new RuntimeException("Booking date must be between " + travelPackage.getAvailableFrom() + " and "
//					+ travelPackage.getAvailableTo());
			throw new InvalidBookingDateException(dto.getPackageId(),
		            "Booking date must be between " +
		            travelPackage.getAvailableFrom() + " and " + travelPackage.getAvailableTo());
		}
		// Map DTO to entity and set defaults
		Booking booking = BookingMapper.toEntity(dto);
		booking.setStatus(BookingStatus.PENDING);
		booking.setPaymentStatus(PaymentStatus.PENDING);
		booking.setTotalAmount(travelPackage.getPrice());

		Booking savedBooking = bookingRepo.save(booking);
		System.out.println(
				"Booking saved with id=" + savedBooking.getId() + ", totalAmount=" + savedBooking.getTotalAmount());

		// only if valid ,Trigger payment automatically
		PaymentDTO paymentRequest = new PaymentDTO();
		paymentRequest.setBookingId(savedBooking.getId());
		paymentRequest.setPaymentMethod("Credit Card");
		paymentRequest.setAmount(dto.getAmount()); // user’s actual payment

		System.out.println("Calling Payment MS with bookingId=" + paymentRequest.getBookingId());

		try {
			PaymentDTO paymentResponse = paymentClient.processPayment(paymentRequest);
//			System.out.println("Payment response received: status=" + paymentResponse.getPaymentStatus());
//			savedBooking.setPaymentStatus(paymentResponse.getPaymentStatus());
//			savedBooking.setStatus(paymentResponse.getPaymentStatus() == PaymentStatus.SUCCESS ? BookingStatus.CONFIRMED
//					: BookingStatus.CANCELED);
			
			if (paymentResponse.getPaymentStatus() == PaymentStatus.SUCCESS) {
	            savedBooking.setStatus(BookingStatus.CONFIRMED);
	            savedBooking.setPaymentStatus(PaymentStatus.SUCCESS);
	        } else if (paymentResponse.getPaymentStatus() == PaymentStatus.PARTIAL) {
	            savedBooking.setStatus(BookingStatus.PENDING);
	            savedBooking.setPaymentStatus(PaymentStatus.PARTIAL);
	        } else {
	            savedBooking.setStatus(BookingStatus.CANCELED);
	            savedBooking.setPaymentStatus(PaymentStatus.FAILED);
	        }
			bookingRepo.save(savedBooking);
		} catch (Exception ex) {
			System.out.println("Payment service call failed: " + ex.getMessage());
			savedBooking.setPaymentStatus(PaymentStatus.FAILED);
			savedBooking.setStatus(BookingStatus.CANCELED);
			bookingRepo.save(savedBooking);
		}
		//bookingRepo.save(savedBooking);
		System.out.println("Final booking status=" + savedBooking.getStatus() + ", paymentStatus="
				+ savedBooking.getPaymentStatus());

		// Map to DTO and enrich with agency info
		BookingDTO response = BookingMapper.toDTO(savedBooking);
		response.setAgencyId(travelPackage.getAgencyId());
		response.setAgencyName(travelPackage.getAgencyName());
		return response;

	}

	@Override
	public BookingDTO getBookingById(Long id) {
//		return bookingRepo.findById(id)
//        .map(BookingMapper::toDTO)
//        .orElseThrow(() -> new BookingNotFoundException(id));

		Booking booking = bookingRepo.findById(id).orElseThrow(() -> new BookingNotFoundException(id));

		BookingDTO dto = BookingMapper.toDTO(booking);
		TravelPackageDTO travelPackage = travelPackageClient.getPackageById(booking.getPackageId());
		dto.setAgencyId(travelPackage.getAgencyId());
		dto.setAgencyName(travelPackage.getAgencyName());

		return dto;
	}

	@Override
	public List<BookingDTO> getAllBookings() {
//		return bookingRepo.findAll()
//                .stream()
//                .map(BookingMapper::toDTO)
//                .collect(Collectors.toList());
		return bookingRepo.findAll().stream().map(booking -> {
			BookingDTO dto = BookingMapper.toDTO(booking);
			TravelPackageDTO travelPackage = travelPackageClient.getPackageById(booking.getPackageId());
			dto.setAgencyId(travelPackage.getAgencyId());
			dto.setAgencyName(travelPackage.getAgencyName());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public BookingDTO updateBooking(Long id, BookingDTO dto) {
		Booking booking = bookingRepo.findById(id).orElseThrow(() -> new BookingNotFoundException(id));

		// Only allow status change
		booking.setStatus(dto.getStatus());

		Booking updated = bookingRepo.save(booking);
		BookingDTO response = BookingMapper.toDTO(updated);
		TravelPackageDTO travelPackage = travelPackageClient.getPackageById(updated.getPackageId());
		response.setAgencyId(travelPackage.getAgencyId());
		response.setAgencyName(travelPackage.getAgencyName());
		return response;
	}

	@Override
	public void deleteBooking(Long id) {
		Booking booking = bookingRepo.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
		bookingRepo.delete(booking);

	}

	@Override
	public BookingDTO updateBookingStatus(Long id, BookingStatus status) {

		Booking booking = bookingRepo.findById(id).orElseThrow(() -> new BookingNotFoundException(id));

		booking.setStatus(status);
		Booking updated = bookingRepo.save(booking);
		BookingDTO response = BookingMapper.toDTO(updated);

		TravelPackageDTO travelPackage = travelPackageClient.getPackageById(updated.getPackageId());
		response.setAgencyId(travelPackage.getAgencyId());
		response.setAgencyName(travelPackage.getAgencyName());
		return response;
	}

	@Override
	public BookingDTO updateBookingStatusFromPayment(Long id, PaymentDTO paymentResponse) {
		Booking booking = bookingRepo.findById(id)
		        .orElseThrow(() -> new BookingNotFoundException(id));

		    if (paymentResponse.getPaymentStatus() == PaymentStatus.SUCCESS) {
		        booking.setStatus(BookingStatus.CONFIRMED);
		        booking.setPaymentStatus(PaymentStatus.SUCCESS);
		    } else if (paymentResponse.getPaymentStatus() == PaymentStatus.PARTIAL) {
		        booking.setStatus(BookingStatus.PENDING);
		        booking.setPaymentStatus(PaymentStatus.PARTIAL);
		    } else {
		        booking.setStatus(BookingStatus.CANCELED);
		        booking.setPaymentStatus(PaymentStatus.FAILED);
		    }

		    bookingRepo.save(booking);
		    return BookingMapper.toDTO(booking);
	}

}
