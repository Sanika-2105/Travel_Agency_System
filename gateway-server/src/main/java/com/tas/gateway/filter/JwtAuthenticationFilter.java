
package com.tas.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.tas.gateway.security.JwtUtil;
import com.tas.gateway.util.GatewayErrorUtil;

import reactor.core.publisher.Mono;



@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();


System.out.println(">>> PATH: " + path);

        // Allow auth endpoints
        if (path.startsWith("/tas/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println(">>> AUTH HEADER: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return GatewayErrorUtil.writeError(
                exchange,
                HttpStatus.UNAUTHORIZED,
                "Missing or invalid Authorization header"
            );
        }

        String token = authHeader.substring(7);
        System.out.println(">>> TOKEN: " + token);

        try {
            // Validate token signature + expiry
            jwtUtil.validateToken(token);

            if (jwtUtil.isTokenExpired(token)) {
                return GatewayErrorUtil.writeError(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    "JWT token expired"
                );
            }

        } 
        catch (Exception ex) {
            ex.printStackTrace(); // ✅ IMPORTANT
            return GatewayErrorUtil.writeError(
                exchange,
                HttpStatus.UNAUTHORIZED,
                "Invalid JWT token"
            );
        }


        // ✅ Token is valid → allow request
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
