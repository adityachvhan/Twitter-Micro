package com.twitter.user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

	@GetMapping("/message")
	@Operation(summary = "this is testing end point to check if it is running or not..")
	public String getMessage() {

		return "User Service is now up and Running..";
	}
}
