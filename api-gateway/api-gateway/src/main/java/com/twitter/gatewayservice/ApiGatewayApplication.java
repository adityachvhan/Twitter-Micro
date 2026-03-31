package com.twitter.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway — single entry point for all client requests.
 *
 * Responsibilities:
 *   - JWT validation (extracts email, injects X-Auth-User-Email header)
 *   - Route requests to the correct downstream microservice via Eureka
 *   - CORS for frontend clients
 *   - Block /internal/** paths from external access
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
