package com.twitter.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.user_service.dto.UserDto;
import com.twitter.user_service.exception.UserException;
import com.twitter.user_service.mapper.UserDtoMapper;
import com.twitter.user_service.model.User;
import com.twitter.user_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/internal/users")
public class InternalController {

	private UserService userService;

	public InternalController(UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "[Internal] Create user profile after auth signup")
	@PostMapping("/create")
	public ResponseEntity<UserDto> createNewUser(User user) throws UserException {

		User created = userService.createUser(user);

		UserDto dto = UserDtoMapper.toUserDto(created);

		return ResponseEntity.status(201).body(dto);

	}
}
