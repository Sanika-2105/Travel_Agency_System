package com.tas.travelPackage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TasTravelPackageAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasTravelPackageAppApplication.class, args);
	}

}
