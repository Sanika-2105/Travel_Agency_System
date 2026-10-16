package com.tas.travelPackage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.travelPackage.entity.TravelPackage;
@Repository
public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {
	List<TravelPackage> findByAgencyId(Long agencyId);
}
