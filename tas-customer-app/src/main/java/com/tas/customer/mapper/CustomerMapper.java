package com.tas.customer.mapper;

import com.tas.customer.dto.CustomerDTO;
import com.tas.customer.entity.Customer;

public class CustomerMapper {
	
	//Entity->DTO
	public static CustomerDTO toDto(Customer customer)
	{
		CustomerDTO dto=new CustomerDTO();
		dto.setId(customer.getId());
		dto.setFirstName(customer.getFirstName());
		dto.setLastName(customer.getLastName());
		dto.setEmail(customer.getEmail());
		dto.setPhone(customer.getPhone());
		dto.setAddress(customer.getAddress());
		return dto;
	}
	
	//DTO->Entity
	
	public static Customer toEntity(CustomerDTO dto)
	{
		Customer customer = new Customer();
        if (dto.getId() != null) {
            customer.setId(dto.getId());
        }
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
       
        return customer;
	}

}
