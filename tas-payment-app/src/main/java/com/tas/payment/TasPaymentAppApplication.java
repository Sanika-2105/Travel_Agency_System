package com.tas.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TasPaymentAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasPaymentAppApplication.class, args);
	}

}
