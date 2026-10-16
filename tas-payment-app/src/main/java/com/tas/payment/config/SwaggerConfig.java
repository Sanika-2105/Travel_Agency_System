package com.tas.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
@Configuration
public class SwaggerConfig {
	@Bean
    public OpenAPI agencyOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Payment Service API")
                .description("API documentation for managing payments")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Sanika Joshi")
                    .email("SanikaVijay.Joshi@cognizant.com")));
    }
}
