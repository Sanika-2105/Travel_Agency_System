package com.tas.booking.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
	@Mock
    private BookingRepository bookingRepo;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private TravelPackageClient travelPackageClient;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingDTO buildBookingDTO() {
        BookingDTO dto = new BookingDTO();
        dto.setId(1L);
        dto.setCustomerId(10L);
        dto.setPackageId(101L);
        dto.setBookingDate(LocalDate.now().plusDays(1));
        dto.setTotalAmount(5000.0);
        dto.setStatus(BookingStatus.PENDING);
        dto.setPaymentStatus(PaymentStatus.PENDING);
        return dto;
    }
    
    private TravelPackageDTO buildPackageDTO() {
        TravelPackageDTO pkg = new TravelPackageDTO();
        pkg.setId(101L);
        pkg.setName("Goa Trip");
        pkg.setPrice(5000.0);
        pkg.setAvailableFrom(LocalDate.now());
        pkg.setAvailableTo(LocalDate.now().plusDays(10));
        pkg.setAgencyId(5L);
        pkg.setAgencyName("Holiday Travels");
        return pkg;
    }
    
    @Test
    void testCreateBooking_Success() {
        BookingDTO dto = buildBookingDTO();

        // Customer mock
        CustomerDTO customer = new CustomerDTO();
        customer.setId(10L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("1234567890");
        customer.setAddress("NYC");
        when(customerClient.getCustomerById(10L)).thenReturn(customer);

        // Travel package mock
        TravelPackageDTO pkg = new TravelPackageDTO();
        pkg.setId(101L);
        pkg.setName("Goa Trip");
        pkg.setDescription("Beach holiday");
        pkg.setPrice(5000.0);
        pkg.setAvailableFrom(LocalDate.now());
        pkg.setAvailableTo(LocalDate.now().plusDays(10));
        pkg.setAgencyId(5L);
        pkg.setAgencyName("Holiday Travels");
        when(travelPackageClient.getPackageById(101L)).thenReturn(pkg);

        // Booking repo mock
        when(bookingRepo.save(any(Booking.class))).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            b.setId(1L);
            return b;
        });

        // Payment mock
        PaymentDTO paymentResponse = new PaymentDTO();
        paymentResponse.setId(1L);
        paymentResponse.setBookingId(1L);
        paymentResponse.setAmount(5000.0);
        paymentResponse.setPaymentMethod("Credit Card");
        paymentResponse.setPaymentStatus(PaymentStatus.SUCCESS);
        paymentResponse.setPaymentDate(LocalDate.now());
        when(paymentClient.processPayment(any(PaymentDTO.class))).thenReturn(paymentResponse);

        BookingDTO result = bookingService.createBooking(dto);

        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        assertEquals(PaymentStatus.SUCCESS, result.getPaymentStatus());
        assertEquals("Holiday Travels", result.getAgencyName());
    }
    
    @Test
    void testCreateBooking_CustomerNotFound() {
        BookingDTO dto = buildBookingDTO();
        when(customerClient.getCustomerById(10L)).thenThrow(new CustomerNotFoundException(10L));

        assertThrows(CustomerNotFoundException.class, () -> bookingService.createBooking(dto));
    }
    
    @Test
    void testCreateBooking_PackageNotFound() {
        BookingDTO dto = buildBookingDTO();

        CustomerDTO customer = new CustomerDTO();
        customer.setId(10L);
        customer.setFirstName("John");
        when(customerClient.getCustomerById(10L)).thenReturn(customer);

        when(travelPackageClient.getPackageById(101L)).thenThrow(new PackageNotFoundException(101L));

        assertThrows(PackageNotFoundException.class, () -> bookingService.createBooking(dto));
    }
    
    @Test
    void testCreateBooking_InvalidBookingDate() {
        BookingDTO dto = buildBookingDTO();
        dto.setBookingDate(LocalDate.now().plusDays(20)); // outside package range

        CustomerDTO customer = new CustomerDTO();
        customer.setId(10L);
        customer.setFirstName("John");
        when(customerClient.getCustomerById(10L)).thenReturn(customer);

        TravelPackageDTO pkg = buildPackageDTO();
        pkg.setAvailableTo(LocalDate.now().plusDays(5));
        when(travelPackageClient.getPackageById(101L)).thenReturn(pkg);

        assertThrows(InvalidBookingDateException.class, () -> bookingService.createBooking(dto));
    }
    
    @Test
    void testCreateBooking_PaymentFailure() {
        BookingDTO dto = buildBookingDTO();

        CustomerDTO customer = new CustomerDTO();
        customer.setId(10L);
        customer.setFirstName("John");
        when(customerClient.getCustomerById(10L)).thenReturn(customer);

        TravelPackageDTO pkg = buildPackageDTO();
        when(travelPackageClient.getPackageById(101L)).thenReturn(pkg);

        when(bookingRepo.save(any(Booking.class))).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            b.setId(1L);
            return b;
        });

        when(paymentClient.processPayment(any(PaymentDTO.class))).thenThrow(new RuntimeException("Payment service down"));

        BookingDTO result = bookingService.createBooking(dto);

        assertEquals(BookingStatus.CANCELED, result.getStatus());
        assertEquals(PaymentStatus.FAILED, result.getPaymentStatus());
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(99L));
    }
    
    @Test
    void testUpdateBookingStatus_Success() {
        Booking booking = BookingMapper.toEntity(buildBookingDTO());
        booking.setId(1L);

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));
        when(travelPackageClient.getPackageById(101L)).thenReturn(buildPackageDTO());

        BookingDTO result = bookingService.updateBookingStatus(1L, BookingStatus.CANCELED);

        assertEquals(BookingStatus.CANCELED, result.getStatus());
    }

	
}
