package com.tas.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Bean
	public RouteLocator

			customRoutings(RouteLocatorBuilder routeLocatorBuilder) {

		return routeLocatorBuilder.routes()

				.route("agency-route", r -> r.path("/agencies/**").uri("lb://tas-agency-app"))

				.route("customer-route", r -> r.path("/customers/**").uri("lb://tas-customer-app"))

				.route("booking-route", r -> r.path("/bookings/**").uri("lb://tas-booking-app"))

				.route("travel-package-route", r -> r.path("/travel-packages/**").uri("lb://tas-travelPackage-app"))

				.route("payment-route", r -> r.path("/payments/**").uri("lb://tas-payment-app"))

				.route("auth-route",
						r -> r.path("/tas/auth/**")
								.filters(f -> f.rewritePath("/tas/auth(?<segment>/.*)?", "/auth${segment}"))
								.uri("lb://auth-server"))

				.build();
	}

}
