package com.tas.auth.mapper;

import com.tas.auth.dto.LoginRequest;
import com.tas.auth.entity.UserCredential;

public class Mapper {

	public static UserCredential toUserCredentialEntity(LoginRequest loginRequestDto)
	{
		//creating new object of type UserCredential
		UserCredential userCredentialEntity = new UserCredential();
				
		//setting value to the newly created object
		userCredentialEntity.setUsername(loginRequestDto.getUsername());
		userCredentialEntity.setPassword(loginRequestDto.getPassword());
		
		return userCredentialEntity;
		
	}
}
