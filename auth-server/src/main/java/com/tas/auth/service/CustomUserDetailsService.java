package com.tas.auth.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tas.auth.dto.JwtResponse;
import com.tas.auth.dto.LoginRequest;
import com.tas.auth.dto.RegisterResponseDTO;
import com.tas.auth.entity.UserCredential;
import com.tas.auth.exception.InvalidPasswordException;
import com.tas.auth.exception.UserNotFoundException;
import com.tas.auth.mapper.Mapper;
import com.tas.auth.repository.UserCredentialRepository;
import com.tas.auth.security.jwt.JwtUtil;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserCredentialRepository repo;
	private PasswordEncoder passwordEncoder;
	private JwtUtil jwtUtil;

	public CustomUserDetailsService(UserCredentialRepository repo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.passwordEncoder = passwordEncoder;
		this.repo = repo;
		this.jwtUtil = jwtUtil;
	}

	public RegisterResponseDTO registerNewUser(LoginRequest user) {
		if (user.getUsername().length() == 0) {
			throw new InvalidPasswordException("Invalid username");
		}

		if (user.getPassword().length() == 0) {
			throw new InvalidPasswordException("Invalid password");
		}

		// hashing the password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println(user.getPassword());

		// mapping request dto to entity
		UserCredential userCredentialEntity = Mapper.toUserCredentialEntity(user);

		// Save user
		UserCredential savedUser = repo.save(userCredentialEntity);

		// Build response DTO
		RegisterResponseDTO response = new RegisterResponseDTO(savedUser.getId(), savedUser.getUsername());

		return response;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserCredential user = repo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		// Build UserDetails with no authorities
		return User.withUsername(user.getUsername()).password(user.getPassword()).authorities(Collections.emptyList()) // required,
																														// even
																														// if
																														// empty
				.build();
	}

	public JwtResponse loginByUserNamePassword(LoginRequest loginRequestDto) {
		System.out.println("username" + loginRequestDto.getUsername());
		// 1. fetch the user details
		UserCredential userProfileEntity = repo.findByUsername(loginRequestDto.getUsername())
				.orElseThrow(() -> new UserNotFoundException("Invalid username."));
		System.out.println("username" + userProfileEntity.getUsername());
		// 2. check if password is correct or not
		if (!passwordEncoder.matches(loginRequestDto.getPassword(), userProfileEntity.getPassword())) {
			throw new InvalidPasswordException("Invalid Password.");
		}

		// 3. If authentication is successful, then generate JWT token
		String jwtToken = jwtUtil.generateToken(userProfileEntity.getUsername());

		// 4. generate LoginResponseDTO
		JwtResponse loginResponseDto = new JwtResponse();
		loginResponseDto.setToken(jwtToken);

		return loginResponseDto;
	}

}
