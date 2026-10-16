package com.tas.customer.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tas.customer.dto.CustomerDTO;
import com.tas.customer.entity.Customer;
import com.tas.customer.exception.CustomerNotFoundException;
import com.tas.customer.mapper.CustomerMapper;
import com.tas.customer.repository.CustomerRepository;
import com.tas.customer.service.CustomerService;
@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepo;
	
	//constructor injection
	public CustomerServiceImpl(CustomerRepository customerRepo) {
		super();
		this.customerRepo = customerRepo;
	}
	
	//get all customers
	@Override
	public List<CustomerDTO> getAllCustomers() {
		
		return customerRepo.findAll()
							.stream()
							.map(CustomerMapper::toDto)
							.collect(Collectors.toList());
	}

	//get customer by id
	@Override
	public CustomerDTO getCustomerById(Long id) {
		Customer customer=customerRepo.findById(id)
									  .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
		return CustomerMapper.toDto(customer);
		
	}

	//create new customer
	@Override
	public CustomerDTO createCustomer(CustomerDTO dto) {
		Customer customer=CustomerMapper.toEntity(dto);
		Customer savedCustomer=customerRepo.save(customer);
		return CustomerMapper.toDto(savedCustomer);
		
	}
	
	//update customer
	@Override
	public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
		Customer existingCustomer=customerRepo.findById(id)
											  .orElseThrow(()->new CustomerNotFoundException("Customer not found with id: " + id));
											  
		existingCustomer.setFirstName(dto.getFirstName());
		existingCustomer.setLastName(dto.getLastName());
		existingCustomer.setAddress(dto.getAddress());
		existingCustomer.setEmail(dto.getEmail());
		existingCustomer.setPhone(dto.getPhone());
		
		Customer UpdatedCustomer=customerRepo.save(existingCustomer);
		return CustomerMapper.toDto(UpdatedCustomer);
		
	}
	
	//delete customer
	@Override
	public void deleteCustomer(Long id) {
		if(!customerRepo.existsById(id))
		{
			throw new CustomerNotFoundException("Customer not found with id: " + id);
		}
		customerRepo.deleteById(id);
		
	}
	
	

	
	
	

}
