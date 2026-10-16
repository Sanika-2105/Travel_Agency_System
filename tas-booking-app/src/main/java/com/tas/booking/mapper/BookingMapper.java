package com.tas.booking.mapper;

import com.tas.booking.dto.BookingDTO;
import com.tas.booking.entity.Booking;

public class BookingMapper {
	//convert Entity->DTO
	public static BookingDTO toDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setCustomerId(booking.getCustomerId());
        dto.setPackageId(booking.getPackageId());
        dto.setBookingDate(booking.getBookingDate());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setStatus(booking.getStatus());
        dto.setPaymentStatus(booking.getPaymentStatus());
        
        return dto;
    }
	
	//convert DTO->Entity
	
	public static Booking toEntity(BookingDTO dto) {
        Booking booking = new Booking();
        if (dto.getId() != null) {
            booking.setId(dto.getId());
        }
        booking.setCustomerId(dto.getCustomerId());
        booking.setPackageId(dto.getPackageId());
        booking.setBookingDate(dto.getBookingDate());
        booking.setTotalAmount(dto.getTotalAmount());
        booking.setStatus(dto.getStatus());
        booking.setPaymentStatus(dto.getPaymentStatus());
        return booking;
    }
	
}
