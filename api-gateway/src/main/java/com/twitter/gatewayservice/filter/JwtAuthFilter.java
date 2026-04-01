package com.twitter.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

/**
 * JWT validation filter applied to all protected routes.
 *
 * On success:
 *   - Strips the original Authorization header
 *   - Injects X-Auth-User-Email header with the verified email
 *   - Downstream services trust this header — they never touch JWTs
 *
 * On failure:
 *   - Returns 401 Unauthorized immediately, request never reaches downstream
 */
@Component
@Slf4j
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    @Value("${app.jwt.secret}")
    private String secretKey;

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Extract Authorization header
            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or malformed Authorization header for path: {}", request.getPath());
                return unauthorized(exchange);
            }

            // 2. Parse and validate JWT
            String jwt = authHeader.substring(7);
            String email;
            try {
                SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                email = String.valueOf(claims.get("email"));
            } catch (Exception e) {
                log.warn("Invalid JWT token: {}", e.getMessage());
                return unauthorized(exchange);
            }

            // 3. Inject verified email as header, forward request
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-Auth-User-Email", email)
                    .build();

            log.debug("JWT validated for email: {} → {}", email, request.getPath());
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {
        // No config fields needed — filter is stateless
    }
}
