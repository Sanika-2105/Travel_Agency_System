package com.tas.travelPackage.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tas.travelPackage.client.AgencyClient;
import com.tas.travelPackage.client.dto.AgencyDTO;
import com.tas.travelPackage.dto.TravelPackageDTO;
import com.tas.travelPackage.entity.TravelPackage;
import com.tas.travelPackage.exception.AgencyNotFoundException;
import com.tas.travelPackage.exception.TravelPackageNotFoundException;
import com.tas.travelPackage.mapper.TravelPackageMapper;
import com.tas.travelPackage.repository.TravelPackageRepository;
import com.tas.travelPackage.service.TravelPackageService;

import feign.FeignException;

@Service
public class TravelPackageServiceImpl implements TravelPackageService {

	private final TravelPackageRepository travelPackageRepo;
	private final AgencyClient agencyClient;

	// constructor injection
	public TravelPackageServiceImpl(TravelPackageRepository travelPackageRepo, AgencyClient agencyClient) {
		super();
		this.travelPackageRepo = travelPackageRepo;
		this.agencyClient = agencyClient;
	}

	// get all travel packages
	@Override
	public List<TravelPackageDTO> getAllPackages() {
		// TODO Auto-generated method stub
		return travelPackageRepo.findAll().stream().map(TravelPackageMapper::toDto).collect(Collectors.toList());

	}

	// get travel package by id
	@Override
	public TravelPackageDTO getPackageById(Long id) {
		TravelPackage travelPkg = travelPackageRepo.findById(id)
				.orElseThrow(() -> new TravelPackageNotFoundException("Travel package not found with id: " + id));

		return TravelPackageMapper.toDto(travelPkg);
	}

	// Create new travel package
	@Override
	public TravelPackageDTO createPackage(TravelPackageDTO dto) {
		// Validate agencyId
		if (dto.getAgencyId() == null || dto.getAgencyId() <= 0) {
			throw new IllegalArgumentException("Invalid agencyId: must be greater than zero");
		}

		AgencyDTO agency;
		try {
			agency= agencyClient.getAgencyById(dto.getAgencyId());
		}catch (FeignException.NotFound ex)
		{
			throw new AgencyNotFoundException("Agency not found with id: " + dto.getAgencyId());
		}
		
		TravelPackage travelPkg = TravelPackageMapper.toEntity(dto);

		travelPkg.setAgencyId(agency.getId());
		travelPkg.setAgencyName(agency.getName());

		TravelPackage savedPkg = travelPackageRepo.save(travelPkg);

		return TravelPackageMapper.toDto(savedPkg);

	}

	// update existing travel package
	@Override
	public TravelPackageDTO updatePackage(Long id, TravelPackageDTO dto) {
		TravelPackage existingPkg = travelPackageRepo.findById(id)
				.orElseThrow(() -> new TravelPackageNotFoundException("Travel package not found with id: " + id));
		existingPkg.setName(dto.getName());
		existingPkg.setPrice(dto.getPrice());
		existingPkg.setDescription(dto.getDescription());
		existingPkg.setAvailableFrom(dto.getAvailableFrom());
		existingPkg.setAvailableTo(dto.getAvailableTo());

		TravelPackage updatedPkg = travelPackageRepo.save(existingPkg);
		return TravelPackageMapper.toDto(updatedPkg);
	}

	// delete travel package
	@Override
	public void deletePackage(Long id) {
		if (!travelPackageRepo.existsById(id)) {
			throw new TravelPackageNotFoundException("Travel package not found with id: " + id);
		}
		travelPackageRepo.deleteById(id);

	}

	@Override
	public List<TravelPackageDTO> getPackagesByAgencyId(Long agencyId) {
		List<TravelPackage> packages = travelPackageRepo.findByAgencyId(agencyId);

		if (packages.isEmpty()) {
			throw new AgencyNotFoundException("No travel packages found for agencyId: " + agencyId);
		}

		return packages.stream().map(TravelPackageMapper::toDto).collect(Collectors.toList());
	}

}
