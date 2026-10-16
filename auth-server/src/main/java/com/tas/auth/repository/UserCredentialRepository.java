package com.tas.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tas.auth.dto.LoginRequest;
import com.tas.auth.entity.UserCredential;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
	Optional<UserCredential> findByUsername(String username);

	LoginRequest save(LoginRequest user);

}
