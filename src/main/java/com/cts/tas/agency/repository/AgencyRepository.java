package com.cts.tas.agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.tas.agency.entity.Agency;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {

}
