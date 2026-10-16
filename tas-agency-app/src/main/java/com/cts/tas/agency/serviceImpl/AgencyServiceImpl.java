package com.cts.tas.agency.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cts.tas.agency.dto.AgencyDTO;
import com.cts.tas.agency.entity.Agency;
import com.cts.tas.agency.exceptions.AgencyNotFoundException;
import com.cts.tas.agency.mapper.AgencyMapper;
import com.cts.tas.agency.repository.AgencyRepository;
import com.cts.tas.agency.service.AgencyService;

@Service
public class AgencyServiceImpl implements AgencyService {
	
	private final AgencyRepository agencyRepo;
	
	//constructor injection
	public AgencyServiceImpl(AgencyRepository agencyRepo) {
		super();
		this.agencyRepo = agencyRepo;
	}

	//get all agencies
	@Override
	public List<AgencyDTO> getAllAgencies() {
		
		return agencyRepo.findAll()
						 .stream()
						 .map(AgencyMapper::toDto)  //entity to dto
						 .collect(Collectors.toList());
	}

	//get agency based on id
	@Override
	public AgencyDTO getAgencyById(Long id) {
		Agency agency=agencyRepo.findById(id)
				.orElseThrow(() -> new AgencyNotFoundException(id));
        return AgencyMapper.toDto(agency);
		
	}

	//Create new Agency
	@Override
	public AgencyDTO createAgency(AgencyDTO dto) {
		Agency agency=AgencyMapper.toEntity(dto);
		Agency savedAgency=agencyRepo.save(agency);
		return AgencyMapper.toDto(savedAgency);
	}
	//update agency
	@Override
	public AgencyDTO updateAgency(Long id, AgencyDTO dto) {
		Agency existingAgency=agencyRepo.findById(id)
										 .orElseThrow(()->new AgencyNotFoundException("Agency not found with id: "+id));
		existingAgency.setName(dto.getName());
		existingAgency.setContactEmail(dto.getContactEmail());
		existingAgency.setAddress(dto.getAddress());
		
		Agency savedAgency=agencyRepo.save(existingAgency);
		return AgencyMapper.toDto(savedAgency);
		
	}
	//Delete agency
	@Override
	public void deleteAgency(Long id)
	{
		if (!agencyRepo.existsById(id)) {
            throw new AgencyNotFoundException(id); // handled by GlobalExceptionHandler
        }
        agencyRepo.deleteById(id);
	}

}
