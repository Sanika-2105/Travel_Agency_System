package com.cts.tas.agency.service;

import java.util.List;

import com.cts.tas.agency.dto.AgencyDTO;

public interface AgencyService {

	public AgencyDTO createAgency(AgencyDTO dto);
	public List<AgencyDTO> getAllAgencies();
	public AgencyDTO getAgencyById(Long id);
	public AgencyDTO updateAgency(Long id,AgencyDTO dto);
	public void deleteAgency(Long id);
	
}
