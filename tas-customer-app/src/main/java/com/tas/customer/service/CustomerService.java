package com.tas.customer.service;

import java.util.List;

import com.tas.customer.dto.CustomerDTO;

public interface CustomerService {
	public List<CustomerDTO> getAllCustomers();
	
	public CustomerDTO getCustomerById(Long id);
	
	public CustomerDTO createCustomer(CustomerDTO dto);
	
	public CustomerDTO updateCustomer(Long id,CustomerDTO dto);

	public void deleteCustomer(Long id);
}
