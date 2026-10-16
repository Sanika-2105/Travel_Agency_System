package com.tas.gateway.util;


import java.nio.charset.StandardCharsets;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;


public class GatewayErrorUtil {

	public static Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, String message) {

		exchange.getResponse().setStatusCode(status);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

		String body = String.format("{\"status\":%d,\"error\":\"%s\"}", status.value(), message);

		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

		return exchange.getResponse().writeWith(Mono.just(buffer));
	}
}
