package com.tas.travelPackage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tas.travelPackage.dto.TravelPackageDTO;


public interface TravelPackageService {
	
	public List<TravelPackageDTO> getAllPackages();
	
	public TravelPackageDTO getPackageById(Long id);
	
	public TravelPackageDTO createPackage(TravelPackageDTO dto);
	
	public TravelPackageDTO updatePackage(Long id, TravelPackageDTO dto);
	
	public void deletePackage(Long id);
	
	List<TravelPackageDTO> getPackagesByAgencyId(Long agencyId);

}
