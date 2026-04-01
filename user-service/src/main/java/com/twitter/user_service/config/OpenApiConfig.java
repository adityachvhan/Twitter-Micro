package com.twitter.user_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI userServiceOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Twitter User Service API")
						.description("Manages user profiles, follow relationships, and search. "
								+ "All endpoints expect the X-Auth-User-Email header "
								+ "injected by the API gateway after JWT validation.")
						.version("1.0.0")
						.contact(new Contact().name("Twitter Microservices").email("dev@twitter-clone.com")))
				.servers(List.of(new Server().url("http://localhost:8082").description("Local user-service"),
						new Server().url("http://localhost:8080").description("Via API gateway")));
	}
}
