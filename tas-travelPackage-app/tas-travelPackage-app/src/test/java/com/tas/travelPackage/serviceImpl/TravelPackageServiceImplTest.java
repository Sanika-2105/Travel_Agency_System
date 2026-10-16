package com.tas.travelPackage.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tas.travelPackage.client.AgencyClient;
import com.tas.travelPackage.dto.TravelPackageDTO;
import com.tas.travelPackage.entity.TravelPackage;
import com.tas.travelPackage.exception.AgencyNotFoundException;
import com.tas.travelPackage.exception.TravelPackageNotFoundException;
import com.tas.travelPackage.repository.TravelPackageRepository;

@ExtendWith(MockitoExtension.class)

class TravelPackageServiceImplTest {
	@Mock
    private TravelPackageRepository travelPackageRepo;

    @Mock
    private AgencyClient agencyClient;

    @InjectMocks
    private TravelPackageServiceImpl travelPackageService;
    
    private TravelPackage buildPackage() {
        TravelPackage pkg = new TravelPackage();
        pkg.setId(1L);
        pkg.setName("Goa Trip");
        pkg.setPrice(15000.0);
        pkg.setDescription("Beach holiday");
        pkg.setAvailableFrom(LocalDate.of(2026, 5, 1));
        pkg.setAvailableTo(LocalDate.of(2026, 5, 31));
        pkg.setAgencyId(5L);
        pkg.setAgencyName("Holiday Travels");
        return pkg;
    }
    
    @Test
    void testGetAllPackages() {
        when(travelPackageRepo.findAll()).thenReturn(Arrays.asList(buildPackage()));

        var result = travelPackageService.getAllPackages();

        assertEquals(1, result.size());
        assertEquals("Goa Trip", result.get(0).getName());
    }

    @Test
    void testGetPackageById_Found() {
        when(travelPackageRepo.findById(1L)).thenReturn(Optional.of(buildPackage()));

        var result = travelPackageService.getPackageById(1L);

        assertEquals("Goa Trip", result.getName());
    }
    
    @Test
    void testGetPackageById_NotFound() {
        when(travelPackageRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TravelPackageNotFoundException.class,
                () -> travelPackageService.getPackageById(99L));
    }
    @Test
    void testCreatePackage_Success() {
        // Arrange: input DTO
        TravelPackageDTO dto = new TravelPackageDTO();
        dto.setName("Goa Trip");
        dto.setDescription("Beach holiday");
        dto.setPrice(15000.0);
        dto.setAvailableFrom(LocalDate.of(2026, 5, 1));
        dto.setAvailableTo(LocalDate.of(2026, 5, 31));
        dto.setAgencyId(5L);

        // Arrange: mock agency (Feign client DTO)
        com.tas.travelPackage.client.dto.AgencyDTO agency = new com.tas.travelPackage.client.dto.AgencyDTO();
        agency.setId(5L);
        agency.setName("Holiday Travels");

        when(agencyClient.getAgencyById(5L)).thenReturn(agency);
        when(travelPackageRepo.save(any(TravelPackage.class))).thenReturn(buildPackage());

        // Act
        TravelPackageDTO result = travelPackageService.createPackage(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Goa Trip", result.getName());
        assertEquals("Holiday Travels", result.getAgencyName());
        verify(travelPackageRepo, times(1)).save(any(TravelPackage.class));
    }
    @Test
    void testCreatePackage_InvalidAgencyId() {
        TravelPackageDTO dto = new TravelPackageDTO();
        dto.setAgencyId(0L);

        assertThrows(IllegalArgumentException.class,
                () -> travelPackageService.createPackage(dto));
    }
    
    @Test
    void testCreatePackage_AgencyNotFound() {
        TravelPackageDTO dto = new TravelPackageDTO();
        dto.setAgencyId(999L);

        when(agencyClient.getAgencyById(999L)).thenReturn(null);

        assertThrows(AgencyNotFoundException.class,
                () -> travelPackageService.createPackage(dto));
    }

    @Test
    void testUpdatePackage_Success() {
        when(travelPackageRepo.findById(1L)).thenReturn(Optional.of(buildPackage()));
        when(travelPackageRepo.save(any(TravelPackage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TravelPackageDTO dto = new TravelPackageDTO();
        dto.setName("Updated Trip");
        dto.setPrice(16000.0);

        var result = travelPackageService.updatePackage(1L, dto);

        assertEquals("Updated Trip", result.getName());
        assertEquals(16000.0, result.getPrice());
    }
    @Test
    void testUpdatePackage_NotFound() {
        when(travelPackageRepo.findById(99L)).thenReturn(Optional.empty());

        TravelPackageDTO dto = new TravelPackageDTO();
        dto.setName("Updated Trip");

        assertThrows(TravelPackageNotFoundException.class,
                () -> travelPackageService.updatePackage(99L, dto));
    }
    
    @Test
    void testDeletePackage_Success() {
        when(travelPackageRepo.existsById(1L)).thenReturn(true);
        doNothing().when(travelPackageRepo).deleteById(1L);

        assertDoesNotThrow(() -> travelPackageService.deletePackage(1L));
    }

    @Test
    void testDeletePackage_NotFound() {
        when(travelPackageRepo.existsById(99L)).thenReturn(false);

        assertThrows(TravelPackageNotFoundException.class,
                () -> travelPackageService.deletePackage(99L));
    }
    
    @Test
    void testGetPackagesByAgencyId_Success() {
        when(travelPackageRepo.findByAgencyId(5L)).thenReturn(Arrays.asList(buildPackage()));

        var result = travelPackageService.getPackagesByAgencyId(5L);

        assertEquals(1, result.size());
        assertEquals("Holiday Travels", result.get(0).getAgencyName());
    }

    @Test
    void testGetPackagesByAgencyId_NotFound() {
        when(travelPackageRepo.findByAgencyId(999L)).thenReturn(Collections.emptyList());

        assertThrows(AgencyNotFoundException.class,
                () -> travelPackageService.getPackagesByAgencyId(999L));
    }
	

}
