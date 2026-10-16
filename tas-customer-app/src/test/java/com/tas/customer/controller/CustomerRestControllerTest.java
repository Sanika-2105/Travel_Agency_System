package com.tas.customer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tas.customer.dto.CustomerDTO;
import com.tas.customer.service.CustomerService;

@ExtendWith(MockitoExtension.class)
class CustomerRestControllerTest {

	@Mock
	private CustomerService customerService;

	@InjectMocks
	private CustomerRestController customerController;

	private MockMvc mockMvc;

	@BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }
	//Test get all customers
	@Test
	void testGetAllCustomers() throws Exception {
        CustomerDTO c1 = new CustomerDTO(1L, "John", "Doe", "Hyderabad", "john@doe.com", "1234567890");
        CustomerDTO c2 = new CustomerDTO(2L, "Jane", "Smith", "Delhi", "jane@smith.com", "9876543210");

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/customers"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].firstName").value("John"))
               .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }
	//test get customer by id
	@Test
    void testGetCustomerById() throws Exception {
        CustomerDTO c = new CustomerDTO();
        c.setId(1L);
        c.setFirstName("John");
        c.setLastName("Doe");
        c.setEmail("john@doe.com");
        c.setPhone("1234567890");
        c.setAddress("Hyderabad");

        when(customerService.getCustomerById(1L)).thenReturn(c);

        mockMvc.perform(get("/customers/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.firstName").value("John"))
               .andExpect(jsonPath("$.email").value("john@doe.com"));
	}

	// Test create customer
	@Test
	void testCreateCustomer() throws Exception {
	    CustomerDTO response = new CustomerDTO();
	    response.setId(1L);
	    response.setFirstName("New");
	    response.setLastName("Customer");
	    response.setEmail("new@customer.com");
	    response.setPhone("1112223330"); // ✅ 10 digits
	    response.setAddress("Mumbai");

	    when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(response);

	    mockMvc.perform(post("/customers")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"firstName\":\"New\",\"lastName\":\"Customer\",\"address\":\"Mumbai\",\"email\":\"new@customer.com\",\"phone\":\"1112223330\"}"))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$.id").value(1))
	           .andExpect(jsonPath("$.firstName").value("New"))
	           .andExpect(jsonPath("$.email").value("new@customer.com"));
	}

	// Test update customer
	@Test
	void testUpdateCustomer() throws Exception {
	    CustomerDTO updated = new CustomerDTO();
	    updated.setId(1L);
	    updated.setFirstName("Updated");
	    updated.setLastName("Name");
	    updated.setEmail("updated@email.com");
	    updated.setPhone("8888888888"); // ✅ 10 digits
	    updated.setAddress("Delhi");

	    when(customerService.updateCustomer(eq(1L), any(CustomerDTO.class))).thenReturn(updated);

	    mockMvc.perform(put("/customers/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"firstName\":\"Updated\",\"lastName\":\"Name\",\"address\":\"Delhi\",\"email\":\"updated@email.com\",\"phone\":\"8888888888\"}"))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$.id").value(1))
	           .andExpect(jsonPath("$.firstName").value("Updated"))
	           .andExpect(jsonPath("$.email").value("updated@email.com"));
	}

	// Test delete customer
    @Test
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customers/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Customer with id 1 deleted successfully"));
    }

}
