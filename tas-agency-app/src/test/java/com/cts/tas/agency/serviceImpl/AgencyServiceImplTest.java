package com.cts.tas.agency.serviceImpl;

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

import com.cts.tas.agency.dto.AgencyDTO;
import com.cts.tas.agency.entity.Agency;
import com.cts.tas.agency.exceptions.AgencyNotFoundException;
import com.cts.tas.agency.repository.AgencyRepository;

class AgencyServiceImplTest {
	@Mock
	private AgencyRepository agencyRepo;

	@InjectMocks
	private AgencyServiceImpl agencyService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// fetch all agencies
	@Test
	void testGetAllAgencies() {
		Agency agency1 = new Agency();
		agency1.setId(1L);
		agency1.setName("Sky High Travels");
		agency1.setContactEmail("sky@agency.com");
		agency1.setAddress("Hyderabad");

		Agency agency2 = new Agency();
		agency2.setId(2L);
		agency2.setName("Mountain Tours");
		agency2.setContactEmail("mountain@agency.com");
		agency2.setAddress("Delhi");

		when(agencyRepo.findAll()).thenReturn(Arrays.asList(agency1, agency2));

		List<AgencyDTO> result = agencyService.getAllAgencies();

		assertEquals(2, result.size());
		assertEquals("Sky High Travels", result.get(0).getName());
	}

	// get agency by id
	@Test
	void testGetAgencyById_Success() {
		Agency agency = new Agency();
		agency.setId(1L);
		agency.setName("Sky High Travels");
		agency.setContactEmail("sky@agency.com");
		agency.setAddress("Hyderabad");

		when(agencyRepo.findById(1L)).thenReturn(Optional.of(agency));

		AgencyDTO result = agencyService.getAgencyById(1L);

		assertEquals("Sky High Travels", result.getName());
		assertEquals("sky@agency.com", result.getContactEmail());
	}

	@Test
	void testGetAgencyById_NotFound() {
		when(agencyRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(AgencyNotFoundException.class, () -> agencyService.getAgencyById(1L));
	}

	// test createAgency
	@Test
	void testCreateAgency() {
		AgencyDTO dto = new AgencyDTO(null, "New Agency", "new@agency.com", "Mumbai");

		Agency agency = new Agency();
		agency.setName(dto.getName());
		agency.setContactEmail(dto.getContactEmail());
		agency.setAddress(dto.getAddress());

		Agency savedAgency = new Agency();
		savedAgency.setId(1L);
		savedAgency.setName("New Agency");
		savedAgency.setContactEmail("new@agency.com");
		savedAgency.setAddress("Mumbai");

		when(agencyRepo.save(any(Agency.class))).thenReturn(savedAgency);

		AgencyDTO result = agencyService.createAgency(dto);

		assertNotNull(result.getId());
		assertEquals("New Agency", result.getName());
		assertEquals("new@agency.com", result.getContactEmail());
		assertEquals("Mumbai", result.getAddress());
	}

	@Test
	void testUpdateAgency_Success() {
		Agency existing = new Agency();
		existing.setId(1L);
		existing.setName("Old Name");
		existing.setContactEmail("old@agency.com");
		existing.setAddress("Delhi");

		AgencyDTO dto = new AgencyDTO(1L, "Updated Name", "updated@agency.com", "Delhi");

		when(agencyRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(agencyRepo.save(any(Agency.class))).thenReturn(existing);

		AgencyDTO result = agencyService.updateAgency(1L, dto);

		assertEquals("Updated Name", result.getName());
		assertEquals("updated@agency.com", result.getContactEmail());
	}

	// Test updateAgency throws exception
	@Test
	void testUpdateAgency_NotFound() {
		AgencyDTO dto = new AgencyDTO(1L, "Updated Name", "updated@agency.com", "Delhi");

		when(agencyRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(AgencyNotFoundException.class, () -> agencyService.updateAgency(1L, dto));
	}

	// Test deleteAgency success
	@Test
	void testDeleteAgency_Success() {
		// build agency with setters
		Agency existing = new Agency();
		existing.setId(1L);
		existing.setName("Sky High Travels");
		existing.setContactEmail("sky@agency.com");
		existing.setAddress("Hyderabad");

		when(agencyRepo.existsById(1L)).thenReturn(true);
		doNothing().when(agencyRepo).deleteById(1L);

		assertDoesNotThrow(() -> agencyService.deleteAgency(1L));
		verify(agencyRepo, times(1)).deleteById(1L);
	}

	// Test deleteAgency throws exception
	@Test
	void testDeleteAgency_NotFound() {
		when(agencyRepo.existsById(1L)).thenReturn(false);

		assertThrows(AgencyNotFoundException.class, () -> agencyService.deleteAgency(1L));
	}

}
