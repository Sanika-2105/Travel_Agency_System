package com.tas.customer.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tas.customer.dto.CustomerDTO;
import com.tas.customer.entity.Customer;
import com.tas.customer.exception.CustomerNotFoundException;
import com.tas.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
	@Mock
	private CustomerRepository customerRepo;

	@InjectMocks
	private CustomerServiceImpl customerService;

//	@BeforeEach
//	void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}

	// Test getAllCustomers
	@Test
	void testGetAllCustomers() {
		Customer c1 = new Customer();
		c1.setId(1L);
		c1.setFirstName("John");
		c1.setLastName("Doe");
		c1.setEmail("john@doe.com");
		c1.setAddress("Hyderabad");
		c1.setPhone("1234567890");

		Customer c2 = new Customer();
		c2.setId(2L);
		c2.setFirstName("Jane");
		c2.setLastName("Smith");
		c2.setEmail("jane@smith.com");
		c2.setAddress("Delhi");
		c2.setPhone("9876543210");

		when(customerRepo.findAll()).thenReturn(Arrays.asList(c1, c2));

		List<CustomerDTO> result = customerService.getAllCustomers();

		assertEquals(2, result.size());
		assertEquals("John", result.get(0).getFirstName());
	}

	// Test getCustomerById success
	@Test
	void testGetCustomerById_Success() {
		Customer c = new Customer();
		c.setId(1L);
		c.setFirstName("John");
		c.setLastName("Doe");
		c.setEmail("john@doe.com");
		c.setAddress("Hyderabad");
		c.setPhone("1234567890");

		when(customerRepo.findById(1L)).thenReturn(Optional.of(c));

		CustomerDTO result = customerService.getCustomerById(1L);

		assertEquals("John", result.getFirstName());
		assertEquals("john@doe.com", result.getEmail());
	}

	// Test getCustomerById throws exception
	@Test
	void testGetCustomerById_NotFound() {
		when(customerRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
	}

	// Test createCustomer
	@Test
	void testCreateCustomer() {
		CustomerDTO dto = new CustomerDTO(null, "New", "Customer", "new@customer.com", "1112223330", "Mumbai");

		Customer saved = new Customer();
		saved.setId(1L);
		saved.setFirstName("New");
		saved.setLastName("Customer");
		saved.setEmail("new@customer.com");
		saved.setPhone("1112223330"); // ✅ 10 digits
		saved.setAddress("Mumbai");

		when(customerRepo.save(any(Customer.class))).thenReturn(saved);

		CustomerDTO result = customerService.createCustomer(dto);

		assertNotNull(result.getId());
		assertEquals("New", result.getFirstName());
		assertEquals("new@customer.com", result.getEmail());
	}

	// Test updateCustomer success
	@Test
	void testUpdateCustomer_Success() {
		Customer existing = new Customer();
		existing.setId(1L);
		existing.setFirstName("Old");
		existing.setLastName("Name");
		existing.setEmail("old@email.com");
		existing.setAddress("Delhi");
		existing.setPhone("9999999999");

		CustomerDTO dto = new CustomerDTO(1L, "Updated", "Name", "updated@email.com", "8888888888", "Delhi");

		Customer updated = new Customer();
		updated.setId(1L);
		updated.setFirstName("Updated");
		updated.setLastName("Name");
		updated.setEmail("updated@email.com");
		updated.setAddress("Delhi");
		updated.setPhone("8888888888");

		when(customerRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(customerRepo.save(any(Customer.class))).thenReturn(updated);

		CustomerDTO result = customerService.updateCustomer(1L, dto);

		assertEquals("Updated", result.getFirstName());
		assertEquals("updated@email.com", result.getEmail());
		assertEquals("8888888888", result.getPhone());
	}

	// Test updateCustomer throws exception
	@Test
	void testUpdateCustomer_NotFound() {
		CustomerDTO dto = new CustomerDTO(1L, "Updated", "Name", "updated@email.com", "8888888888", "Delhi");

		when(customerRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(1L, dto));
	}

	// Test deleteCustomer success
	@Test
	void testDeleteCustomer_Success() {
		when(customerRepo.existsById(1L)).thenReturn(true);
		doNothing().when(customerRepo).deleteById(1L);

		assertDoesNotThrow(() -> customerService.deleteCustomer(1L));
		verify(customerRepo, times(1)).deleteById(1L);
	}

	// Test deleteCustomer throws exception
	@Test
	void testDeleteCustomer_NotFound() {
		when(customerRepo.existsById(1L)).thenReturn(false);

		assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1L));
	}
}
