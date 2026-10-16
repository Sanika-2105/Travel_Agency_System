package com.tas.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tas.auth.dto.JwtResponse;
import com.tas.auth.dto.LoginRequest;
import com.tas.auth.dto.RegisterResponseDTO;
import com.tas.auth.service.CustomUserDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@PostMapping("/register")
	public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody LoginRequest user) {
System.out.println(user.getPassword());
		RegisterResponseDTO response = userDetailsService.registerNewUser(user);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		JwtResponse token = userDetailsService.loginByUserNamePassword(request);

		return ResponseEntity.ok().body(token);
	}
}
