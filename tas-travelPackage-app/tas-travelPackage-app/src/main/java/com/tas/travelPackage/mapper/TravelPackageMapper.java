package com.tas.travelPackage.mapper;

import com.tas.travelPackage.dto.TravelPackageDTO;
import com.tas.travelPackage.entity.TravelPackage;

public class TravelPackageMapper {
	
	//Convert Entity->DTO
	public static TravelPackageDTO toDto(TravelPackage travelPackage)
	{
		TravelPackageDTO dto=new TravelPackageDTO();
		
		dto.setId(travelPackage.getId());
		dto.setName(travelPackage.getName());
		dto.setPrice(travelPackage.getPrice());
		dto.setDescription(travelPackage.getDescription());
		dto.setAvailableTo(travelPackage.getAvailableTo());
		dto.setAvailableFrom(travelPackage.getAvailableFrom());
		dto.setAgencyId(travelPackage.getAgencyId());     
        dto.setAgencyName(travelPackage.getAgencyName());
		
		return dto;
	}
	//Convert DTO->Entity
	public static TravelPackage toEntity(TravelPackageDTO dto)
	{
		TravelPackage entity=new TravelPackage();
		if (dto.getId() != null) {
			entity.setId(dto.getId());
        }
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setAvailableFrom(dto.getAvailableFrom());
		entity.setAvailableTo(dto.getAvailableTo());
		entity.setAgencyId(dto.getAgencyId());     
        entity.setAgencyName(dto.getAgencyName());
		return entity;
		
	}
}