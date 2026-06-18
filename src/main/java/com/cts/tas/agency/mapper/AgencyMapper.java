package com.cts.tas.agency.mapper;

import com.cts.tas.agency.dto.AgencyDTO;
import com.cts.tas.agency.entity.Agency;

public class AgencyMapper {

	//Entity->DTO
	public static AgencyDTO toDto(Agency agency)
	{
		AgencyDTO agencyDto=new AgencyDTO();
		agencyDto.setId(agency.getId());
		agencyDto.setName(agency.getName());
		agencyDto.setContactEmail(agency.getContactEmail());
		agencyDto.setAddress(agency.getAddress());
		return agencyDto;
		
	}
	
	//DTO->Entity
	
	public static Agency toEntity(AgencyDTO dto)
	{
		Agency agency=new Agency();
		if (dto.getId() != null) {
            agency.setId(dto.getId());
        }
		agency.setName(dto.getName());
		agency.setContactEmail(dto.getContactEmail());
		agency.setAddress(dto.getAddress());
		return agency;
	}
}
