package com.tas.travelPackage.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tas.travelPackage.dto.TravelPackageDTO;
import com.tas.travelPackage.service.TravelPackageService;

@ExtendWith(MockitoExtension.class)
class TravelPackageRestControllerTest {

	@Mock
	private TravelPackageService travelPackageService;

	@InjectMocks
	private TravelPackageRestController travelPackageController;

	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(travelPackageController).build();
	}

	// Test: Get all packages
	@Test
	void testGetAllPackages() throws Exception {
		TravelPackageDTO pkg1 = new TravelPackageDTO(1L, "Goa Trip", "Beach holiday", 15000.0, LocalDate.of(2026, 5, 1),
				LocalDate.of(2026, 5, 31), 5L, "Holiday Travels");
		TravelPackageDTO pkg2 = new TravelPackageDTO(2L, "Kerala Backwaters", "Houseboat tour", 20000.0,
				LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 15), 5L, "Holiday Travels");

		when(travelPackageService.getAllPackages()).thenReturn(Arrays.asList(pkg1, pkg2));

		mockMvc.perform(get("/travel-packages")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Goa Trip"))
				.andExpect(jsonPath("$[1].name").value("Kerala Backwaters"));
	}

	// Test: Get package by ID
	@Test
	void testGetPackageById() throws Exception {
		TravelPackageDTO pkg = new TravelPackageDTO(1L, "Goa Trip", "Beach holiday", 15000.0, LocalDate.of(2026, 5, 1),
				LocalDate.of(2026, 5, 31), 5L, "Holiday Travels");

		when(travelPackageService.getPackageById(1L)).thenReturn(pkg);

		mockMvc.perform(get("/travel-packages/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Goa Trip"))
				.andExpect(jsonPath("$.agencyName").value("Holiday Travels"));
	}
	

	// Test: Create new package
	@Test
	void testCreatePackage() throws Exception {
		TravelPackageDTO response = new TravelPackageDTO(1L, "Goa Trip", "Beach holiday", 15000.0,
				LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31), 5L, "Holiday Travels");

		when(travelPackageService.createPackage(any(TravelPackageDTO.class))).thenReturn(response);

		mockMvc.perform(post("/travel-packages").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Goa Trip\",\"description\":\"Beach holiday\",\"price\":15000,"
						+ "\"availableFrom\":\"2026-05-01\",\"availableTo\":\"2026-05-31\",\"agencyId\":5}"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Goa Trip"));
	}

	// Test: Update package
	@Test
	void testUpdatePackage() throws Exception {
		TravelPackageDTO updated = new TravelPackageDTO(1L, "Goa Trip Updated", "Beach holiday", 16000.0,
				LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31), 5L, "Holiday Travels");

		when(travelPackageService.updatePackage(eq(1L), any(TravelPackageDTO.class))).thenReturn(updated);

		mockMvc.perform(put("/travel-packages/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Goa Trip Updated\",\"description\":\"Beach holiday\",\"price\":16000,"
						+ "\"availableFrom\":\"2026-05-01\",\"availableTo\":\"2026-05-31\",\"agencyId\":5}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Goa Trip Updated"))
				.andExpect(jsonPath("$.price").value(16000.0));
	}

	// Test: Delete package
	@Test
	void testDeletePackage() throws Exception {
		doNothing().when(travelPackageService).deletePackage(1L);

		mockMvc.perform(delete("/travel-packages/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Travel package with id 1 deleted successfully"));
	}

	// Test: Get packages by Agency ID
	@Test
	void testGetPackagesByAgencyId() throws Exception {
		TravelPackageDTO pkg = new TravelPackageDTO(101L, "Summer Goa Trip", "Beach holiday", 15000.0,
				LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31), 5L, "Holiday Travels");

		when(travelPackageService.getPackagesByAgencyId(5L)).thenReturn(Arrays.asList(pkg));

		mockMvc.perform(get("/travel-packages/agency/5")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].agencyName").value("Holiday Travels"))
				.andExpect(jsonPath("$[0].agencyId").value(5));
	}

	// Test: Get packages by Agency ID - Not Found
	@Test
	void testGetPackagesByAgencyId_NotFound() throws Exception {
		when(travelPackageService.getPackagesByAgencyId(999L)).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/travel-packages/agency/999")).andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
	}
}
