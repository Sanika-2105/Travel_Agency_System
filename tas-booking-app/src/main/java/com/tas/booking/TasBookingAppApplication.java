package com.tas.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TasBookingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasBookingAppApplication.class, args);
	}

}
